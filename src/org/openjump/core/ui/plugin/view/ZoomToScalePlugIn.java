package org.openjump.core.ui.plugin.view;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.MultiInputDialog;
import com.osfac.dmt.workbench.ui.Viewport;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.openjump.core.ui.util.ScreenScale;

/**
 * Zooms to a given map scale, received from a input dialog
 *
 * @author sstein
 */
public class ZoomToScalePlugIn extends AbstractPlugIn {

    private String T1 = "scale"; //[sstein] this string is not used anymore
    int scale = 0;
    double oldHorizontalScale = 0; // is calculated for panel-width (not heigth!!)
    double modelWidth = 0;
    double panelWidth = 0;
    String text = I18N.get("org.openjump.core.ui.plugin.view.ZoomToScalePlugIn.set-new-scale-to-zoom") + ":  1 : ";

    public void initialize(PlugInContext context) throws Exception {

        this.T1 = I18N.get("org.openjump.core.ui.plugin.view.ZoomToScalePlugIn.scale") + ": ";
        context.getFeatureInstaller().addMainMenuItemWithJava14Fix(this,
                new String[]{MenuNames.VIEW},
                I18N.get("org.openjump.core.ui.plugin.view.ZoomToScalePlugIn.zoom-to-scale") + "{pos:9}",
                false,
                null,
                createEnableCheck(context.getWorkbenchContext()));
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
    }

    public boolean execute(PlugInContext context) throws Exception {

        Viewport port = context.getLayerViewPanel().getViewport();
        this.oldHorizontalScale = ScreenScale.getHorizontalMapScale(port);

        MultiInputDialog dialog = new MultiInputDialog(
                context.getWorkbenchFrame(),
                I18N.get("org.openjump.core.ui.plugin.view.ZoomToScalePlugIn.zoom-to-scale"),
                true);
        setDialogValues(dialog, context);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()) {
            return false;
        }
        getDialogValues(dialog);
        //-- get zoom factor
        double factor = this.scale / this.oldHorizontalScale;
        //--calculating new screen using the envelope of the corner LineString 
        Envelope oldEnvelope = port.getEnvelopeInModelCoordinates();
        double xc = 0.5 * (oldEnvelope.getMaxX() + oldEnvelope.getMinX());
        double yc = 0.5 * (oldEnvelope.getMaxY() + oldEnvelope.getMinY());
        double xmin = xc - 1 / 2.0 * factor * oldEnvelope.getWidth();
        double xmax = xc + 1 / 2.0 * factor * oldEnvelope.getWidth();
        double ymin = yc - 1 / 2.0 * factor * oldEnvelope.getHeight();
        double ymax = yc + 1 / 2.0 * factor * oldEnvelope.getHeight();
        Coordinate[] coords = new Coordinate[]{new Coordinate(xmin, ymin),
            new Coordinate(xmax, ymax)};
        Geometry g1 = new GeometryFactory().createLineString(coords);
        port.zoom(g1.getEnvelopeInternal());
        return true;
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        //dialog.addLabel("actual scale in horizontal direction: " + (int)this.oldHorizontalScale);
        dialog.addLabel(I18N.get("org.openjump.core.ui.plugin.view.ZoomToScalePlugIn.actual-scale-in-horizontal-direction") + " 1 : " + (int) this.oldHorizontalScale);
        //dialog.addLabel("set new scale to zoom:");
        dialog.addIntegerField(text, 25000, 7, text);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        this.scale = dialog.getInteger(text);
    }
}
