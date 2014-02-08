package org.openjump.core.ui.plugin.layer.pirolraster;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import org.openjump.core.apitools.LayerTools;
import org.openjump.core.rasterimage.RasterImageLayer;

/**
 * TODO: comment class
 *
 * @author Ole Rahn <br> <br>FH Osnabr&uuml;ck - University of Applied Sciences
 * Osnabr&uuml;ck, <br>Project: PIROL (2006), <br>Subproject: Daten- und
 * Wissensmanagement
 *
 * @version $Rev: 2509 $ [sstein] - 22.Feb.2009 - modified to work in OpenJUMP
 */
public class ZoomToRasterImagePlugIn extends AbstractPlugIn {

    public ZoomToRasterImagePlugIn() {
        //super(new PersonalLogger(DebugUserIds.OLE));
    }

    /**
     *@inheritDoc
     */
    public String getIconString() {
        return null;
    }

    /**
     * @inheritDoc
     */
    public String getName() {
        return I18N.get("org.openjump.core.ui.plugin.layer.pirolraster.ZoomToRasterImagePlugIn.Zoom-To-Raster-Image");
    }

    /**
     *@inheritDoc
     */
    public boolean execute(PlugInContext context) throws Exception {

        RasterImageLayer rLayer = (RasterImageLayer) LayerTools.getSelectedLayerable(context, RasterImageLayer.class);

        if (rLayer == null) {
            context.getWorkbenchFrame().warnUser(I18N.get("pirol.plugIns.EditAttributeByFormulaPlugIn.no-layer-selected"));
            return false;
        }

        context.getLayerViewPanel().getViewport().zoom(rLayer.getEnvelope());

        return true;
    }
}
