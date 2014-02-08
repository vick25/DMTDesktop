package com.osfac.dmt.workbench.ui.cursortool.editing;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.cursortool.CoordinateListMetrics;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.cursortool.MultiClickTool;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.Icon;

public class DrawLineStringTool extends MultiClickTool {

    private FeatureDrawingUtil featureDrawingUtil;

    private DrawLineStringTool(FeatureDrawingUtil featureDrawingUtil) {
        this.featureDrawingUtil = featureDrawingUtil;
        setMetricsDisplay(new CoordinateListMetrics());
    }

    public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
        FeatureDrawingUtil featureDrawingUtil = new FeatureDrawingUtil(layerNamePanelProxy);

        return featureDrawingUtil.prepare(new DrawLineStringTool(
                featureDrawingUtil), true);
    }

    public String getName() {
        //Specify name explicitly, otherwise it will be "Draw Line String" [Bob Boseko]
        return I18N.get("ui.cursortool.editing.DrawLineString.draw-linestring");
    }

    public Icon getIcon() {
        return IconLoader.icon("DrawLineString.gif");
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        if (!checkLineString()) {
            return;
        }

//        execute(featureDrawingUtil.createAddCommand(getLineString(),
//                isRollingBackInvalidEdits(), getPanel(), this));

        featureDrawingUtil.drawLineString(
                getLineString(),
                isRollingBackInvalidEdits(),
                this,
                getPanel());
    }

    protected LineString getLineString() throws NoninvertibleTransformException {
        return new GeometryFactory().createLineString(toArray(
                getCoordinates()));
    }

    protected boolean checkLineString() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 2) {
            getPanel().getContext().warnUser(I18N.get("ui.cursortool.editing.DrawLineString.the-linestring-must-have-at-least-2-points"));

            return false;
        }

        IsValidOp isValidOp = new IsValidOp(getLineString());

        if (!isValidOp.isValid()) {
            getPanel().getContext().warnUser(isValidOp.getValidationError()
                    .getMessage());

            if (getWorkbench().getBlackboard().get(EditTransaction.ROLLING_BACK_INVALID_EDITS_KEY, false)) {
                return false;
            }
        }

        return true;
    }
}
