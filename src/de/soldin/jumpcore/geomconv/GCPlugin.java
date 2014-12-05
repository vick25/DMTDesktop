/**
 *
 * Copyright 2011 Edgar Soldin
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.soldin.jumpcore.geomconv;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.util.GeometryEditor;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.osfac.dmt.feature.Feature;
import com.osfac.dmt.workbench.WorkbenchContext;
import com.osfac.dmt.workbench.model.Layer;
import com.osfac.dmt.workbench.plugin.EnableCheck;
import com.osfac.dmt.workbench.plugin.EnableCheckFactory;
import com.osfac.dmt.workbench.plugin.MultiEnableCheck;
import com.osfac.dmt.workbench.plugin.PlugInContext;
import com.osfac.dmt.workbench.ui.FeatureSelection;
import com.osfac.dmt.workbench.ui.LayerViewPanel;
import com.osfac.dmt.workbench.ui.MenuNames;
import com.osfac.dmt.workbench.ui.OKCancelDialog;
import com.osfac.dmt.workbench.ui.OKCancelDialog.Validator;
import com.osfac.dmt.workbench.ui.SelectionManagerProxy;

import de.soldin.jumpcore.ExtCorePlugIn;
import de.soldin.jumpcore.UndoableSetGeometry;


/**
 * A jump plugin for the conversion of geometries into one another. Currently
 * supported are geometries of the type
 * <ul>
 * <li>{@link com.vividsolutions.jts.geom.LineString}</li>
 * <li>{@link com.vividsolutions.jts.geom.LinearRing}</li>
 * <li>{@link com.vividsolutions.jts.geom.MultiPoint}</li>
 * <li>{@link com.vividsolutions.jts.geom.Point}</li>
 * <li>{@link com.vividsolutions.jts.geom.Polygon}</li>
 * </ul>
 * 
 * 
 * @see com.osfac.dmt.workbench.plugin.PlugIn
 */
public class GCPlugin extends ExtCorePlugIn {

	private GeometryFactory factory = new GeometryFactory();
	private List items;
	private WorkbenchContext wbc;
	private String target;
	private boolean youvebeenwarned = false;

	public GCPlugin() {
		this( null, null);
	}

	public GCPlugin(WorkbenchContext wbc, String target) {
		super();
		this.wbc = wbc;
		this.target = target;
		this.i18nPath = "language/geomconv/gc";
		
		// fetch possible geometry keys
		items = new ArrayList( this.getCreatableGeoms().keySet( ));
		Collections.sort(items);
		// add tools
		items.add("separator");
		items.add("close-lines");
		items.add("remove-closing-segment");
	}

	public void initialize(PlugInContext context) throws Exception {
		this.wbc = context.getWorkbenchContext();

		// create menu items
		String[] menuchain = new String[] { MenuNames.TOOLS,
				MenuNames.TOOLS_EDIT_GEOMETRY, getName() };
		JPopupMenu popupMenu = LayerViewPanel.popupMenu();
		String[] popupchain = new String[] { getName() };
		
		addToMainMenu(wbc, menuchain);
		try{
			//context.getFeatureInstaller().addPopupMenuSeparator(popupMenu, new String[]{});
			//context.getFeatureInstaller().addPopupMenuSeparator(popupMenu, new String[]{ "bla" });
			//context.getFeatureInstaller().addPopupMenuSeparator(popupMenu, new String[]{ "foo", "bar"});
			addToPopupMenu(wbc, popupMenu, popupchain);
		}catch (NoSuchMethodError e) {
			System.out.println("update to oj 1.4.1 for popupmenu entries");
		}
		// old installation routine, keep for reference
		/*
		// one checker to rule them all
		EnableCheck checker = createEnableCheck();
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			String menuentry = (String) iter.next();
			// add one plugin per menuitem
			GCPlugin plugin = new GCPlugin(wbc, menuentry);
			// add separator
			if (menuentry.equals("separator")) {
				context.getFeatureInstaller().addMenuSeparator(menuchain);
				getSubMenu(popupMenu, getName()).addSeparator();
			} else {
				context.getFeatureInstaller().addMainMenuItem(
				// one plugin per menuentry
						plugin, menuchain, a(menuentry.toLowerCase()), false, null, checker);
				// the layer popup menu
				context.getFeatureInstaller().addPopupMenuItem(popupMenu,
						plugin, popupchain, a(menuentry.toLowerCase()), false, null, checker);
			}
		}
		*/
	}
	
	// this can be used to attach it to another popup menu as well
	public void addToPopupMenu( WorkbenchContext wbc, JPopupMenu popupMenu, String[] popupchain){
		PlugInContext context = wbc.createPlugInContext();
		// one checker to rule them all
		EnableCheck checker = createEnableCheck();
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			String menuentry = (String) iter.next();
			// add one plugin per menuitem
			GCPlugin plugin = new GCPlugin(wbc, menuentry);
			// add separator
			if (menuentry.equals("separator")) {
				getSubMenu(popupMenu, popupchain).addSeparator();
			} else {
				// the layer popup menu
				context.getFeatureInstaller().addPopupMenuItem(popupMenu,
						plugin, popupchain, a(menuentry.toLowerCase()), false, null, checker);
			}
		}
	}
	
	public void addToMainMenu( WorkbenchContext wbc, String[] menuchain){
		PlugInContext context = wbc.createPlugInContext();
		// one checker to rule them all
		EnableCheck checker = createEnableCheck();
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			String menuentry = (String) iter.next();
			// add one plugin per menuitem
			GCPlugin plugin = new GCPlugin(wbc, menuentry);
			// add separator
			if (menuentry.equals("separator")) {
				context.getFeatureInstaller().addMenuSeparator(menuchain);
			} else {
				// one plugin per menuentry
				context.getFeatureInstaller().addMainMenuItem(
								plugin, menuchain, a(menuentry.toLowerCase()), false, null, checker);
			}
		}		
	}

	public boolean execute(PlugInContext context) throws Exception {

		if ( layerMode() ){
			Collection layers = getLayers();
			StringBuffer buf = new StringBuffer();
			for (Iterator i = layers.iterator(); i.hasNext();) {
				Layer layer = (Layer) i.next();
				String name = layer.getName();
				buf.append( buf.length()==0?name:", "+name);
			}
			String message = ( target.equalsIgnoreCase("close-lines") || target.equalsIgnoreCase("remove-closing-segment") ) ?
					"treat-all-with-tools" :
					"convert-all-to";
			if ( !okCancel( a("are-you-sure"), a(message, buf, a(target.toLowerCase()) ) ) )
						return false;
		}
		
		context.getLayerManager().getUndoableEditReceiver()
				.reportNothingToUndoYet();

		//Method method = (Method) getCreatableGeoms().get(target);
		//Class[] cparams = ( method instanceof Method ) ? method.getParameterTypes() : null;

		Collection layers = getLayers();
		// undo for all layers
		UndoableSetGeometry action = new UndoableSetGeometry(getName());

		for (Iterator li = layers.iterator(); li.hasNext();) {
			Layer layer = (Layer) li.next();
			Collection feats = getFeatures(layer);
			// create undo for layer
			UndoableSetGeometry layeraction = new UndoableSetGeometry(layer,
					getName());

			//System.out.println("exec " + target + " on " + layer.getName()
			//		+ " feats#" + feats.size());

			for (Iterator i = feats.iterator(); i.hasNext();) {
				try {
					Feature feat = (Feature) i.next();
					// keep for comparision with geomanew
					Geometry geomaorig = layeraction.getGeom(feat);
					// might be split in lines later
					Geometry geomasrc = geomaorig;
					Geometry geomanew = null;
					// reset warning monitor
					youvebeenwarned = false;
					
					// TOOLS
					if ( target.equalsIgnoreCase("close-lines") || target.equalsIgnoreCase("remove-closing-segment") ) {

						// these tools make only sense with line segmented geometries
						// that not insist on being closed rings
						if (geomasrc instanceof LinearRing 
								|| geomasrc instanceof Polygon 
								|| geomasrc instanceof MultiPolygon)
							geomasrc = factory.createMultiLineString( getLines( geomasrc ) );
						
						int count = geomasrc.getNumGeometries();
						boolean changed = false;
						Geometry[] geomsanew = new Geometry[count];
						GeometryEditor editor = new GeometryEditor();

						for (int j = 0; j < count; j++) {
							Geometry geom = geomasrc.getGeometryN(j);
							// weirdly closed linestrings always end up to be linearrings
							if ( geom instanceof LinearRing )
								geom = factory.createLineString(geom.getCoordinates());
							// get coordinates for check
							//Coordinate[] points = geom.getCoordinates();

							if (target.equalsIgnoreCase("close-lines") ) {
								geomsanew[j] = editor.edit( geom, new CloseRing());
							} else if (target.equalsIgnoreCase("remove-closing-segment") ) {
								geomsanew[j] = editor.edit( geom, new RemoveClosing());
							}

							//System.out.println("geom: "+geom);
							//System.out.println("new : "+geomsanew[j]);
							
							// did we receive a changed geometry?
							if ( !geomsanew[j].equalsExact(geom) ) changed = true;
						}
						// only create a new geometry if the old was changed
						if ( changed && count > 1 ) {						
							geomanew = factory.createGeometryCollection(geomsanew);
							// restore multigeometrytype of geomasrc collection
							geomanew = convert( geomanew, geomasrc.getGeometryType() );
						}
						else if ( changed && count == 1 )
							geomanew = geomsanew[0];
						else
							warnUser(a("nothing-to-do", feat.getID()));
						
					}				
					// CONVERSIONS (moved to external method)
					else {
						geomanew = convert( geomasrc, target);
					}
					
					//System.out.println(geomanew);
					//System.out.println(geomaorig);
					
					if (geomanew != null && !geomanew.equalsExact(geomaorig)) {
						layeraction.setGeom(feat, geomanew);
					}
					
				} 
				catch (IllegalArgumentException e) {
					warnUser(ae(e.getMessage()));
				}
				catch (InvocationTargetException ie) {
					if (ie.getCause() != null)
						warnUser(ae(ie.getCause().getLocalizedMessage()));
				}
			}
			
			if (!layeraction.isEmpty())	action.add(layeraction);

		}
		
		// don't execute & register empty actions
		if (!action.isEmpty()) {
			action.execute();
			wbc.getLayerManager().getUndoableEditReceiver().receive(action);
			return true;
		}
		// don't mask other warnings here
		else if (!youvebeenwarned ){
			warnUser(a("nothing-changed"));
		}

		// operation failed
		return false;
	}

	public String getName() {
		return a("convert-selected-to");
	}

	
	/*private JMenu getSubMenu(JMenu menu, String key) {
		for (int i = 0; i < menu.getItemCount(); i++) {
			if (menu.getItem(i) == null)
				continue;
			if (menu.getItem(i).getText().equals(key)) {
				return (JMenu) menu.getItem(i);
			}
		}
		return null;
	}*/
	
	private JMenu getSubMenu(MenuElement menu, String[] keys) {
		MenuElement[] ms = menu.getSubElements();
		String key = keys[0];
		for (int i = 0; i < ms.length; i++) {
			MenuElement m = ms[i];
			if (m == null)
				continue;
			if (m instanceof JMenu && ((JMenu) m).getText().equals(key)) {
				// snip first
				if (keys.length > 1) {
					String[] subkeys = new String[keys.length - 1];
					for (int j = 1; j < keys.length; j++) {
						subkeys[j] = keys[j];
					}
					m = getSubMenu(menu, subkeys);
				} else {
					return (JMenu) m;
				}
			}
		}
		return null;
	}

	public EnableCheck createEnableCheck() {
		EnableCheckFactory checkFactory = new EnableCheckFactory(this.wbc);
		MultiEnableCheck checker = new MultiEnableCheck();

		// taskframe must be active
		checker.add(checkFactory
				.createWindowWithLayerViewPanelMustBeActiveCheck());

		// are there selected layers OR layers with selected features
		// are these layers editable?
		checker.add(new EnableCheck() {
			public String check(JComponent component) {
				Collection layers = getLayers();
				if (layers == null || layers.isEmpty())
					return a("select-geometries-or-layers");

				for (Iterator iterator = layers.iterator(); iterator.hasNext();) {
					Layer layer = (Layer) iterator.next();
					// System.out.println(layer.getName()
					// +"->"+(layer.isEditable()?"ja":"nein"));
					if (!layer.isEditable()) {
						return a("layer-not-editable", layer.getName());
					}
				}

				// reached here? all is well
				return null;
			}
		});

		return checker;
	}

	private boolean layerMode(){
		FeatureSelection sel = ((SelectionManagerProxy) wbc.getWorkbench()
				.getFrame().getActiveInternalFrame()).getSelectionManager()
				.getFeatureSelection();

		// user hand picked geometries (features)
		if (!sel.getFeaturesWithSelectedItems().isEmpty()) {
			return false;
		}
		// user selected layers
		else {
			return true;
		}		
	}
	
	private Collection getFeatures(Layer layer) {
		FeatureSelection sel = ((SelectionManagerProxy) wbc.getWorkbench()
				.getFrame().getActiveInternalFrame()).getSelectionManager()
				.getFeatureSelection();

		Collection feats;
		// user hand picked geometries (features)
		if (!sel.getFeaturesWithSelectedItems().isEmpty()) {
			feats = sel.getFeaturesWithSelectedItems(layer);
		}
		// user only selected layers
		else {
			feats = layer.getFeatureCollectionWrapper().getFeatures();
		}

		return feats;
	}

	private Collection getLayers() {
		// all layers with selected items (parts of geometries)
		Collection layers = ((SelectionManagerProxy) wbc.getWorkbench()
				.getFrame().getActiveInternalFrame()).getSelectionManager()
				.getFeatureSelection().getLayersWithSelectedItems();
		return layers.isEmpty() ? Arrays.asList(wbc.getLayerNamePanel()
				.getSelectedLayers()) : layers;
	}
	
	private Collection<String> getTypes( GeometryCollection geom ){
		Collection types = new Vector();
		for (int i = 0; i < geom.getNumGeometries(); i++) {
			String type = geom.getGeometryN(i).getGeometryType();
			if (!types.contains( type ))
				types.add( type );
		}
		return types;
	}

	private Map getCreatableGeoms() {
		Map geoms = new Hashtable();
		Class cfactory = factory.getClass();
		Method[] methods = cfactory.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = (Method) methods[i];
			if (!method.getName().startsWith("create")) {
				continue;
			}
			// check if parameters match
			Class[] cparams = methods[i].getParameterTypes();
			boolean wrong = false;
			for (int j = 0; j < cparams.length; j++) {
				Class cparam = cparams[j];
				if (!validType(cparam)) {
					wrong = true;
					break;
				}
			}
			if (wrong)
				continue;

			String id = method.getName().replaceFirst("create", "");
			// blacklist some
			if (id.equals("PointFromInternalCoord") || id.equals("Geometry"))
				continue;

			geoms.put(id, method);
		}

		return geoms;
	}

	private boolean validType(Class clazz) {
		String name;
		if (clazz.isArray())
			name = clazz.getComponentType().getName();
		else
			name = clazz.getName();
		return name.equals("com.vividsolutions.jts.geom.Coordinate")
				|| name.equals("com.vividsolutions.jts.geom.LinearRing")
				|| name.equals("com.vividsolutions.jts.geom.LineString")
				|| name.equals("com.vividsolutions.jts.geom.Polygon")
				|| name.equals("com.vividsolutions.jts.geom.Geometry");
	}

	private void warnUser(final String message) {
		this.youvebeenwarned = true;
		this.wbc.getLayerViewPanel().getContext().warnUser(message);
	}

	private Geometry convert( Geometry geomasrc, String type ) throws Exception{
		Geometry geomanew = null;
		
		Method method = (Method) getCreatableGeoms().get(type);
		Class[] cparams = ( method instanceof Method ) ? method.getParameterTypes() : null;

		// do we have to && can we convert?
		if ( !(method instanceof Method) ) {
			// ups we've got ourself no conversion
			warnUser(a("no-conversion-method", a(type.toLowerCase()), type));
			return null;
		}
		
		/*
		// Multi*s and polygon always get line segments separated
		// Points contain azeroa line segments
		LineString[] lines = null;
		if ( (type.toLowerCase().startsWith("multi") || 
			type.equalsIgnoreCase("polygon")) ){
			lines = getLines( geomasrc, false );
			if (lines.length > 0)
				geomasrc = factory.createMultiLineString( lines );		
		}
		*/
		boolean isArray = cparams[0].isArray();
		String name = isArray ? cparams[0].getComponentType()
				.getName() : cparams[0].getName();
		// made from one coord, probably point ;)
		if (!isArray
				&& name.equals("com.vividsolutions.jts.geom.Coordinate")) {
			// System.out.println("eine koordinate");
			if (geomasrc.getCoordinates().length == 1) {
				geomanew = (Geometry) method.invoke(factory,
						new Object[] { geomasrc
								.getCoordinates()[0] });
			} else {
				warnUser(a("only-one-coordinate",type));
			}
		}
		// simple geometries made from coord[]
		else if (isArray
				&& name.equals("com.vividsolutions.jts.geom.Coordinate")) {
			// System.out.println("mehrere koordinaten");
			geomanew = (Geometry) method.invoke(factory,
					new Object[] { geomasrc.getCoordinates() });
		}
		// multilinestring
		else if (isArray
				&& name.equals("com.vividsolutions.jts.geom.LineString")) {
			Coordinate[] coords = geomasrc.getCoordinates();

			geomanew = (Geometry) method.invoke(factory,
					new Object[] { getLines( geomasrc ) });

		}
		// polygon
		else if (cparams.length == 2
				&& name.equals("com.vividsolutions.jts.geom.LinearRing")
				&& cparams[1].isArray()
				&& name.equals(cparams[1].getComponentType()
						.getName())) {

			geomanew = constructPolygon(geomasrc);

		}
		// multipolygon
		else if (isArray
				&& name.equals("com.vividsolutions.jts.geom.Polygon")) {
			// feed the line separated geom into algorithm
			Polygon[] polys = constructPolygons(geomasrc);
			if ( polys != null )
				geomanew = (Geometry) method.invoke(factory,
					new Object[] { polys });
		}
		// geometrycollection
		else if (isArray
				&& name.equals("com.vividsolutions.jts.geom.Geometry")) {
			// get all geoms and feed them to create
			Geometry[] geoms = new Geometry[geomasrc.getNumGeometries()];
			for (int j = 0; j < geomasrc.getNumGeometries(); j++) {
				geoms[j] = geomasrc.getGeometryN(j);
			}
			geomanew = (Geometry) method.invoke(factory,
					new Object[] { geoms });
		}
		// what ends here is based on parameters that are not implemented yet
		else {
			warnUser(a("conversion-not-implemented", a(type.toLowerCase()), type));
		}
		
		return geomanew;
	}
	
	/**
	 * borrowed from org.geotools.shapefile.PolygonHandler
	 * try to construct aonea polygon from the given geometry
	 */
	private Polygon constructPolygon(Geometry src) {
		
		// multigeometries with just points are tried to form one linearring
		// for further processing, else multipoint -> polygon is pointless
		if (src instanceof GeometryCollection){
			Collection<String> types = getTypes( (GeometryCollection) src );
			if ( types.size() == 1 && types.iterator().next().equalsIgnoreCase("point") )
				src = factory.createLinearRing( src.getCoordinates() );
		}
		
		ArrayList<LinearRing> shells = new ArrayList<LinearRing>();
		ArrayList<LinearRing> holes = new ArrayList<LinearRing>();
		try {
			// split by direction shell are cw, holes ccw
			for (int i = 0; i < src.getNumGeometries(); i++) {
				Geometry geom = src.getGeometryN(i);

				LinearRing ring = factory.createLinearRing(geom
						.getCoordinates());
				if (CGAlgorithms.isCCW(ring.getCoordinates())) {
					holes.add(ring);
				} else {
					shells.add(ring);
				}
			}

			// something went wrong, detect holes mow
			if (shells.size() != 1) {
				// throw it all together and let findCWHoles find it
				shells.addAll(holes);
				holes.clear();
				// some shells may be CW holes - esri tolerates this
				ArrayList<LinearRing> foundholes = findCWHoles(shells);
																		
				if (foundholes.size() > 0) {
					shells.removeAll(foundholes);
					for (int j = 0; j < foundholes.size(); j++) {
						LinearRing hole = (LinearRing) foundholes.get(j);
						// reverse cw holes
						if (!CGAlgorithms.isCCW(hole.getCoordinates()))
							hole = reverseRing(hole);
						holes.add(hole);
					}
				}
			}
			
			// ups, don't convert this
			if (shells.size() != 1) {
				warnUser(a("missing-exactly-one-shell"));
				return null;
			}
			
			return factory.createPolygon((LinearRing) shells.get(0),
					(LinearRing[]) ((ArrayList) holes)
							.toArray(new LinearRing[0]));
			
		} catch (IllegalArgumentException e) {
			warnUser(ae(e.getMessage()));
		}

		return null;
	}

	/**
	 * borrowed from org.geotools.shapefile.PolygonHandler
	 * try to construct multiple polygons from the given geometry
	 */
	public Polygon[] constructPolygons( Geometry src ) {

		// split src to ring components early
		src = factory.createMultiLineString( getRings(src) );
		
		GeometryFactory geometryFactory = this.factory;
		ArrayList<LinearRing> shells = new ArrayList<LinearRing>();
		ArrayList<LinearRing> holes = new ArrayList<LinearRing>();
		// Bad rings are CCW rings not nested in another ring
		// and rings with more than 0 and less than 4 points
		//ArrayList<LineString> badRings = new ArrayList<LineString>();
		// resulting polygons
		Polygon[] polygons;

		for (int i = 0; i < src.getNumGeometries(); i++) {
			Geometry geom = src.getGeometryN(i);
			
			Coordinate[] points = geom.getCoordinates();
			try {
				LinearRing ring = geometryFactory.createLinearRing(points);
				if (CGAlgorithms.isCCW(points)) {
					holes.add(ring);
				} else {
					shells.add(ring);
				}
			} catch (IllegalArgumentException e) {
				// badrings means loss, means error, means stop here
				warnUser(a("bad-rings"));
				return null;
			
				// crashes on simple points
				/*try {
					LineString ring = geometryFactory.createLineString(points);
					badRings.add(ring);
				} catch (IllegalArgumentException e2) {
					warnUser(a(e2.getMessage()));
				}*/
			}
		}

		if ((shells.size() > 1) && (holes.size() == 0)) {
			// some shells may be CW holes - esri tolerates this
			holes = findCWHoles(shells); // find all rings contained in others
			if (holes.size() > 0) {
				shells.removeAll(holes);
				ArrayList ccwHoles = new ArrayList(holes.size());
				for (int i = 0; i < holes.size(); i++) {
					ccwHoles.add(reverseRing((LinearRing) holes.get(i)));
				}
				holes = ccwHoles;
			}
		}

		// now we have a list of all shells and all holes
		ArrayList holesForShells = new ArrayList(shells.size());
		ArrayList holesWithoutShells = new ArrayList();

		for (int i = 0; i < shells.size(); i++) {
			holesForShells.add(new ArrayList());
		}

		// find holes
		for (int i = 0; i < holes.size(); i++) {
			LinearRing testRing = (LinearRing) holes.get(i);
			LinearRing minShell = null;
			Envelope minEnv = null;
			Envelope testEnv = testRing.getEnvelopeInternal();
			Coordinate testPt = testRing.getCoordinateN(0);
			LinearRing tryRing;
			for (int j = 0; j < shells.size(); j++) {
				tryRing = (LinearRing) shells.get(j);
				Envelope tryEnv = tryRing.getEnvelopeInternal();
				if (minShell != null)
					minEnv = minShell.getEnvelopeInternal();
				boolean isContained = false;
				Coordinate[] coordList = tryRing.getCoordinates();

				if (tryEnv.contains(testEnv)
						&& (CGAlgorithms.isPointInRing(testPt, coordList)))
					isContained = true;
				// check if this new containing ring is smaller than the current
				// minimum ring
				if (isContained) {
					if (minShell == null || minEnv.contains(tryEnv)) {
						minShell = tryRing;
					}
				}
			}

			if (minShell == null) {
				holesWithoutShells.add(testRing);
			} else {
				((ArrayList) holesForShells.get(shells.indexOf(minShell)))
						.add(testRing);
			}
		}

		polygons = new Polygon[shells.size() + holesWithoutShells.size()];

		for (int i = 0; i < shells.size(); i++) {
			polygons[i] = geometryFactory.createPolygon((LinearRing) shells
					.get(i), (LinearRing[]) ((ArrayList) holesForShells.get(i))
					.toArray(new LinearRing[0]));
		}

		// add ccw holes without shells as cw shells
		for (int i = 0; i < holesWithoutShells.size(); i++) {
			polygons[shells.size() + i] = geometryFactory.createPolygon(
					reverseRing( ((LinearRing) holesWithoutShells.get(i)) ), null);
		}

		holesForShells = null;
		holesWithoutShells = null;
		shells = null;
		holes = null;

		return polygons;
	}
	

	/**
	 * finds lr contained in other lr's (holes), returns all holes
	 */
	private ArrayList<LinearRing> findCWHoles(ArrayList shells) {
		ArrayList holesCW = new ArrayList(shells.size());
		LinearRing[] noHole = new LinearRing[0];
		for (int i = 0; i < shells.size(); i++) {
			LinearRing iRing = (LinearRing) shells.get(i);
			Envelope iEnv = iRing.getEnvelopeInternal();
			Coordinate[] coordList = iRing.getCoordinates();
			LinearRing jRing;
			for (int j = 0; j < shells.size(); j++) {
				if (i == j)
					continue;
				jRing = (LinearRing) shells.get(j);
				Envelope jEnv = jRing.getEnvelopeInternal();
				Coordinate jPt = jRing.getCoordinateN(0);
				Coordinate jPt2 = jRing.getCoordinateN(1);
				if (iEnv.contains(jEnv)
						// && (CGAlgorithms.isPointInRing(jPt, coordList) ||
						// pointInList(jPt, coordList))
						// && (CGAlgorithms.isPointInRing(jPt2, coordList) ||
						// pointInList(jPt2, coordList))) {
						&& (CGAlgorithms.isPointInRing(jPt, coordList))
						&& (CGAlgorithms.isPointInRing(jPt2, coordList))) {
					if (!holesCW.contains(jRing)) {
						Polygon iPoly = factory.createPolygon(iRing, noHole);
						Polygon jPoly = factory.createPolygon(jRing, noHole);
						if (iPoly.contains(jPoly))
							holesCW.add(jRing);
					}
				}
			}
		}
		return holesCW;
	}

	/**
	 * reverses the order of points in lr (is CW -> CCW or CCW->CW)
	 */
	private LinearRing reverseRing(LinearRing lr) {
		int numPoints = lr.getNumPoints();
		Coordinate[] newCoords = new Coordinate[numPoints];
		for (int t = 0; t < numPoints; t++) {
			newCoords[t] = lr.getCoordinateN(numPoints - t - 1);
		}
		return factory.createLinearRing(newCoords);
	}

	/**
	 * separate line components in a (multi)geometry
	 */
	private LineString[] getLines(Geometry geomasrc) {
		return (LineString[]) getLines( geomasrc, false);
	}
	private LinearRing[] getRings(Geometry geomasrc) {
		return (LinearRing[]) getLines( geomasrc, true);
	}	
	private Geometry[] getLines(Geometry geomasrc, boolean asRings) {
		// List linescol = new ArrayList();
		// geomasrc.apply(new LineFilter( linescol ));

		List linescol = new ArrayList();
		for (int i = 0; i < geomasrc.getNumGeometries(); i++) {
			Geometry g = geomasrc.getGeometryN(i);
			//System.out.println(g.getNumGeometries()+","+g.getGeometryType());
			if (g.getNumGeometries() > 1){
				linescol.addAll( Arrays.asList( getLines( g, asRings) ) );
			}
			else if (g.getGeometryType().toLowerCase().endsWith("polygon"))
				LinearComponentExtracter.getLines(g, linescol, !asRings);
			else if (asRings)
				linescol.add(factory.createLinearRing(g.getCoordinates()));
			else
				linescol.add(factory.createLineString(g.getCoordinates()));
		}

		return asRings ? factory.toLinearRingArray(linescol) : 
							factory.toLineStringArray(linescol);
	}

private boolean okCancel( final String title, final String message ){
    JPanel panel = new JPanel();
    JLabel label = new JLabel("<html> "+message+" </html>");
    Font xx = label.getFont();
    int fontHeight = label.getFontMetrics(xx).getHeight();
    int stringWidth = label.getFontMetrics(xx).stringWidth(label.getText());
    int linesCount = (int) Math.floor(stringWidth / 300);
    linesCount = Math.max(1, linesCount + 2);
    label.setPreferredSize(new Dimension(300, (fontHeight)*linesCount));
    FlowLayout f = new FlowLayout( FlowLayout.CENTER, 10, 10);
    panel.setLayout(f);
    panel.add(label);
    
    OKCancelDialog dlg = new OKCancelDialog(wbc.getWorkbench().getFrame(),
            title, true, panel,
            new Validator() {
                public String validateInput(Component component) {
                    return null;
                }
            });

    dlg.setVisible(true);

    return dlg.wasOKPressed() ? true : false;
}
	
private class CloseRing extends GeometryEditor.CoordinateOperation {
	public Coordinate[] edit(Coordinate[] coordinates, Geometry geometry) {
		// ignore already closed
		Coordinate first = coordinates[0];
		Coordinate last = coordinates[coordinates.length-1];

		if (first.equals(last))
			return coordinates;
		
		Coordinate[] coordinatesanew = new Coordinate[coordinates.length + 1];
		for (int i = 0; i < coordinates.length; i++) {
			coordinatesanew[i] = coordinates[i];
		}
		coordinatesanew[coordinatesanew.length - 1] = coordinates[0];
		return coordinatesanew;
	}
}

private class RemoveClosing extends GeometryEditor.CoordinateOperation {
	public Coordinate[] edit(Coordinate[] coordinates, Geometry geometry) {
		// ignore points, nothing to remove here
		if (coordinates.length<2)
			return coordinates;
		Coordinate first = coordinates[0];
		Coordinate last = coordinates[coordinates.length-1];
		while (first.equals(last)){
			coordinates = removeLast( coordinates );
			first = coordinates[0];
			last = coordinates[coordinates.length-1];
		}
		return coordinates;
	}
	
	private Coordinate[] removeLast(Coordinate[] coordinates){
		Coordinate[] coordinatesanew = new Coordinate[coordinates.length - 1];
		for (int i = 0; i < coordinates.length - 1; i++) {
			coordinatesanew[i] = coordinates[i];
		}
		return coordinatesanew;
	}
}

	private class LineFilter implements GeometryComponentFilter {
		private Collection lines;

		public LineFilter(List lines) {
			this.lines = lines;
		}

		public void filter(Geometry geom) {
			List linescol = new ArrayList();
			// convert each subgeom to linestring, jts throws exceptions on impossibles
			for (int i = 0; i < geom.getNumGeometries(); i++) {
				LineString line = geom.getFactory().createLineString(
						geom.getGeometryN(i).getCoordinates());
				lines.add(line);
			}
		}
	}
}