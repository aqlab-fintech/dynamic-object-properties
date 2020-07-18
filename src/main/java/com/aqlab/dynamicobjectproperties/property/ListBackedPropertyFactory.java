package com.aqlab.dynamicobjectproperties.property;

import java.util.WeakHashMap;

/**
 * The factory of {@link com.aqlab.dynamicobjectproperties.property.ListBackedProperty}.
 */
public class ListBackedPropertyFactory {
    /**
     * The singleton instance
     */
    public static final ListBackedPropertyFactory INSTANCE = new ListBackedPropertyFactory();

    private static final WeakHashMap<Integer, ListBackedProperty> cache = new WeakHashMap<>();

    /**
     * Create an instance of {@link com.aqlab.dynamicobjectproperties.property.ListBackedProperty} if not already created. Return a cached instance otherwise.
     * <p>
     * Caching is done using a {@link java.util.WeakHashMap}.
     *
     * @param index the list index
     * @return an instance of {@link ListBackedProperty}
     */
    public static ListBackedProperty getListBackedProperty(final int index) {
        ListBackedProperty instance = cache.get(index);
        if (instance == null) {
            instance = new ListBackedProperty(index);
            cache.put(index, instance);
        }

        return instance;
    }
}
