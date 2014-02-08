package com.osfac.dmt.workbench.ui.style;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.util.Collection;
import javax.swing.ImageIcon;

/**
 * Copies the styles for a layer to a paste buffer
 *
 * @author Martin Davis
 * @version 1.0
 */
public class CopyStylesPlugIn extends AbstractPlugIn {

    static Collection stylesBuffer = null;

    public CopyStylesPlugIn() {
    }

    public String getName() {
        return I18N.get("ui.style.CopyStylesPlugIn.copy-styles");
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("Palette_in.gif");
    }

    public static MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        final Layer layer = context.getSelectedLayer(0);
        stylesBuffer = layer.cloneStyles();
        return true;
    }
}