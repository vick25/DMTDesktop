/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * JUMP is Copyright (C) 2003 Vivid Solutions
 *
 * This program implements extensions to JUMP and is
 * Copyright (C) 2007 Integrated Systems Analysts, Inc.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Integrated Systems Analysts, Inc.
 * 630C Anchors St., Suite 101
 * Fort Walton Beach, Florida
 * USA
 *
 * (850)862-7321
*/

package org.openjump.core.ui.plugin.layer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.vividsolutions.jts.geom.Geometry;
import com.osfac.dmt.I18N;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.feature.FeatureCollectionWrapper;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.feature.FeatureSchema;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.model.StandardCategoryNames;
import com.osfac.dmt.workbench.plugin.AbstractPlugIn;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.images.IconLoader;
import com.osfac.dmt.workbench.ui.plugin.FeatureInstaller;

public class ExtractLayerInFence extends AbstractPlugIn {

	   private final static String EXTRACT_LAYER_IN_FENCE = 
	    	I18N.get("org.openjump.core.ui.plugin.layer.ExtractLayerInFence.Extract-Layer-in-Fence");
	 
	public ExtractLayerInFence() {

	}
	
	public void initialize(PlugInContext context) throws Exception {
		WorkbenchContext workbenchContext = context.getWorkbenchContext();
		FeatureInstaller featureInstaller = new FeatureInstaller(
				workbenchContext);
		
		/*
		JPopupMenu layerNamePopupMenu = workbenchContext.getWorkbench()
				.getFrame().getLayerNamePopupMenu();
		featureInstaller.addPopupMenuItem(layerNamePopupMenu, this, getName(),
				false, ICON, createEnableCheck(workbenchContext));
		*/
		
	    context.getFeatureInstaller().addMainMenuItemWithJava14Fix(this,
		        new String[]
				{MenuNames.EDIT, MenuNames.EXTRACT},
				getName(), 
				false, 
				ICON, 
				createEnableCheck(context.getWorkbenchContext()));
	}

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);      
        return new MultiEnableCheck()
        .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
        .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1))
        .add(checkFactory.createFenceMustBeDrawnCheck());
    }  
    

	public boolean execute(PlugInContext context) throws Exception {
		Layer[] layers = context.getWorkbenchContext().getLayerNamePanel().getSelectedLayers();
		if (layers.length > 0){
			Layer layer = layers[0];
			splitLayer(context, layer);
			return true;
		} else
			return false;
	}
	
	public String getName() {
		return EXTRACT_LAYER_IN_FENCE;
	}

    public static final ImageIcon ICON = IconLoader.icon("extract1.gif");


    private void splitLayer(PlugInContext context, Layer layer)
    {
    	   Geometry fence = context.getLayerViewPanel().getFence();
   	
     		FeatureCollectionWrapper featureCollection = layer.getFeatureCollectionWrapper();
            List featureList = featureCollection.getFeatures();
            FeatureSchema featureSchema = layer.getFeatureCollectionWrapper().getFeatureSchema();
            
			boolean wasFiringEvents = context.getLayerManager().isFiringEvents();
            Collection selectedCategories = context.getLayerNamePanel().getSelectedCategories();
			context.getLayerManager().setFiringEvents(true);
			
	        Layer fencedLayer = context.addLayer(selectedCategories.isEmpty()
			? StandardCategoryNames.WORKING
			: selectedCategories.iterator().next().toString(), layer.getName(),
			 new FeatureDataset(featureSchema));
	        
	        
	        FeatureCollectionWrapper fencedFeatureCollection = fencedLayer.getFeatureCollectionWrapper();

			context.getLayerManager().setFiringEvents(false);
           for (Iterator i = featureList.iterator(); i.hasNext();)
            {
                Feature feature = (Feature) i.next();
                Geometry geometry = feature.getGeometry();
                
                if ((!geometry.isEmpty()) && (fence != null) &&  (geometry.intersects(fence)) )
                	fencedFeatureCollection.add((Feature)feature.clone());
                	//featureCollection.remove(feature);
            }   
            
    		context.getLayerManager().setFiringEvents(wasFiringEvents);
    		context.getLayerViewPanel().repaint();

    }


}
