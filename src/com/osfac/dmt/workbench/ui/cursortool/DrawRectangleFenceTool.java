package com.osfac.dmt.workbench.ui.cursortool;

import com.osfac.dmt.workbench.model.FenceLayerFinder;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.Icon;

public class DrawRectangleFenceTool extends RectangleTool {

    public final static Color COLOR = Color.black;

    public DrawRectangleFenceTool() {
        setColor(COLOR);
    }

    public Icon getIcon() {
        //return IconLoaderFamFam.icon("shape_square_edit.png");
        return IconLoader.icon("Box.gif");
    }

    public Cursor getCursor() {
        return createCursor(IconLoader.icon("FenceCursor.gif").getImage());
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        //Don't want viewport to change at this stage. [Bob Boseko]
        getPanel().setViewportInitialized(true);

        FenceLayerFinder fenceLayerFinder = new FenceLayerFinder(getPanel());
        fenceLayerFinder.setFence(getRectangle());

        if (!fenceLayerFinder.getLayer().isVisible()) {
            fenceLayerFinder.getLayer().setVisible(true);
        }
    }
}
