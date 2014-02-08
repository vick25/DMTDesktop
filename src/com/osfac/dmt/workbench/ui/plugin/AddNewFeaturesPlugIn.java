package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.feature.FeatureUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.UndoableCommand;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EnterWKTDialog;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class AddNewFeaturesPlugIn extends WKTPlugIn {

    public AddNewFeaturesPlugIn() {
    }

    protected Layer layer(PlugInContext context) {
        return context.getLayerNamePanel().chooseEditableLayer();
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        return super.execute(context);
    }

    protected void apply(FeatureCollection c, final PlugInContext context) {
        //Can't use WeakHashMap, otherwise the features will vanish when the command
        //is undone! [Bob Boseko]
        final ArrayList features = new ArrayList();
        FeatureSchema fs = this.layer.getFeatureCollectionWrapper().getFeatureSchema();
        for (Iterator i = c.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            features.add(FeatureUtil.toFeature(feature.getGeometry(), fs));
        }
        execute(new UndoableCommand(getName()) {
            public void execute() {
                layer.getFeatureCollectionWrapper().addAll(features);
            }

            public void unexecute() {
                layer.getFeatureCollectionWrapper().removeAll(features);
            }
        }, context);
    }

    protected EnterWKTDialog createDialog(PlugInContext context) {
        EnterWKTDialog d = super.createDialog(context);
        d.setTitle(I18N.get("ui.plugin.AddNewFeaturesPlugIn.add-features-to") + " " + layer);
        d.setDescription("<HTML>" + I18N.get("ui.plugin.AddNewFeaturesPlugIn.enter-well-known-text-for-one-or-more-geometries") + "</HTML>");
        return d;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyOneSelectedLayerMustBeEditableCheck());
    }
    public static final ImageIcon ICON = IconLoader.icon("add.png");
}
