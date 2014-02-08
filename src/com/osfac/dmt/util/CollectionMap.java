package com.osfac.dmt.util;

import com.vividsolutions.jts.util.Assert;
import java.util.*;

public class CollectionMap implements Map {

    private Map map;
    private Class collectionClass = ArrayList.class;

    /**
     * Creates a CollectionMap backed by the given Map class.
     *
     * @param mapClass a Class that implements Map
     */
    public CollectionMap(Class mapClass) {
        try {
            map = (Map) mapClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Assert.shouldNeverReachHere();
        }
    }

    public CollectionMap(Class mapClass, Class collectionClass) {
        this.collectionClass = collectionClass;
        try {
            map = (Map) mapClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Assert.shouldNeverReachHere();
        }
    }

    /**
     * Creates a CollectionMap.
     */
    public CollectionMap() {
        this(HashMap.class);
    }

    private Collection getItemsInternal(Object key) {
        Collection collection = (Collection) map.get(key);
        if (collection == null) {
            try {
                collection = (Collection) collectionClass.newInstance();
            } catch (InstantiationException e) {
                Assert.shouldNeverReachHere();
            } catch (IllegalAccessException e) {
                Assert.shouldNeverReachHere();
            }
            map.put(key, collection);
        }
        return collection;
    }

    /**
     * Adds the item to the Collection at the given key, creating a new
     * Collection if necessary.
     *
     * @param key the key to the Collection to which the item should be added
     * @param item the item to add
     */
    public void addItem(Object key, Object item) {
        getItemsInternal(key).add(item);
    }

    public void removeItem(Object key, Object item) {
        getItemsInternal(key).remove(item);
    }

    public void clear() {
        map.clear();
    }

    /**
     * Adds the items to the Collection at the given key, creating a new
     * Collection if necessary.
     *
     * @param key the key to the Collection to which the items should be added
     * @param items the items to add
     */
    public void addItems(Object key, Collection items) {
        for (Iterator i = items.iterator(); i.hasNext();) {
            addItem(key, i.next());
        }
    }

    public void addItems(CollectionMap other) {
        for (Iterator i = other.keySet().iterator(); i.hasNext();) {
            Object key = i.next();
            addItems(key, other.getItems(key));
        }
    }

    /**
     * Returns the values.
     *
     * @return a view of the values, backed by this CollectionMap
     */
    public Collection values() {
        return map.values();
    }

    /**
     * Returns the keys.
     *
     * @return a view of the keys, backed by this CollectionMap
     */
    public Set keySet() {
        return map.keySet();
    }

    /**
     * Returns the number of mappings.
     *
     * @return the number of key-value pairs
     */
    public int size() {
        return map.size();
    }

    public Object get(Object key) {
        return getItems(key);
    }

    public Collection getItems(Object key) {
        return Collections.unmodifiableCollection(getItemsInternal(key));
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Object put(Object key, Object value) {
        Assert.isTrue(value instanceof Collection);

        return map.put(key, value);
    }

    public void putAll(Map map) {
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            Object key = i.next();
            //Delegate to #put so that the assertion is made. [Bob Boseko]
            put(key, map.get(key));
        }
    }

    public void removeItems(Object key, Collection items) {
        getItemsInternal(key).removeAll(items);
    }

    public Map getMap() {
        return map;
    }
}
