package org.openjump.core.ui.plugin.view;

import com.osfac.dmt.I18N;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.LayerNamePanelListener;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.LayerViewPanelListener;
import com.osfac.dmt.workbench.ui.TaskFrame;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Collection;
import java.util.Iterator;

public class ShowFullPathPlugIn extends AbstractPlugIn {

    PlugInContext gContext;
    final static String sErrorSeeOutputWindow = I18N.get("org.openjump.core.ui.plugin.view.ShowFullPathPlugIn.Error-See-Output-Window");
    final static String sNumberSelected = I18N.get("org.openjump.core.ui.plugin.view.ShowFullPathPlugIn.NumberSelected");
    //-- added by sstein for test reasons
//	private LayerListener myLayerListener = new LayerListener() {
//        public void categoryChanged(CategoryEvent e) {}
//        public void featuresChanged(FeatureEvent e)  {}        
//		public void layerChanged(LayerEvent e) {
//        if (e.getType() == LayerEventType.ADDED || e.getType() == LayerEventType.REMOVED) {
//            Collection layerCollection = (Collection) gContext.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
//            for (Iterator i = layerCollection.iterator(); i.hasNext();)
//            {
//                Layer layer = (Layer) i.next();
//                if (layer.hasReadableDataSource())
//                {
//                    DataSourceQuery dsq = layer.getDataSourceQuery();
//                    try{
//                    	String fname = dsq.getDataSource().getProperties().get("File").toString();                   	
//                    	layer.setDescription(fname);
//                    }
//                    catch(Exception e){
//                    	System.out.println("seems to be a database dataset" + e);
//                    }                }
//            	}   
//        	}
//		}
//	};
    private LayerNamePanelListener layerNamePanelListener =
            new LayerNamePanelListener() {
                public void layerSelectionChanged() {
                    Collection layerCollection = (Collection) gContext.getWorkbenchContext().getLayerNamePanel().getLayerManager().getLayers();
                    for (Iterator i = layerCollection.iterator(); i.hasNext();) {
                        Layer layer = (Layer) i.next();
                        if (layer.hasReadableDataSource()) {
                            DataSourceQuery dsq = layer.getDataSourceQuery();
                            String fname = "";
                            Object fnameObj = dsq.getDataSource().getProperties().get("File");
                            if (fnameObj != null) {
                                fname = fnameObj.toString();
                            }
                            //layer.setDescription(fname);
                            Object archiveObj = layer.getBlackboard().get("ArchiveFileName");
                            //if (archiveObj != null) layer.setDescription(archiveObj.toString());
                        }
                    }
                }
            };
    private LayerViewPanelListener layerViewPanelListener =
            new LayerViewPanelListener() {
                public void selectionChanged() {
                    LayerViewPanel panel = gContext.getWorkbenchContext().getLayerViewPanel();
                    if (panel == null) {
                        return;
                    } //[Bob Boseko 2005-08-04]
                    Collection selectedFeatures = panel.getSelectionManager().getSelectedItems();
                    int numSel = selectedFeatures.size();
                    int numPts = 0;
                    for (Iterator i = selectedFeatures.iterator(); i.hasNext();) {
                        numPts += ((Geometry) i.next()).getNumPoints();
                    }
                    //LDB added the following to simulate 4D Draw Coordinates Panel
                    Envelope env = envelope(panel.getSelectionManager().getSelectedItems());
                    String sx = panel.format(env.getWidth());
                    String sy = panel.format(env.getHeight());
                    //gContext.getWorkbenchFrame().setTimeMessage(sNumberSelected + " " + numSel);
                    gContext.getWorkbenchFrame().setTimeMessage(sNumberSelected + " " + numSel + " [" + sx + ", " + sy + "] " + numPts + " pts");
                }

                public void cursorPositionChanged(String x, String y) {
                }

                public void painted(Graphics graphics) {
                }
            };

    public void initialize(PlugInContext context) throws Exception {
        gContext = context;
//    	/***	added by sstein ***********************/
//	    //
//        // Whenever anything happens on an internal frame we want to do this.
//        //
//	    GUIUtil.addInternalFrameListener(
//	            context.getWorkbenchFrame().getDesktopPane(),
//	            GUIUtil.toInternalFrameListener(new ActionListener() {
//	        public void actionPerformed(ActionEvent e) {
//	            installListenersOnCurrentPanel();
//	        }
//	    }));
        /**
         * ** original ********************************
         */
        context.getWorkbenchFrame().getDesktopPane().addContainerListener(
                new ContainerListener() {
                    public void componentAdded(ContainerEvent e) {
                        Component child = e.getChild();
                        if (child.getClass().getName().equals("com.osfac.dmt.workbench.ui.TaskFrame")) {
                            ((TaskFrame) child).getLayerNamePanel().addListener(layerNamePanelListener);
                            ((TaskFrame) child).getLayerViewPanel().addListener(layerViewPanelListener);
                        }
                    }

                    public void componentRemoved(ContainerEvent e) {
                        Component child = e.getChild();
                        if (child.getClass().getName().equals("com.osfac.dmt.workbench.ui.TaskFrame")) {
                            ((TaskFrame) child).getLayerNamePanel().removeListener(layerNamePanelListener);
                            ((TaskFrame) child).getLayerViewPanel().removeListener(layerViewPanelListener);
                        }
                    }
                });
    }

//    //-- method by sstein adapted from Zoombar
//    private void installListenersOnCurrentPanel(){
//    	 System.out.println("try to install listener");
//        String LAYER_PATH_LISTENERS_INSTALLED_KEY =
//            Integer.toHexString(hashCode()) + " - LAYER PATH LISTENERS INSTALLED";
//        if (viewBlackboard().get(LAYER_PATH_LISTENERS_INSTALLED_KEY) != null) {
//            return;
//        }
//    	if(gContext.getLayerViewPanel() == null){
//    		return;
//    	}
//        //[sstein]        
//        LayerManager lm = gContext.getLayerManager();
//        lm.addLayerListener(this.myLayerListener);
//        System.out.println("listener installed");
//        viewBlackboard().put(LAYER_PATH_LISTENERS_INSTALLED_KEY, new Object());
//    }
//    
//    //-- method by sstein adapted from Zoombar    
//    private Blackboard viewBlackboard() {
//        return gContext.getLayerViewPanel() != null ? gContext.getLayerViewPanel().getBlackboard() : new Blackboard();
//    }
//    
    public boolean execute(PlugInContext context) throws Exception {
        try {
            return true;
        } catch (Exception e) {
            context.getWorkbenchFrame().warnUser(I18N.get("org.openjump.core.ui.plugin.layer.AddSIDLayerPlugIn.Error-See-Output-Window"));
            context.getWorkbenchFrame().getOutputFrame().createNewDocument();
            context.getWorkbenchFrame().getOutputFrame().addText("ShowFullPathPlugIn Exception:" + e.toString());
            return false;
        }
    }

    private Envelope envelope(Collection geometries) {
        Envelope envelope = new Envelope();

        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            envelope.expandToInclude(geometry.getEnvelopeInternal());
        }

        return envelope;
    }
}
