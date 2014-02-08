package com.osfac.dmt.workbench.ui.plugin.test;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureDatasetFactory;
import com.osfac.dmt.geom.GeometryMicroscope;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.vividsolutions.jts.geom.Envelope;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MicroscopePlugIn extends AbstractPlugIn {

    public MicroscopePlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        EnableCheckFactory checkFactory =
                new EnableCheckFactory(context.getWorkbenchContext());
        context
                .getFeatureInstaller()
                .addMainMenuItemWithJava14Fix(
                this,
                new String[]{MenuNames.TOOLS, "Test"},
                getName(),
                false,
                null,
                new MultiEnableCheck()
                .add(
                checkFactory
                .createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1))
                .add(
                checkFactory
                .createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createFenceMustBeDrawnCheck()));
    }

    public boolean execute(PlugInContext context) throws Exception {
        FeatureCollection fc = context.getSelectedLayer(0).getFeatureCollectionWrapper();
        Envelope fence = context.getLayerViewPanel().getFence().getEnvelopeInternal();
        FeatureCollection magFC = magnify(fc, fence);
        Layer lyr = context.addLayer(StandardCategoryNames.QA, "Microscope", magFC);
        lyr.getBasicStyle().setFillColor(Color.red);
        lyr.getBasicStyle().setLineColor(Color.red);
        lyr.getBasicStyle().setAlpha(100);
        lyr.getVertexStyle().setEnabled(true);
        lyr.fireAppearanceChanged();
        return true;
    }

    private FeatureCollection magnify(FeatureCollection fc, Envelope env) {
        List geomList = new ArrayList();
        for (Iterator i = fc.query(env).iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            geomList.add(feature.getGeometry().clone());
        }
        //double minSep = 5.0;
        double minSep = env.getWidth() / 20; // kluge
        GeometryMicroscope micro = new GeometryMicroscope(geomList, env, minSep);
        List result = micro.getAdjusted();
        return FeatureDatasetFactory.createFromGeometry(result);
    }
}
