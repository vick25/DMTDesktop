package com.osfac.dmt.workbench.ui.plugin.analysis;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.task.TaskMonitor;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.plugin.ThreadedPlugIn;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jts.geom.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

public class ConvexHullPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private String LAYER = I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Source-Layer");
    private MultiInputDialog dialog;

    public ConvexHullPlugIn() {
    }
    private String categoryName = StandardCategoryNames.RESULT;

    public void setCategoryName(String value) {
        categoryName = value;
    }

    public void initialize(PlugInContext context) throws Exception {
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(
                this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_ANALYSIS},
                new JMenuItem(this.getName() + "..."),
                createEnableCheck(context.getWorkbenchContext()));
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    public boolean execute(PlugInContext context) throws Exception {
        //[sstein, 16.07.2006] put here again for language settings
        LAYER = I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Source-Layer");
        //Unlike ValidatePlugIn, here we always call #initDialog because we want
        //to update the layer comboboxes. [Bob Boseko]
        initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        return true;
    }

    public String getName() {
        return I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Convex-Hull-on-Layer");
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(context.getWorkbenchFrame(), I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Convex-Hull-on-Layer"), true);

        //dialog.setSideBarImage(IconLoader.icon("Overlay.gif"));
        dialog.setSideBarDescription(
                I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Creates-a-new-layer-containing-the-convex-hull-of-all-the-features-in-the-source-layer"));
        String fieldName = LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, context.getCandidateLayer(0), null, context.getLayerManager());
        GUIUtil.centreOnWindow(dialog);
    }

    public void run(TaskMonitor monitor, PlugInContext context)
            throws Exception {
        FeatureCollection a = dialog.getLayer(LAYER).getFeatureCollectionWrapper();
        FeatureCollection hullFC = convexHhull(monitor, a);

        if (hullFC == null) {
            return;
        }

        context.getLayerManager().addCategory(categoryName);
        context.addLayer(categoryName, I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Convex-Hull"), hullFC);
    }

    private FeatureCollection convexHhull(TaskMonitor monitor, FeatureCollection fc) {
        monitor.allowCancellationRequests();
        monitor.report(I18N.get("ui.plugin.analysis.ConvexHullPlugIn.Computing-Convex-Hull") + "...");

        int size = fc.size();
        GeometryFactory geomFact = null;

        if (size == 0) {
            return null;
        }
        int count = 0;
        Geometry[] geoms = new Geometry[size];

        for (Iterator i = fc.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Geometry geom = f.getGeometry();
            if (geom == null) {
                continue;
            }
            if (geomFact == null) {
                geomFact = geom.getFactory();
            }

            geoms[count++] = geom;
        }
        GeometryCollection gc = geomFact.createGeometryCollection(geoms);
        Geometry hull = gc.convexHull();
        List hullList = new ArrayList();
        hullList.add(hull);

        return FeatureDatasetFactory.createFromGeometry(hullList);
    }
}
