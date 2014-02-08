package org.openjump.core.ccordsys.srid;

import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.util.Block;
import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.WorkbenchFrame;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JInternalFrame;

/**
 * Adds the SRIDStyle to every layer that JUMP encounters.
 */
public class EnsureAllLayersHaveSRIDStylePlugIn extends AbstractPlugIn {

    public void initialize(PlugInContext context) throws Exception {
        initializeCurrentAndFutureInternalFrames(context.getWorkbenchFrame(),
                new Block() {
                    private Collection initializedFrames = new ArrayList();

                    public Object yield(Object internalFrame) {
                        if (!initializedFrames.contains(internalFrame)) {
                            initialize((JInternalFrame) internalFrame);
                            initializedFrames.add(internalFrame);
                        }
                        return null;
                    }
                });
    }

    private void initialize(JInternalFrame internalFrame) {
        if (!(internalFrame instanceof LayerManagerProxy)) {
            return;
        }
        initialize(((LayerManagerProxy) internalFrame).getLayerManager());
    }

    private void initialize(LayerManager layerManager) {
        for (Iterator i = layerManager.iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            ensureHasSRIDStyle(layer);
        }
        layerManager.addLayerListener(new LayerListener() {
            public void featuresChanged(FeatureEvent e) {
            }

            public void layerChanged(LayerEvent e) {
                if (e.getLayerable() instanceof Layer) {
                    ensureHasSRIDStyle((Layer) e.getLayerable());
                }
            }

            public void categoryChanged(CategoryEvent e) {
            }
        });
    }

    private void ensureHasSRIDStyle(Layer layer) {
        if (layer.getStyle(SRIDStyle.class) != null) {
            return;
        }
        SRIDStyle sridStyle = new SRIDStyle();
        if (layer.getFeatureCollectionWrapper().size() > 0) {
            sridStyle.setSRID(((Feature) layer.getFeatureCollectionWrapper()
                    .iterator().next()).getGeometry().getSRID());
        }
        layer.addStyle(sridStyle);
    }

    private void initializeCurrentAndFutureInternalFrames(
            WorkbenchFrame workbenchFrame, final Block block) {
        workbenchFrame.getDesktopPane().addContainerListener(
                new ContainerAdapter() {
                    public void componentAdded(ContainerEvent e) {
                        if (!(e.getChild() instanceof JInternalFrame)) {
                            return;
                        }
                        block.yield((JInternalFrame) e.getChild());
                    }
                });
        for (Iterator i = Arrays.asList(workbenchFrame.getInternalFrames())
                .iterator(); i.hasNext();) {
            JInternalFrame internalFrame = (JInternalFrame) i.next();
            block.yield(internalFrame);
        }
    }
}
