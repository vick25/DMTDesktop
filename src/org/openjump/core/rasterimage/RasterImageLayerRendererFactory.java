package org.openjump.core.rasterimage;

import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.ui.renderer.Renderer;

/**
 * TODO: comment class
 *
 * @author Ole Rahn <br> <br>FH Osnabr&uuml;ck - University of Applied Sciences
 * Osnabr&uuml;ck, <br>Project: PIROL (2006), <br>Subproject: Daten- und
 * Wissensmanagement
 *
 * @version $Rev: 2434 $
 *
 */
public class RasterImageLayerRendererFactory implements Renderer.ContentDependendFactory {

    protected WorkbenchContext wbContext = null;

    public RasterImageLayerRendererFactory(WorkbenchContext wbContext) {
        super();
        this.wbContext = wbContext;
    }

    /**
     *@inheritDoc
     */
    public Renderer create(Object contentID) {
        return new RasterImageRenderer(contentID, this.wbContext.getLayerViewPanel());
    }
}
