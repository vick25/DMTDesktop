package com.osfac.dmt.workbench.datasource;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.datasource.FileDataSourceQueryChooser.FileChooserPanel;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedBasePlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.vividsolutions.jts.util.Assert;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/**
 * Prompts the user to pick a dataset to load.
 *
 * @see DataSourceQueryChooserDialog
 */
public class LoadDatasetPlugIn extends ThreadedBasePlugIn {

    private static String LAST_FORMAT_KEY = LoadDatasetPlugIn.class.getName()
            + " - LAST FORMAT";

    private DataSourceQueryChooserDialog getDialog(PlugInContext context) {
        Blackboard blackboard = context.getWorkbenchContext().getWorkbench().getBlackboard();
        String KEY = getClass().getName() + " - DIALOG";

        if (null == blackboard.get(KEY)) {
            DataSourceQueryChooserDialog dlg = new DataSourceQueryChooserDialog(
                    DataSourceQueryChooserManager.get(blackboard).getLoadDataSourceQueryChoosers(),
                    context.getWorkbenchFrame(),
                    getName(),
                    true);
            blackboard.put(KEY, dlg);

            //
            // JJ - If a user double clicks on a file then we want the same behaviour
            // as if they hit the OK button.
            // I'm not too famialiar with how people use the LoadDatasetPlugIn.
            // I have the impression that developers can add/remove choosers -
            // hence this "if" stuff.
            //
            Object obj = blackboard.get(LoadFileDataSourceQueryChooser.FILE_CHOOSER_KEY);
            if (obj != null && obj instanceof JFileChooser) {
                JFileChooser chooser = (JFileChooser) obj;
                chooser.addActionListener(new DoubleClickActionListener(dlg));
            }
        }
        ((DataSourceQueryChooserDialog) blackboard.get(KEY)).setDialogTask(DataSourceQueryChooserDialog.LOADDIALOG);
        return (DataSourceQueryChooserDialog) blackboard.get(KEY);
    }

    class DoubleClickActionListener implements ActionListener {

        private DataSourceQueryChooserDialog dlg;

        public DoubleClickActionListener(DataSourceQueryChooserDialog dlg) {
            this.dlg = dlg;
        }

        public void actionPerformed(ActionEvent e) {
            //
            // The dailog's setOKPressed method calls a isInputValid method
            // which calls actionPerformed and we get into an infinite loop
            // unless we add some extra logic here and carefully order the
            // method calls in setOKPressed.
            //
            if (!dlg.wasOKPressed()) {
                dlg.setOKPressed();
            }
        }
    }

    public String getName() {
        //Suggest that multiple datasets may be loaded [Bob Boseko 11/10/2003]
        return I18N.get("datasource.LoadDatasetPlugIn.load-dataset");
    }

    public void initialize(final PlugInContext context) throws Exception {
        //Give other plug-ins a chance to add DataSourceQueryChoosers
        //before the dialog is realized. [Bob Boseko]
        context.getWorkbenchFrame().addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                String format = (String) PersistentBlackboardPlugIn.get(context.getWorkbenchContext())
                        .get(LAST_FORMAT_KEY);
                if (format != null) {
                    getDialog(context).setSelectedFormat(format);
                }
            }
        });
    }

    public boolean execute(PlugInContext context) throws Exception {

        //Rescan current directory, otherwise, if the directory is updated, one has
        //to select the parent direcory then to come back to the current directory
        //in order to update the ui.
        FileChooserPanel fcp =
                (FileChooserPanel) context.getWorkbenchContext()
                .getBlackboard()
                .get(LoadFileDataSourceQueryChooser.FILE_CHOOSER_PANEL_KEY);
        if (fcp != null) {
            JFileChooser jfc = fcp.getChooser();
            jfc.rescanCurrentDirectory();
        }

        GUIUtil.centreOnWindow(getDialog(context));
        getDialog(context).setVisible(true);

        if (getDialog(context).wasOKPressed()) {
            PersistentBlackboardPlugIn.get(context.getWorkbenchContext()).put(LAST_FORMAT_KEY,
                    getDialog(context).getSelectedFormat());
        }

        return getDialog(context).wasOKPressed();
    }

    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        //Seamus Thomas Carroll [mailto:carrolls@cpsc.ucalgary.ca]
        //was concerned when he noticed that #getDataSourceQueries
        //was being called twice. So call it once only. [Bob Boseko 2004-02-05]
        Collection dataSourceQueries = getDialog(context).getCurrentChooser()
                .getDataSourceQueries();
        Assert.isTrue(!dataSourceQueries.isEmpty());

        boolean exceptionsEncountered = false;
        for (Iterator i = dataSourceQueries.iterator(); i.hasNext();) {
            DataSourceQuery dataSourceQuery = (DataSourceQuery) i.next();
            ArrayList exceptions = new ArrayList();
            Assert.isTrue(dataSourceQuery.getDataSource().isReadable());
            monitor.report("Loading " + dataSourceQuery.toString() + "...");

            Connection connection = dataSourceQuery.getDataSource()
                    .getConnection();
            try {
                FeatureCollection dataset = dataSourceQuery.getDataSource().installCoordinateSystem(connection.executeQuery(dataSourceQuery.getQuery(),
                        exceptions, monitor), CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()));
                if (dataset != null) {
                    context.getLayerManager()
                            .addLayer(chooseCategory(context),
                            dataSourceQuery.toString(), dataset)
                            .setDataSourceQuery(dataSourceQuery)
                            .setFeatureCollectionModified(false);
                }
            } finally {
                connection.close();
            }
            if (!exceptions.isEmpty()) {
                if (!exceptionsEncountered) {
                    context.getOutputFrame().createNewDocument();
                    exceptionsEncountered = true;
                }
                reportExceptions(exceptions, dataSourceQuery, context);
            }
        }
        if (exceptionsEncountered) {
            context.getWorkbenchFrame().warnUser(I18N.get("datasource.LoadDatasetPlugIn.problems-were-encountered"));
        }
    }

    private void reportExceptions(ArrayList exceptions,
            DataSourceQuery dataSourceQuery, PlugInContext context) {
        context.getOutputFrame().addHeader(1,
                exceptions.size() + " " + I18N.get("datasource.LoadDatasetPlugIn.problem") + StringUtil.s(exceptions.size())
                + " " + I18N.get("datasource.LoadDatasetPlugIn.loading") + " " + dataSourceQuery.toString() + "."
                + ((exceptions.size() > 10) ? I18N.get("datasource.LoadDatasetPlugIn.first-and-last-five") : ""));
        context.getOutputFrame().addText(I18N.get("datasource.LoadDatasetPlugIn.see-view-log"));
        context.getOutputFrame().append("<ul>");

        Collection exceptionsToReport = exceptions.size() <= 10 ? exceptions
                : CollectionUtil.concatenate(Arrays.asList(
                new Collection[]{
                    exceptions.subList(0, 5),
                    exceptions.subList(exceptions.size() - 5,
                    exceptions.size())
                }));
        for (Iterator j = exceptionsToReport.iterator(); j.hasNext();) {
            Exception exception = (Exception) j.next();
            context.getWorkbenchFrame().log(StringUtil.stackTrace(exception));
            context.getOutputFrame().append("<li>");
            context.getOutputFrame().append(GUIUtil.escapeHTML(
                    WorkbenchFrame.toMessage(exception), true, true));
            context.getOutputFrame().append("</li>");
        }
        context.getOutputFrame().append("</ul>");
    }

    private String chooseCategory(PlugInContext context) {
        return context.getLayerNamePanel().getSelectedCategories().isEmpty()
                ? StandardCategoryNames.WORKING
                : context.getLayerNamePanel().getSelectedCategories().iterator().next()
                .toString();
    }

    //[sstein 26.08.2006] added for toolbar
    public static MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck());
    }

    //[sstein 26.08.2006] added for toolbar
    public static ImageIcon getIcon() {
        return IconLoader.icon("Plus.gif");
    }
}
