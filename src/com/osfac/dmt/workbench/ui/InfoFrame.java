package com.osfac.dmt.workbench.ui;

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
import com.osfac.dmt.workbench.model.Task;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.vividsolutions.jts.util.Assert;
import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

/**
 * Provides proxied (non-spatial) views of a Layer.
 */
public class InfoFrame
        extends JInternalFrame
        implements
        LayerManagerProxy,
        SelectionManagerProxy,
        LayerNamePanelProxy,
        TaskFrameProxy,
        LayerViewPanelProxy {

    public LayerManager getLayerManager() {
        return layerManager;
    }

    public TaskFrame getTaskFrame() {
        return attributeTab.getTaskFrame();
    }
    private LayerManager layerManager;
    private BorderLayout borderLayout1 = new BorderLayout();
    private AttributeTab attributeTab;
    private InfoModel model = new InfoModel();
    private GeometryInfoTab geometryInfoTab;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private WorkbenchFrame workbenchFrame;

    public InfoFrame(
            WorkbenchContext workbenchContext,
            LayerManagerProxy layerManagerProxy,
            final TaskFrame taskFrame) {
        geometryInfoTab = new GeometryInfoTab(model, workbenchContext);
        //Keep my own copy of LayerManager, because it will be nulled in TaskFrame
        //when TaskFrame closes (it may in fact already be closed, which is why
        //a LayerManagerProxy must be passed in too). But I have to 
        //remember to null it when I close. [Bob Boseko]
        Assert.isTrue(layerManagerProxy.getLayerManager() != null);
        layerManager = layerManagerProxy.getLayerManager();
        // I cannot see any reason to add this listener [mmichaud 2007-06-03]
        // See also WorkbenchFrame
        /*addInternalFrameListener(new InternalFrameAdapter() {
         public void internalFrameClosed(InternalFrameEvent e) {
         layerManager = new LayerManager();
         }
         });*/
        attributeTab = new AttributeTab(model, workbenchContext, taskFrame, this, false);
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameOpened(InternalFrameEvent e) {
                attributeTab.getToolBar().updateEnabledState();
            }
        });
        workbenchFrame = workbenchContext.getWorkbench().getFrame();
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        //This size is chosen so that when the user hits the Info tool, the window
        //fits between the lower edge of the TaskFrame and the lower edge of the
        //WorkbenchFrame. See the call to #setSize in WorkbenchFrame. [Bob Boseko]
        //Make sure there's a little space for a custom FeatureTextWriter 
        //[Bob Boseko 12/31/2003]
        this.setSize(550, 185);
        try {
            jbInit();
        } catch (Exception e) {
            JXErrorPane.showDialog(null, new ErrorInfo("Fatal error"
                    + "", e.getMessage(), null, null, e, Level.SEVERE, null));
        }
        tabbedPane.addTab("", IconLoader.icon("Table.gif"), attributeTab, "Table View");
        tabbedPane.addTab("", IconLoader.icon("Paper.gif"), geometryInfoTab, "HTML View");
        updateTitle(taskFrame.getTask().getName());
        taskFrame.getTask().add(new Task.NameListener() {
            public void taskNameChanged(String name) {
                updateTitle(taskFrame.getTask().getName());
            }
        });
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent e) {
                //Assume that there are no other views on the model
                model.dispose();
            }
        });
        layerManagerProxy.getLayerManager().addLayerListener(new LayerListener() {
            public void featuresChanged(FeatureEvent e) {
            }

            public void layerChanged(LayerEvent e) {
                // Layer REMOVE [mmichaud 2012-01-05]
                if (e.getType() == LayerEventType.REMOVED) {
                    if (getModel().getLayers().contains(e.getLayerable())) {
                        getModel().remove((Layer) e.getLayerable());
                    }
                }
            }

            public void categoryChanged(CategoryEvent e) {
            }
        });
    }

    public JPanel getAttributeTab() {
        return attributeTab;
    }

    public JPanel getGeometryTab() {
        return geometryInfoTab;
    }

    public void setSelectedTab(JPanel tab) {
        tabbedPane.setSelectedComponent(tab);
    }

    public static String title(String taskName) {
        return I18N.get("ui.InfoFrame.feature-info") + ": " + taskName;
    }

    private void updateTitle(String taskName) {
        setTitle(title(taskName));
    }

    public InfoModel getModel() {
        return model;
    }

    private void jbInit() throws Exception {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // With the DefaultInternalFrameCloser of WorkbenchFrame,
        // I think this code is no more necessary [mmichaud 2007-06-03]
        /*addInternalFrameListener(new InternalFrameAdapter() {
         public void internalFrameClosing(InternalFrameEvent e) {
         //Regardless of the defaultCloseOperation, this InfoFrame should be
         //removed from the WorkbenchFrame when the user hits X so it won't
         //appear on the Window list. [Bob Boseko]
         try {
         workbenchFrame.removeInternalFrame(InfoFrame.this);
         } catch (Exception x) {
         workbenchFrame.handleThrowable(x);
         }
         }
         });*/
        this.setTitle(I18N.get("ui.InfoFrame.feature-info"));
        this.getContentPane().setLayout(borderLayout1);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.setFocusable(false);
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void surface() {
        JInternalFrame activeFrame = workbenchFrame.getActiveInternalFrame();
        if (!workbenchFrame.hasInternalFrame(this)) {
            workbenchFrame.addInternalFrame(this, false, true);
        }
        if (activeFrame != null) {
            workbenchFrame.activateFrame(activeFrame);
        }
        moveToFront();
        //Move this frame to the front, but don't activate it if the TaskFrame is
        //active. Otherwise the user would need to re-activate the TaskFrame before
        //making another Info gesture. [Bob Boseko]
    }

    public SelectionManager getSelectionManager() {
        return attributeTab.getPanel().getSelectionManager();
    }

    public LayerNamePanel getLayerNamePanel() {
        return attributeTab;
    }

    public LayerViewPanel getLayerViewPanel() {
        return getTaskFrame().getLayerViewPanel();
    }
}
