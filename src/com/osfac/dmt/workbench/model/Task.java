package com.osfac.dmt.workbench.model;

import com.vividsolutions.jts.geom.Envelope;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

/**
 * Whatever the user needs to do a particular task. Currently a thin wrapper
 * around a LayerManager.
 */
// I wonder if this class should be named "Project" instead. [Bob Boseko]
public class Task implements LayerManagerProxy {

    private String name = "";
    private Point taskWindowLocation = null;
    private Dimension taskWindowSize = null;
    private boolean maximized = false;
    private Envelope savedViewEnvelope = null;
    private LayerManager layerManager;
    private List<NameListener> nameListeners = new ArrayList<>();
    private File projectFile = null;
    /**
     * The map of task properties.
     */
    private Map<QName, Object> properties = new HashMap<>();

    // No parameters so it can be created by Java2XML.
    public Task() {
        this.layerManager = new LayerManager(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        fireNameChanged(name);
    }

    public void add(NameListener nameListener) {
        nameListeners.add(nameListener);
    }

    private void fireNameChanged(String name) {
        for (NameListener nameListener : nameListeners) {
            nameListener.taskNameChanged(name);
        }
    }

    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    @SuppressWarnings("unchecked")
    public Collection<Category> getCategories() {
        return getLayerManager().getCategories();
    }

    /**
     * Called by Java2XML
     */
    @SuppressWarnings("unchecked")
    public void addCategory(Category category) {
        getLayerManager().addCategory(category.getName());

        Category actual = getLayerManager().getCategory(category.getName());

        for (Layerable layerable : (Collection<Layerable>) category.getLayerables()) {
            actual.addPersistentLayerable(layerable);
        }
    }

    /**
     * Set the value for the named property.
     *
     * @param name The name of the property.
     * @param value The value for the property.
     */
    public void setProperty(QName name, Object value) {
        properties.put(name, value);
    }

    /**
     * <p> Get the value for the named property casting it to the return value.
     * </p> <p> Instead of: </p>
     *
     * <pre>
     * Integer i = (Integer)task.getProperty(...)
     * </pre>
     *
     * <p> You can use the following: </p>
     *
     * <pre>
     * Integer i = task.getProperty(...)
     * </pre>
     *
     * @param name The name of the property.
     * @return value The value for the property.
     */
    @SuppressWarnings("unchecked")
    public <T> T getProperty(QName name) {
        return (T) properties.get(name);
    }

    /**
     * Get all the task properties.
     *
     * @return The task properties.
     */
    public Map<QName, Object> getProperties() {
        return properties;
    }

    /**
     * Set all the task properties.
     *
     * @param properties The task properties.
     */
    public void setProperties(Map<QName, Object> properties) {
        this.properties.putAll(properties);
    }

    public String toString() {
        return getName();
    }

    /**
     * Interface: NameListener must respond to task name changing.
     */
    public static interface NameListener {

        public void taskNameChanged(String name);
    }

    /*
     * The following getters and setters are for use by Java2XML to convert to and from XML
     */
    public Point getTaskWindowLocation() {
        return taskWindowLocation;
    }

    public int getTaskWindowLocationX() {
        return taskWindowLocation.x;
    }

    public int getTaskWindowLocationY() {
        return taskWindowLocation.y;
    }

    public void setTaskWindowLocation(Point taskWindowLocation) {
        this.taskWindowLocation = taskWindowLocation;
    }

    public void setTaskWindowLocationX(String x) {
        if (taskWindowLocation == null) {
            taskWindowLocation = new Point();
        }
        this.taskWindowLocation.x = Integer.valueOf(x);
    }

    public void setTaskWindowLocationY(String y) {
        if (taskWindowLocation == null) {
            taskWindowLocation = new Point();
        }
        this.taskWindowLocation.y = Integer.valueOf(y);
    }

    public Dimension getTaskWindowSize() {
        return taskWindowSize;
    }

    public int getTaskWindowSizeWidth() {
        return taskWindowSize.width;
    }

    public int getTaskWindowSizeHeight() {
        return taskWindowSize.height;
    }

    public void setTaskWindowSize(Dimension taskWindowSize) {
        this.taskWindowSize = taskWindowSize;
    }

    public void setTaskWindowSizeWidth(String width) {
        if (taskWindowSize == null) {
            taskWindowSize = new Dimension();
        }
        taskWindowSize.width = Integer.valueOf(width);
    }

    public void setTaskWindowSizeHeight(String height) {
        if (taskWindowSize == null) {
            taskWindowSize = new Dimension();
        }
        this.taskWindowSize.height = Integer.valueOf(height);
    }

    public boolean getMaximized() {
        return maximized;
    }

    public void setMaximized(boolean isMaximized) {
        this.maximized = isMaximized;
    }

    public Envelope getSavedViewEnvelope() {
        return savedViewEnvelope;
    }

    public double getTaskWindowZoomLeft() {
        return savedViewEnvelope.getMinX();
    }

    public double getTaskWindowZoomRight() {
        return savedViewEnvelope.getMaxX();
    }

    public double getTaskWindowZoomBottom() {
        return savedViewEnvelope.getMinY();
    }

    public double getTaskWindowZoomTop() {
        return savedViewEnvelope.getMaxY();
    }

    public void setSavedViewEnvelope(Envelope savedViewEnvelope) {
        this.savedViewEnvelope = savedViewEnvelope;
    }
    private double left = 0d;
    private double right = 0d;
    private double bottom = 0d;
    private double top = 0d;

    public void setTaskWindowZoomLeft(String left) {
        this.left = Double.valueOf(left);
    }

    public void setTaskWindowZoomRight(String right) {
        this.right = Double.valueOf(right);
    }

    public void setTaskWindowZoomBottom(String bottom) {
        this.bottom = Double.valueOf(bottom);
    }

    /**
     * This method must be called after all three previous have been done.
     * Java2XML does this. This is necessary because all parameters must be
     * known before an Envelope can be created (unlike Point and Dimension).
     *
     * @param top
     */
    public void setTaskWindowZoomTop(String top) {
        this.top = Double.valueOf(top);
        savedViewEnvelope = new Envelope(this.left, this.right, this.top, this.bottom);
    }
}
