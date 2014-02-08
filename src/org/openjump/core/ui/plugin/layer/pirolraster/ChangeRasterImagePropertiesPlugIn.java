package org.openjump.core.ui.plugin.layer.pirolraster;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import org.openjump.core.apitools.LayerTools;
import org.openjump.core.rasterimage.RasterImageLayer;

/**
 *
 * PlugIn that adds a menu item to the layer context menu, that enables changing
 * RasterImage layer transparencies add any time.
 *
 * @author Ole Rahn <br> <br>FH Osnabr&uuml;ck - University of Applied Sciences
 * Osnabr&uuml;ck, <br>Project: PIROL (2005), <br>Subproject: Daten- und
 * Wissensmanagement
 *
 * @version $Rev: 2509 $ [sstein] - 22.Feb.2009 - modified to work in OpenJUMP
 */
public class ChangeRasterImagePropertiesPlugIn extends AbstractPlugIn {

    public ChangeRasterImagePropertiesPlugIn() {
        //super(new PersonalLogger(DebugUserIds.OLE));
    }

    /**
     * @inheritDoc
     */
    public String getIconString() {
        return null;
    }

    /**
     * @inheritDoc
     */
    public String getName() {
        return I18N.get("org.openjump.core.ui.plugin.layer.pirolraster.ChangeRasterImagePropertiesPlugIn.Change-Raster-Image-Properties");
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {
        RasterImageLayer rlayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);

        if (rlayer == null) {
            context.getWorkbenchFrame().warnUser(I18N.get("pirol.plugIns.EditAttributeByFormulaPlugIn.no-layer-selected"));
            return false;
        }

        ChangeRasterImageStyleDialog dialog = new ChangeRasterImageStyleDialog(rlayer, context.getWorkbenchFrame(), this.getName(), true);

        dialog.setVisible(true);

        if (!dialog.wasOkClicked()) {
            return false;
        }

        rlayer.fireAppearanceChanged();

        return true;
    }

    public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
                .add(checkFactory.createExactlyNLayerablesMustBeSelectedCheck(1, RasterImageLayer.class));
    }

    public void initialize(PlugInContext context) throws Exception {
    }
}
