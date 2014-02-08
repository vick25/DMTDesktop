package com.osfac.dmt.workbench.ui.plugin;

import com.osfac.dmt.I18N;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.CategoryEvent;
import com.osfac.dmt.workbench.model.FeatureEvent;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.LayerEvent;
import com.osfac.dmt.workbench.model.LayerEventType;
import com.osfac.dmt.workbench.model.LayerListener;
import com.osfac.dmt.workbench.model.LayerManager;
import com.osfac.dmt.workbench.model.LayerManagerProxy;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.CloneableInternalFrame;
import com.osfac.dmt.workbench.ui.LayerNamePanel;
import com.osfac.dmt.workbench.ui.LayerNamePanelProxy;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelProxy;
import com.osfac.dmt.workbench.ui.OneLayerAttributeTab;
import com.osfac.dmt.workbench.ui.SelectionManager;
import com.osfac.dmt.workbench.ui.SelectionManagerProxy;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.osfac.dmt.workbench.ui.TaskFrameProxy;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.vividsolutions.jts.util.Assert;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class ViewAttributesPlugIn extends AbstractPlugIn {

    public ViewAttributesPlugIn() {
    }

    public String getName() {
        return I18N.get("ui.plugin.ViewAttributesPlugIn.view-edit-attributes");
    }

    public boolean execute(final PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        //Don't add GeometryInfoFrame because the HTML will probably be too
        // much for the editor pane (too many features). [Bob Boseko]
        final ViewAttributesFrame frame = new ViewAttributesFrame(context.getSelectedLayer(0), context);
        context.getWorkbenchFrame().addInternalFrame(frame);
        return true;
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(
                checkFactory.createTaskWindowMustBeActiveCheck()).add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));
    }

    public ImageIcon getIcon() {
        //return IconLoaderFamFam.icon("table.png");
        return IconLoader.icon("Row.gif");
    }

    public static class ViewAttributesFrame extends JInternalFrame implements
            LayerManagerProxy,
            SelectionManagerProxy,
            LayerNamePanelProxy,
            TaskFrameProxy,
            LayerViewPanelProxy {

        private LayerManager layerManager;
        private OneLayerAttributeTab attributeTab;

        public ViewAttributesFrame(final Layer layer, final PlugInContext context) {
            this.layerManager = context.getLayerManager();
            addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosed(InternalFrameEvent e) {
                    //Assume that there are no other views on the model [Jon
                    // Aquino]
                    attributeTab.getModel().dispose();
                }
            });
            setResizable(true);
            setClosable(true);
            setMaximizable(true);
            setIconifiable(true);
            getContentPane().setLayout(new BorderLayout());
            attributeTab = new OneLayerAttributeTab(context
                    .getWorkbenchContext(), ((TaskFrameProxy) context
                    .getActiveInternalFrame()).getTaskFrame(), this).setLayer(layer);
            addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameOpened(InternalFrameEvent e) {
                    attributeTab.getToolBar().updateEnabledState();
                }
            });
            getContentPane().add(attributeTab, BorderLayout.CENTER);
            setSize(500, 300);
            updateTitle(attributeTab.getLayer());
            context.getLayerManager().addLayerListener(new LayerListener() {
                public void layerChanged(LayerEvent e) {
                    if (attributeTab.getLayer() != null) {
                        updateTitle(attributeTab.getLayer());
                    }
                    // Layer REMOVE [mmichaud 2012-01-05]
                    if (e.getType() == LayerEventType.REMOVED) {
                        if (e.getLayerable() == attributeTab.getLayer()) {
                            attributeTab.getModel().dispose();
                            context.getLayerManager().removeLayerListener(this);
                            context.getWorkbenchFrame().removeInternalFrame(ViewAttributesFrame.this);
                            dispose();
                        }
                    }
                }

                public void categoryChanged(CategoryEvent e) {
                }

                public void featuresChanged(FeatureEvent e) {
                }
            });
            Assert.isTrue(!(this instanceof CloneableInternalFrame),
                    I18N.get("ui.plugin.ViewAttributesPlugIn.there-can-be-no-other-views-on-the-InfoModels"));
        }

        public LayerViewPanel getLayerViewPanel() {
            return getTaskFrame().getLayerViewPanel();
        }

        public LayerManager getLayerManager() {
            return layerManager;
        }

        private void updateTitle(Layer layer) {
            String editView;
            if (layer.isEditable()) {
                editView = I18N.get("ui.plugin.ViewAttributesPlugIn.edit");
            } else {
                editView = I18N.get("ui.plugin.ViewAttributesPlugIn.view");
            }

            setTitle(" " + I18N.get("ui.plugin.ViewAttributesPlugIn.attributes")
                    + ": " + layer.getName());
        }

        public TaskFrame getTaskFrame() {
            return attributeTab.getTaskFrame();
        }

        public SelectionManager getSelectionManager() {
            return attributeTab.getPanel().getSelectionManager();
        }

        public LayerNamePanel getLayerNamePanel() {
            return attributeTab;
        }
    }
}