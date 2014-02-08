package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.WorkbenchException;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.EnterWKTDialog;
import com.vividsolutions.jts.io.WKTWriter;
import java.util.Arrays;
import javax.swing.ImageIcon;
import org.openjump.core.ui.images.IconLoader;

public class EditSelectedFeaturePlugIn extends WKTPlugIn {

    private WKTDisplayHelper helper = new WKTDisplayHelper();
    public static ImageIcon ICON = IconLoader.icon("view_edit_geometry.png");
    private Feature feature;

    public EditSelectedFeaturePlugIn() {
    }

    protected Layer layer(PlugInContext context) {
        return (Layer) context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems().iterator().next();
    }

    public String getName() {
        return I18N.get("ui.plugin.EditSelectedFeaturePlugIn.view-edit-selected-feature");
    }

    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNFeaturesMustHaveSelectedItemsCheck(1));
    }

    public boolean execute(PlugInContext context) throws Exception {
        return execute(
                context,
                (Feature) context
                .getLayerViewPanel()
                .getSelectionManager()
                .getFeaturesWithSelectedItems()
                .iterator()
                .next(),
                true);
    }

    public boolean execute(PlugInContext context, Feature feature, boolean editable)
            throws Exception {
        this.feature = feature;
        reportNothingToUndoYet(context);
        return super.execute(context);
    }

    protected void apply(String wkt, PlugInContext context) throws Exception {
        if (!layer(context).isEditable()) {
            return;
        }
        super.apply(wkt, context);
    }

    protected void apply(FeatureCollection c, PlugInContext context) throws WorkbenchException {
        if (c.size() != 1) {
            throw new WorkbenchException(I18N.get("ui.plugin.EditSelectedFeaturePlugIn.expected-1-feature-but-found") + " " + c.size());
        }
        EditTransaction transaction =
                new EditTransaction(
                Arrays.asList(new Feature[]{feature}),
                getName(),
                layer,
                isRollingBackInvalidEdits(context),
                false,
                context.getWorkbenchFrame());
        //Can't simply pass the LayerViewPanel to the transaction because if there is
        //an attribute viewer up and its TaskFrame has been closed, the LayerViewPanel's
        //LayerManager will be null. [Bob Boseko]
        Feature newFeature = (Feature) c.iterator().next();
        transaction.setGeometry(feature, newFeature.getGeometry());
        transaction.commit();
    }

    protected EnterWKTDialog createDialog(PlugInContext context) {
        EnterWKTDialog d = super.createDialog(context);
        d.setTitle(
                (layer(context).isEditable() ? I18N.get("ui.plugin.EditSelectedFeaturePlugIn.edit") + " " : "")
                + I18N.get("ui.plugin.EditSelectedFeaturePlugIn.feature") + " "
                + feature.getID() + " " + I18N.get("ui.plugin.EditSelectedFeaturePlugIn.in") + " "
                + layer
                + (layer(context).isEditable() ? "" : " (" + I18N.get("ui.plugin.EditSelectedFeaturePlugIn.layer-is-uneditable") + ")"));
        d.setEditable(layer(context).isEditable());
        d.setText(helper.format(new WKTWriter(3).write(feature.getGeometry())));
        return d;
    }

    public ImageIcon getIcon() {
        return ICON;
    }
}