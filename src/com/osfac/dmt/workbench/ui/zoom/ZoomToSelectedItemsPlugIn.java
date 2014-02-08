package com.osfac.dmt.workbench.ui.zoom;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.util.Assert;
import com.osfac.dmt.geom.CoordUtil;
import com.osfac.dmt.geom.EnvelopeUtil;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.GUIUtil;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.renderer.ThreadQueue;
import java.awt.geom.NoninvertibleTransformException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;

/**
 * Zoom to the features, then flash them.
 */
public class ZoomToSelectedItemsPlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        zoom(
                context.getLayerViewPanel().getSelectionManager().getSelectedItems(),
                context.getLayerViewPanel());
        return true;
    }

    public void zoom(final Collection geometries, final LayerViewPanel panel)
            throws NoninvertibleTransformException {
        if (envelope(geometries).isNull()) {
            return;
        }
        Envelope proposedEnvelope =
                EnvelopeUtil.bufferByFraction(
                envelope(geometries),
                zoomBufferAsExtentFraction(geometries));

        if ((proposedEnvelope.getWidth()
                > panel.getLayerManager().getEnvelopeOfAllLayers().getWidth())
                || (proposedEnvelope.getHeight()
                > panel.getLayerManager().getEnvelopeOfAllLayers().getHeight())) {
            //We've zoomed out farther than we would if we zoomed to all layers.
            //This is too far. Set scale to that of zooming to all layers,
            //and center on the selected features. [Bob Boseko]
            proposedEnvelope = panel.getViewport().fullExtent();
            EnvelopeUtil.translate(
                    proposedEnvelope,
                    CoordUtil.subtract(
                    EnvelopeUtil.centre(envelope(geometries)),
                    EnvelopeUtil.centre(proposedEnvelope)));
        }

        panel.getViewport().zoom(proposedEnvelope);
        //Wait until the zoom is complete before executing the flash. [Bob Boseko]
        ThreadQueue.Listener listener = new ThreadQueue.Listener() {
            public void allRunningThreadsFinished() {
                panel.getRenderingManager().getDefaultRendererThreadQueue().remove(this);
                try {
                    GUIUtil.invokeOnEventThread(new Runnable() {
                        public void run() {
                            try {
                                flash(geometries, panel);
                            } catch (NoninvertibleTransformException e) {
                            }
                        }
                    });
                } catch (InterruptedException | InvocationTargetException e) {
                }
            }
        };
        panel.getRenderingManager().getDefaultRendererThreadQueue().add(listener);

    }

    private Envelope envelope(Collection geometries) {
        Envelope envelope = new Envelope();

        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            envelope.expandToInclude(geometry.getEnvelopeInternal());
        }

        return envelope;
    }

    private double zoomBufferAsExtentFraction(Collection geometries) {
        //Easiest to express zoomBuffer as a multiple of the average extent of
        //the individual features, rather than a multiple of the average extent
        //of the features combined. For example, 2 * the average extent of the
        //features combined can be a huge zoomBuffer if the features are far
        //apart. But if you consider the average extent of the individual features,
        //you don't need to think about how far apart the features are. [Bob Boseko]
        double zoomBuffer = 2 * averageExtent(geometries);
        double averageFullExtent = averageFullExtent(geometries);

        if (averageFullExtent == 0) {
            //Point feature. Just return 0. Rely on EnvelopeUtil#buffer to choose
            //a reasonable buffer for point features. [Bob Boseko]
            return 0;
        }

        return zoomBuffer / averageFullExtent;
    }

    private double averageExtent(Collection geometries) {
        Assert.isTrue(!geometries.isEmpty());

        double extentSum = 0;

        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            extentSum += geometry.getEnvelopeInternal().getWidth();
            extentSum += geometry.getEnvelopeInternal().getHeight();
        }

        return extentSum / (2d * geometries.size());
        //2 because width and height [Bob Boseko]
    }

    private double averageFullExtent(Collection geometries) {
        Envelope envelope = envelope(geometries);

        return (envelope.getWidth() + envelope.getHeight()) / 2d;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
                .add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1));
    }

    public void flash(Collection geometries, final LayerViewPanel panel)
            throws NoninvertibleTransformException {
        final GeometryCollection gc = toGeometryCollection(geometries);

        if (!panel
                .getViewport()
                .getEnvelopeInModelCoordinates()
                .intersects(gc.getEnvelopeInternal())) {
            return;
        }

        panel.flash(gc);
    }

    private GeometryCollection toGeometryCollection(Collection geometries) {
        return new GeometryFactory().createGeometryCollection(
                (Geometry[]) geometries.toArray(new Geometry[]{}));
    }

    public ImageIcon getIcon() {
        //return IconLoaderFamFam.icon("ZoomSelected_small.png");
        return IconLoader.icon("ZoomSelected.gif");
    }
}
