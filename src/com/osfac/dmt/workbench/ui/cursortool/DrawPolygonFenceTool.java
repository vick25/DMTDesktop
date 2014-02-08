package com.osfac.dmt.workbench.ui.cursortool;

import java.awt.Cursor;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.Icon;
import com.vividsolutions.jts.geom.Polygon;
import com.osfac.dmt.workbench.model.FenceLayerFinder;
import com.osfac.dmt.workbench.ui.images.IconLoader;

public class DrawPolygonFenceTool extends PolygonTool {

    public DrawPolygonFenceTool() {
        setColor(DrawRectangleFenceTool.COLOR);
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("FenceCursor.gif").getImage());
    }

    public Icon getIcon() {
        return IconLoader.icon("Box.gif");
    }

    private boolean doubleClicked() {
        return getCoordinates().size() == 1;
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        reportNothingToUndoYet();

        Polygon fence;

        if (doubleClicked()) {
            fence = null;
        } else {
            if (!checkPolygon()) {
                return;
            }

            //Don't want viewport to change at this stage. [Bob Boseko]
            getPanel().setViewportInitialized(true);

            fence = getPolygon();
        }

        FenceLayerFinder fenceLayerFinder = new FenceLayerFinder(getPanel());
        fenceLayerFinder.setFence(fence);

        if (!fenceLayerFinder.getLayer().isVisible()) {
            fenceLayerFinder.getLayer().setVisible(true);
        }
    }
}
