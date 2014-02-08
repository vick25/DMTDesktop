package com.osfac.dmt.workbench.model;

import com.vividsolutions.jts.util.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A folder containing Layers.
 */
public class Category {

    private ArrayList layerables = new ArrayList();
    private String name;
    private LayerManager layerManager;

    public Category() {
    }

    public void setName(String name) {
        this.name = name;
        fireCategoryChanged(CategoryEventType.METADATA_CHANGED);
    }

    public Task getTask() {
        if (layerManager != null) {
            return layerManager.getTask();
        } else {
            return null;
        }
    }

    public void setLayerManager(LayerManager layerManager) {
        this.layerManager = layerManager;
    }

    public void fireCategoryChanged(CategoryEventType type) {
        if (getLayerManager() == null) {
            //layerManager is null when Java2XML creates the object. [Bob Boseko]
            return;
        }

        getLayerManager().fireCategoryChanged(this, type);
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    /**
     * Called by Java2XML
     *
     * @return Layerables with enough information to be saved to a project file
     */
    public List getPersistentLayerables() {
        ArrayList persistentLayerables = new ArrayList();

        for (Iterator i = layerables.iterator(); i.hasNext();) {
            Layerable layerable = (Layerable) i.next();

            if (layerable instanceof Layer
                    && !((Layer) layerable).hasReadableDataSource()) {
                continue;
            }

            persistentLayerables.add(layerable);
        }

        return persistentLayerables;
    }

    public List getLayerables() {
        return Collections.unmodifiableList(layerables);
    }

    public void remove(Layerable layerable) {
        Assert.isTrue(contains(layerable));
        layerables.remove(layerable);
    }

    /**
     * @return -1 if the category does not contain the layerable
     */
    public int indexOf(Layerable layerable) {
        return layerables.indexOf(layerable);
    }

    public boolean contains(Layerable layerable) {
        return layerables.contains(layerable);
    }

    /**
     * @param index 0 to add to the top
     */
    public void add(int index, Layerable layerable) {
        layerables.add(index, layerable);
        if (getLayerManager() != null) {
            //layerManager is null when Java2XML creates the object. [Bob Boseko]
            getLayerManager().fireLayerChanged(layerable, LayerEventType.ADDED);
        }
    }

    /**
     * Called by Java2XML
     */
    public void addPersistentLayerable(Layerable layerable) {
        add(layerables.size(), layerable);
    }

    public boolean isEmpty() {
        return layerables.isEmpty();
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
