package org.openjump.core.ui.plugin.file;

import com.osfac.dmt.I18N;
import com.osfac.dmt.coordsys.CoordinateSystemRegistry;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.io.datasource.Connection;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.util.CollectionUtil;
import com.osfac.dmt.util.StringUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.datasource.DataSourceQueryChooser;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.wizard.WizardDialog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.openjump.core.ui.plugin.file.open.ChooseProjectPanel;
import org.openjump.core.ui.swing.wizard.AbstractWizardGroup;

public class DataSourceQueryChooserOpenWizard extends AbstractWizardGroup {

    public static final String KEY = DataSourceQueryChooserOpenWizard.class.getName();
    private DataSourceQueryChooser chooser;
    private WorkbenchContext workbenchContext;
    private ChooseProjectPanel chooseProjectPanel;

    public DataSourceQueryChooserOpenWizard(WorkbenchContext workbenchContext,
            DataSourceQueryChooser chooser) {
        super(chooser.toString(), IconLoader.icon("Table.gif"), chooser.getClass()
                .getName());
        this.workbenchContext = workbenchContext;
        this.chooser = chooser;
    }

    public void initialize(WorkbenchContext workbenchContext, WizardDialog dialog) {
        removeAllPanels();
        ComponentWizardPanel componentPanel = new ComponentWizardPanel(
                chooser.toString(), chooser.getClass().getName(), chooser.getComponent());
        chooseProjectPanel = new ChooseProjectPanel(workbenchContext,
                componentPanel.getID());
        addPanel(chooseProjectPanel);
        addPanel(componentPanel);
    }

    public String getFirstId() {
        String firstId = super.getFirstId();
        if (!chooseProjectPanel.hasActiveTaskFrame()
                && chooseProjectPanel.hasTaskFrames()) {
            chooseProjectPanel.setNextID(firstId);
            return chooseProjectPanel.getID();
        } else {
            return firstId;
        }
    }

    public void run(WizardDialog dialog, TaskMonitor monitor) {
        if (chooser.isInputValid()) {
            chooseProjectPanel.activateSelectedProject();
            PlugInContext context = workbenchContext.createPlugInContext();
            Collection dataSourceQueries = chooser.getDataSourceQueries();
            if (!dataSourceQueries.isEmpty()) {

                boolean exceptionsEncountered = false;
                for (Iterator i = dataSourceQueries.iterator(); i.hasNext();) {
                    DataSourceQuery dataSourceQuery = (DataSourceQuery) i.next();
                    ArrayList exceptions = new ArrayList();
                    if (dataSourceQuery.getDataSource().isReadable()) {
                        monitor.report("Loading " + dataSourceQuery.toString() + "...");

                        Connection connection = dataSourceQuery.getDataSource()
                                .getConnection();
                        try {
                            FeatureCollection dataset = dataSourceQuery.getDataSource()
                                    .installCoordinateSystem(
                                    connection.executeQuery(dataSourceQuery.getQuery(),
                                    exceptions, monitor),
                                    CoordinateSystemRegistry.instance(workbenchContext.getBlackboard()));
                            if (dataset != null) {
                                context.getLayerManager().addLayer(chooseCategory(context),
                                        dataSourceQuery.toString(), dataset).setDataSourceQuery(
                                        dataSourceQuery).setFeatureCollectionModified(false);
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
                    } else {
                        context.getWorkbenchFrame().warnUser(
                                I18N.get("datasource.LoadDatasetPlugIn.query-not-readable"));
                    }
                }
                if (exceptionsEncountered) {
                    context.getWorkbenchFrame().warnUser(
                            I18N.get("datasource.LoadDatasetPlugIn.problems-were-encountered"));
                }
            } else {
                context.getWorkbenchFrame().warnUser(
                        I18N.get(KEY + ".no-queries-found"));
            }
        }
    }

    private String chooseCategory(PlugInContext context) {
        return context.getLayerNamePanel().getSelectedCategories().isEmpty() ? StandardCategoryNames.WORKING
                : context.getLayerNamePanel()
                .getSelectedCategories()
                .iterator()
                .next()
                .toString();
    }

    private void reportExceptions(ArrayList exceptions,
            DataSourceQuery dataSourceQuery, PlugInContext context) {
        context.getOutputFrame()
                .addHeader(
                1,
                exceptions.size()
                + " "
                + I18N.get("datasource.LoadDatasetPlugIn.problem")
                + StringUtil.s(exceptions.size())
                + " "
                + I18N.get("datasource.LoadDatasetPlugIn.loading")
                + " "
                + dataSourceQuery.toString()
                + "."
                + ((exceptions.size() > 10) ? I18N.get("datasource.LoadDatasetPlugIn.first-and-last-five")
                : ""));
        context.getOutputFrame().addText(
                I18N.get("datasource.LoadDatasetPlugIn.see-view-log"));
        context.getOutputFrame().append("<ul>");

        Collection exceptionsToReport = exceptions.size() <= 10 ? exceptions
                : CollectionUtil.concatenate(Arrays.asList(new Collection[]{
                    exceptions.subList(0, 5),
                    exceptions.subList(exceptions.size() - 5, exceptions.size())
                }));
        for (Iterator j = exceptionsToReport.iterator(); j.hasNext();) {
            Exception exception = (Exception) j.next();
            context.getWorkbenchFrame().log(StringUtil.stackTrace(exception));
            context.getOutputFrame().append("<li>");
            context.getOutputFrame().append(
                    GUIUtil.escapeHTML(WorkbenchFrame.toMessage(exception), true, true));
            context.getOutputFrame().append("</li>");
        }
        context.getOutputFrame().append("</ul>");
    }
}
