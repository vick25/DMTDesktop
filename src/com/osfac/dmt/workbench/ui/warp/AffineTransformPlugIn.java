package com.osfac.dmt.workbench.ui.warp;

import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.warp.AffineTransform;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.util.Assert;
import java.util.Iterator;

/**
 * Applies an affine transform to the selected layers. The affine transform is
 * specified using three vectors drawn by the user.
 */
public class AffineTransformPlugIn extends AbstractPlugIn {

    public AffineTransformPlugIn() {
    }

    public static EnableCheck getEnableCheck(EnableCheckFactory checkFactory) {
        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
                .add(checkFactory
                .createWindowWithLayerNamePanelMustBeActiveCheck())
                .add(checkFactory
                .createExactlyNLayersMustBeSelectedCheck(1))
                .add(checkFactory
                .createBetweenNAndMVectorsMustBeDrawnCheck(1, 3));
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItem(this,
                new String[]{MenuNames.TOOLS, MenuNames.TOOLS_WARP}, getName(), false, null,
                getEnableCheck(context.getCheckFactory()));
    }

    public boolean execute(PlugInContext context) throws Exception {
        AffineTransform transform = affineTransform(context);
        FeatureCollection featureCollection = transform.transform(context.getSelectedLayer(
                0).getFeatureCollectionWrapper());
        context.getLayerManager().addLayer(StandardCategoryNames.WORKING,
                I18N.get("ui.warp.AffineTransformPlugIn.affined") + " " + context.getSelectedLayer(0).getName(),
                featureCollection);
        checkValid(featureCollection, context);

        return true;
    }

    public static void checkValid(FeatureCollection featureCollection,
            PlugInContext context) {
        for (Iterator i = featureCollection.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();

            if (!feature.getGeometry().isValid()) {
                context.getLayerViewPanel().getContext().warnUser(I18N.get("ui.warp.AffineTransformPlugIn.some-geometries-are-not-valid"));

                return;
            }
        }
    }

    /**
     * @return either the tip or the tail coordinate of the nth vector
     */
    private Coordinate vectorCoordinate(int n, boolean tip,
            PlugInContext context, WarpingVectorLayerFinder vectorLayerManager) {
        LineString vector = (LineString) vectorLayerManager.getVectors().get(n);

        return tip ? vector.getCoordinateN(1) : vector.getCoordinateN(0);
    }

    private AffineTransform affineTransform(PlugInContext context) {
        WarpingVectorLayerFinder vlm = new WarpingVectorLayerFinder(context);

        switch (vlm.getVectors().size()) {
            case 1:
                return new AffineTransform(vectorCoordinate(0, false, context, vlm),
                        vectorCoordinate(0, true, context, vlm));

            case 2:
                return new AffineTransform(vectorCoordinate(0, false, context, vlm),
                        vectorCoordinate(0, true, context, vlm),
                        vectorCoordinate(1, false, context, vlm),
                        vectorCoordinate(1, true, context, vlm));

            case 3:
                return new AffineTransform(vectorCoordinate(0, false, context, vlm),
                        vectorCoordinate(0, true, context, vlm),
                        vectorCoordinate(1, false, context, vlm),
                        vectorCoordinate(1, true, context, vlm),
                        vectorCoordinate(2, false, context, vlm),
                        vectorCoordinate(2, true, context, vlm));
        }

        Assert.shouldNeverReachHere();

        return null;
    }
}
