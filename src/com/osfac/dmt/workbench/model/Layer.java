package com.osfac.dmt.workbench.model;

import com.osfac.dmt.feature.FeatureCollection;
import com.osfac.dmt.feature.FeatureCollectionWrapper;
import com.osfac.dmt.feature.FeatureDataset;
import com.osfac.dmt.io.datasource.DataSourceQuery;
import com.osfac.dmt.util.Blackboard;
import com.osfac.dmt.workbench.ui.plugin.AddNewLayerPlugIn;
import com.osfac.dmt.workbench.ui.renderer.style.BasicStyle;
import com.osfac.dmt.workbench.ui.renderer.style.LabelStyle;
import com.osfac.dmt.workbench.ui.renderer.style.SquareVertexStyle;
import com.osfac.dmt.workbench.ui.renderer.style.Style;
import com.osfac.dmt.workbench.ui.renderer.style.VertexStyle;
import com.vividsolutions.jts.util.Assert;
import java.awt.Color;
import java.util.*;
import javax.swing.SwingUtilities;

/**
 * Adds colour, line-width, and other stylistic information to a Feature
 * Collection. <p> When adding or removing multiple features to this Layer's
 * FeatureCollection, prefer #addAll and #removeAll to #add and #remove -- fewer
 * events will be fired.
 */
public class Layer extends AbstractLayerable implements LayerManagerProxy {

    public static final String FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE = Layer.class
            .getName() + " - FIRING APPEARANCE CHANGED ON ATTRIBUTE CHANGE";
    private String description = "";
    private boolean drawingLast = false;
    private FeatureCollectionWrapper featureCollectionWrapper;
    private ArrayList styles = new ArrayList();
    private boolean synchronizingLineColor = true;
    private boolean editable = false;
    private boolean selectable = true;
    private boolean readonly = false;
    private LayerListener layerListener = null;
    private Blackboard blackboard = new Blackboard() {
        private static final long serialVersionUID = 6504993615735124204L;

        {
            put(FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE, true);
        }
    };
    private boolean featureCollectionModified = false;
    private DataSourceQuery dataSourceQuery;

    /**
     * Called by Java2XML
     */
    public Layer() {
    }

    public Layer(String name, Color fillColor,
            FeatureCollection featureCollection, LayerManager layerManager) {
        super(name, layerManager);
        Assert.isTrue(featureCollection != null);

        //Can't fire events because this Layerable hasn't been added to the
        //LayerManager yet. [Bob Boseko]
        boolean firingEvents = layerManager.isFiringEvents();
        layerManager.setFiringEvents(false);

        try {
            addStyle(new BasicStyle());
            addStyle(new SquareVertexStyle());
            addStyle(new LabelStyle());
        } finally {
            layerManager.setFiringEvents(firingEvents);
        }

        getBasicStyle().setFillColor(fillColor);
        getBasicStyle().setLineColor(defaultLineColor(fillColor));
        getBasicStyle().setAlpha(150);
        setFeatureCollection(featureCollection);
    }

    /**
     * @return a darker version of the given fill colour, for use as the line
     * colour
     */
    public static Color defaultLineColor(Color fillColor) {
        return fillColor.darker();
    }

    public void setDescription(String description) {
        Assert.isTrue(
                description != null,
                "Java2XML requires that the description be non-null. Use an empty string if necessary.");
        this.description = description;
    }

    /**
     * @return true if this layer should always be 'readonly' I.e.: The layer
     * should never have the editable field set to true.
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Set whether this layer can be made editable.
     */
    public void setReadonly(boolean value) {
        readonly = value;
    }

    /**
     * @return true if features in this layer can be selected.
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Set whether or not features in this layer can be selected.
     *
     * @param value true if features in this layer can be selected
     */
    public void setSelectable(boolean value) {
        selectable = value;
    }

    /**
     * Used for lightweight layers like the Vector layer.
     *
     * @param drawingLast true if the layer should be among those drawn last
     */
    public void setDrawingLast(boolean drawingLast) {
        this.drawingLast = drawingLast;
        fireAppearanceChanged();
    }

    public void setFeatureCollection(final FeatureCollection featureCollection) {
        final FeatureCollection oldFeatureCollection = featureCollectionWrapper != null ? featureCollectionWrapper
                .getUltimateWrappee()
                : AddNewLayerPlugIn.createBlankFeatureCollection();
        ObservableFeatureCollection observableFeatureCollection = new ObservableFeatureCollection(
                featureCollection);
        observableFeatureCollection.checkNotWrappingSameClass();
        observableFeatureCollection
                .add(new ObservableFeatureCollection.Listener() {
            public void featuresAdded(Collection features) {
                getLayerManager().fireFeaturesChanged(features,
                        FeatureEventType.ADDED, Layer.this);
            }

            public void featuresRemoved(Collection features) {
                getLayerManager().fireFeaturesChanged(features,
                        FeatureEventType.DELETED, Layer.this);
            }
        });

        if ((getLayerManager() != null)
                && getLayerManager().getLayers().contains(this)) {
            //Don't fire APPEARANCE_CHANGED immediately, to avoid the
            //following problem:
            //(1) Add fence layer
            //(2) LAYER_ADDED event will be called
            //(3) APPEARANCE_CHANGED will be fired in this method (before
            //the JTree receives its LAYER_ADDED event)
            //(4) The JTree will complain because it gets the
            // APPEARANCE_CHANGED
            //event before the LAYER_ADDED event:
            //            java.lang.ArrayIndexOutOfBoundsException: 0 >= 0
            //                at java.util.Vector.elementAt(Vector.java:412)
            //                at
            // javax.swing.tree.DefaultMutableTreeNode.getChildAt(DefaultMutableTreeNode.java:226)
            //                at
            // javax.swing.tree.VariableHeightLayoutCache.treeNodesChanged(VariableHeightLayoutCache.java:369)
            //                at
            // javax.swing.plaf.basic.BasicTreeUI$TreeModelHandler.treeNodesChanged(BasicTreeUI.java:2339)
            //                at
            // javax.swing.tree.DefaultTreeModel.fireTreeNodesChanged(DefaultTreeModel.java:435)
            //                at
            // javax.swing.tree.DefaultTreeModel.nodesChanged(DefaultTreeModel.java:318)
            //                at
            // javax.swing.tree.DefaultTreeModel.nodeChanged(DefaultTreeModel.java:251)
            //                at
            // com.osfac.dmt.workbench.model.LayerTreeModel.layerChanged(LayerTreeModel.java:292)
            //[Bob Boseko]
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //Changed APPEARANCE_CHANGED event to FEATURE_DELETED and
                    //FEATURE_ADDED events, but I think the lengthy comment
                    //above still applies. [Bob Boseko]
                    //Drop #isEmpty checks, so that database-backed feature
                    //collections don't have to implement it.
                    //[Bob Boseko 2005-03-02]
                    getLayerManager().fireFeaturesChanged(
                            oldFeatureCollection.getFeatures(),
                            FeatureEventType.DELETED, Layer.this);
                    getLayerManager().fireFeaturesChanged(
                            featureCollection.getFeatures(),
                            FeatureEventType.ADDED, Layer.this);
                }
            });
        }

        setFeatureCollectionWrapper(observableFeatureCollection);
    }

    /**
     * Editability is not enforced; all parties are responsible for heeding this
     * flag.
     */
    public void setEditable(boolean editable) {
        if (this.editable == editable) {
            return;
        }

        this.editable = editable;
        fireLayerChanged(LayerEventType.METADATA_CHANGED);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setSynchronizingLineColor(boolean synchronizingLineColor) {
        this.synchronizingLineColor = synchronizingLineColor;
        fireAppearanceChanged();
    }

    public BasicStyle getBasicStyle() {
        return (BasicStyle) getStyle(BasicStyle.class);
    }

    public VertexStyle getVertexStyle() {
        return (VertexStyle) getStyle(VertexStyle.class);
    }

    public LabelStyle getLabelStyle() {
        return (LabelStyle) getStyle(LabelStyle.class);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns a wrapper around the FeatureCollection which was added using
     * #wrapFeatureCollection. The original FeatureCollection can be retrieved
     * using FeatureCollectionWrapper#getWrappee. However, parties are
     * encouraged to use the FeatureCollectionWrapper instead, so that feature
     * additions and removals cause FeatureEvents to be fired (by the Layer).
     */
    public FeatureCollectionWrapper getFeatureCollectionWrapper() {
        return featureCollectionWrapper;
    }

    protected void setFeatureCollectionWrapper(
            FeatureCollectionWrapper featureCollectionWrapper) {
        this.featureCollectionWrapper = featureCollectionWrapper;
    }

    /**
     * Styles do not notify the Layer when their parameters change. Therefore,
     * after you modify a Style's parameters (for example, the fill colour of
     * BasicStyle), be sure to call #fireAppearanceChanged
     *
     * @param c Can even be the desired Style's superclass or interface
     * @return The style value
     */
    public Style getStyle(Class c) {
        for (Iterator i = styles.iterator(); i.hasNext();) {
            Style p = (Style) i.next();

            if (c.isInstance(p)) {
                return p;
            }
        }

        return null;
    }

    public List getStyles() {
        return Collections.unmodifiableList(styles);
    }

    public boolean hasReadableDataSource() {
        return dataSourceQuery != null
                && dataSourceQuery.getDataSource().isReadable();
    }

    public boolean isDrawingLast() {
        return drawingLast;
    }

    public boolean isSynchronizingLineColor() {
        return synchronizingLineColor;
    }

    public void addStyle(Style style) {
        styles.add(style);
        fireAppearanceChanged();
    }

    /**
     * Releases references to the data, to facilitate garbage collection.
     * Important for MDI apps like the JUMP Workbench. Called when the last
     * JInternalFrame viewing the LayerManager is closed (i.e. internal frame's
     * responsibility). To conserve memory, if layers are frequently added and
     * removed from the LayerManager, parties may want to call #dispose
     * themselves rather than waiting for the internal frame to be closed.
     */
    public void dispose() {
        //Don't just call FeatureCollection#removeAll, because it may be a
        // database
        //table, and we don't want to delete its contents! [Bob Boseko]
        setFeatureCollection(AddNewLayerPlugIn.createBlankFeatureCollection());
    }

    public void removeStyle(Style p) {
        Assert.isTrue(styles.remove(p));
        fireAppearanceChanged();
    }

    public Collection cloneStyles() {
        ArrayList styleClones = new ArrayList();

        for (Iterator i = getStyles().iterator(); i.hasNext();) {
            Style style = (Style) i.next();
            styleClones.add(style.clone());
        }

        return styleClones;
    }

    public void setStyles(Collection newStyles) {
        boolean firingEvents = getLayerManager().isFiringEvents();
        getLayerManager().setFiringEvents(false);

        try {
            //new ArrayList to prevent ConcurrentModificationException [Jon
            // Aquino]
            for (Iterator i = new ArrayList(getStyles()).iterator(); i
                    .hasNext();) {
                Style style = (Style) i.next();
                removeStyle(style);
            }

            for (Iterator i = newStyles.iterator(); i.hasNext();) {
                Style style = (Style) i.next();
                addStyle(style);
            }
        } finally {
            getLayerManager().setFiringEvents(firingEvents);
        }

        fireAppearanceChanged();
    }

    public void setLayerManager(LayerManager layerManager) {
        if (layerManager != null) {
            layerManager.removeLayerListener(getLayerListener());
        }

        super.setLayerManager(layerManager);
        layerManager.addLayerListener(getLayerListener());
    }

    private LayerListener getLayerListener() {
        //Need to create layerListener lazily because it will be called by the
        //superclass constructor. [Bob Boseko]
        if (layerListener == null) {
            layerListener = new LayerListener() {
                public void featuresChanged(FeatureEvent e) {
                    if (e.getLayer() == Layer.this) {
                        setFeatureCollectionModified(true);

                        //Before I wasn't firing appearance-changed on an
                        // attribute
                        //change. But now with labelling and colour theming,
                        //I have to. [Bob Boseko]
                        if (e.getType() != FeatureEventType.ATTRIBUTES_MODIFIED
                                || getBlackboard()
                                .get(
                                FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE,
                                true)) {
                            //Fixed bug above -- wasn't supplying a default
                            // value to
                            //Blackboard#getBoolean, resulting in a
                            // NullPointerException
                            //when the Layer was created using the
                            // parameterless
                            //constructor (because that constructor doesn't
                            // initialize
                            //FIRING_APPEARANCE_CHANGED_ON_ATTRIBUTE_CHANGE
                            //on the blackboard [Bob Boseko 10/21/2003]
                            fireAppearanceChanged();
                        }
                    }
                }

                public void layerChanged(LayerEvent e) {
                }

                public void categoryChanged(CategoryEvent e) {
                }
            };
        }

        return layerListener;
    }

    public Blackboard getBlackboard() {
        return blackboard;
    }

    /**
     * Enables a layer to be changed undoably. Since the layer's features are
     * saved, only use this method for layers with few features.
     */
    public static UndoableCommand addUndo(final String layerName,
            final LayerManagerProxy proxy, final UndoableCommand wrappeeCommand) {
        return new UndoableCommand(wrappeeCommand.getName()) {
            private Layer layer;
            private String categoryName;
            private Collection features;
            private boolean visible;

            private Layer currentLayer() {
                return proxy.getLayerManager().getLayer(layerName);
            }

            public void execute() {
                layer = currentLayer();

                if (layer != null) {
                    features = new ArrayList(layer
                            .getFeatureCollectionWrapper().getFeatures());
                    categoryName = layer.getName();
                    visible = layer.isVisible();
                }

                wrappeeCommand.execute();
            }

            public void unexecute() {
                wrappeeCommand.unexecute();

                if ((layer == null) && (currentLayer() != null)) {
                    proxy.getLayerManager().remove(currentLayer());
                }

                if ((layer != null) && (currentLayer() == null)) {
                    proxy.getLayerManager().addLayer(categoryName, layer);
                }

                if (layer != null) {
                    layer.getFeatureCollectionWrapper().clear();
                    layer.getFeatureCollectionWrapper().addAll(features);
                    layer.setVisible(visible);
                }
            }
        };
    }

    /**
     * Does nothing if the underlying feature collection is not a
     * FeatureDataset.
     */
    public static void tryToInvalidateEnvelope(Layer layer) {
        if (layer.getFeatureCollectionWrapper().getUltimateWrappee() instanceof FeatureDataset) {
            ((FeatureDataset) layer.getFeatureCollectionWrapper()
                    .getUltimateWrappee()).invalidateEnvelope();
        }
    }

    public DataSourceQuery getDataSourceQuery() {
        return dataSourceQuery;
    }

    public Layer setDataSourceQuery(DataSourceQuery dataSourceQuery) {
        this.dataSourceQuery = dataSourceQuery;

        return this;
    }

    public boolean isFeatureCollectionModified() {
        return featureCollectionModified;
    }

    public Layer setFeatureCollectionModified(boolean featureCollectionModified) {
        if (this.featureCollectionModified == featureCollectionModified) {
            return this;
        }

        this.featureCollectionModified = featureCollectionModified;
        fireLayerChanged(LayerEventType.METADATA_CHANGED);

        return this;
    }
}