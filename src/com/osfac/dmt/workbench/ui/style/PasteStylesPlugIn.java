package com.osfac.dmt.workbench.ui.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.renderer.style.ColorThemingStyle;
import com.osfac.dmt.workbench.ui.renderer.style.LabelStyle;
import com.osfac.dmt.workbench.ui.renderer.style.Style;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

/**
 * Pastes the styles from the internal style paste buffer to a layer
 *
 * @author Martin Davis
 * @version 1.0
 */
public class PasteStylesPlugIn extends AbstractPlugIn {

    public static MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    public PasteStylesPlugIn() {
    }

    public String getName() {
        return I18N.get("ui.style.PasteStylesPlugIn.paste-styles");
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("Palette_out.gif");
    }

    public boolean execute(PlugInContext context) throws Exception {
        if (CopyStylesPlugIn.stylesBuffer == null) {
            return false;
        }
        Layer[] selectedLayers = context.getSelectedLayers();
        for (int i = 0; i < selectedLayers.length; i++) {
            validateStyleForLayer(selectedLayers[i]); //throws exception if bad
            pasteStyles(selectedLayers[i]);
        }
        return true;
    }

    private void pasteStyles(Layer layer) {
        layer.setStyles(CopyStylesPlugIn.stylesBuffer);
    }

    private void validateStyleForLayer(Layer layer) {
        String attribName = "";
        try {
            FeatureSchema featureSchema = layer.getFeatureCollectionWrapper().getFeatureSchema();
            ArrayList<String> attribList = new ArrayList<String>();
            for (Iterator i = CopyStylesPlugIn.stylesBuffer.iterator(); i.hasNext();) {
                Style style = (Style) i.next();
                if (style instanceof LabelStyle) {
                    LabelStyle labelStyle = (LabelStyle) style;
                    if (labelStyle.isEnabled()) {
                        attribName = labelStyle.getAttribute();
                        if (attribName != "") {
                            attribList.add(attribName);
                        }
                        attribName = labelStyle.getAngleAttribute();
                        if (attribName != "") {
                            attribList.add(attribName);
                        }
                        attribName = labelStyle.getHeightAttribute();
                        if (attribName != "") {
                            attribList.add(attribName);
                        }
                    }
                } else if (style instanceof ColorThemingStyle) {
                    ColorThemingStyle ctStyle = (ColorThemingStyle) style;
                    if (ctStyle.isEnabled()) {
                        attribName = ctStyle.getAttributeName();
                        if (attribName != "") {
                            attribList.add(attribName);
                        }
                    }
                }
            }
            for (Iterator i = attribList.iterator(); i.hasNext();) {
                attribName = (String) i.next();
                //check for attribute in Layer's FeatureSchema and throw exception now if not found.
                featureSchema.getAttributeIndex(attribName);
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Attribute " + attribName
                    + " not found on Layer: " + layer.getName());
        }
    }
}