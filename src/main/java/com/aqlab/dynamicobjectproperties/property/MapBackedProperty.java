package com.aqlab.dynamicobjectproperties.property;

import java.util.Map;

/**
 * This represents a value corresponds to the given key in a map.
 * <p>
 * The property is always readable and writable. However it is possible to get a runtime exception (e.g. via a {@link java.util.Collections#unmodifiableMap(Map)} depending on the map being accessed.
 * <p>
 * Primitive getters and setters are not implemented.
 */
public class MapBackedProperty extends FunctionalPropertyObjectValue<Map<String, ?>> {

    private final String key;

    /**
     * The constructor
     *
     * @param key the key used to access the map
     */
    public MapBackedProperty(final String key) {
        super(Map.class, Object.class, "!MAP_BACKED[" + key + "]",
                m -> m.get(key), (m, v) -> ((Map) m).put(key, v));
        this.key = key;
    }

    /**
     * Get the key used to access the map
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }
}
