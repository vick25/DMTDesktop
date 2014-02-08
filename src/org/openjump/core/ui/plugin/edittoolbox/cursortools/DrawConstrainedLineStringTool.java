package org.openjump.core.ui.plugin.edittoolbox.cursortools;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.ui.EditTransaction;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.cursortool.CursorTool;
import com.osfac.dmt.workbench.ui.cursortool.editing.FeatureDrawingUtil;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DrawConstrainedLineStringTool extends ConstrainedMultiClickTool {

    private FeatureDrawingUtil featureDrawingUtil;
    final static String drawConstrainedLineString = I18N.get("org.openjump.core.ui.plugin.edittoolbox.cursortools.DrawConstrainedLineStringTool.Draw-Constrained-LineString");
    final static String TheLinestringMustHaveAtLeast2Points = I18N.get("org.openjump.core.ui.plugin.edittoolbox.cursortools.DrawConstrainedLineStringTool.The-linestring-must-have-at-least-2-points");

    protected DrawConstrainedLineStringTool(FeatureDrawingUtil featureDrawingUtil) {
        drawClosed = false;
        this.featureDrawingUtil = featureDrawingUtil;
    }

    public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
        FeatureDrawingUtil featureDrawingUtil = new FeatureDrawingUtil(layerNamePanelProxy);

        return featureDrawingUtil.prepare(new DrawConstrainedLineStringTool(
                featureDrawingUtil), true);
    }

    public String getName() {
        //Specify name explicitly, otherwise it will be "Draw Line String" [Bob Boseko]
        return drawConstrainedLineString;
    }

    public Icon getIcon() {
        return new ImageIcon(getClass().getResource("DrawLinestringConstrained.gif"));
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        if (!checkLineString()) {
            return;
        }

        execute(featureDrawingUtil.createAddCommand(getLineString(),
                isRollingBackInvalidEdits(), getPanel(), this));
    }

    protected LineString getLineString() throws NoninvertibleTransformException {
        return new GeometryFactory().createLineString(toArray(
                getCoordinates()));
    }

    protected boolean checkLineString() throws NoninvertibleTransformException {
        if (getCoordinates().size() < 2) {
            getPanel().getContext().warnUser(TheLinestringMustHaveAtLeast2Points);

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
