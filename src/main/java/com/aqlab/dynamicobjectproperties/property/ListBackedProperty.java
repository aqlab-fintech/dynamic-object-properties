package com.aqlab.dynamicobjectproperties.property;

import java.util.List;

/**
 * This represents a value corresponds to the given index in a list.
 * <p>
 * The property is always readable and writable. However it is possible to get a runtime exception (e.g. via a {@link java.util.Collections#unmodifiableList} depending on the map being accessed.
 * <p>
 * Primitive getters and setters are not implemented.
 */
public class ListBackedProperty extends FunctionalPropertyObjectValue<List<?>> {

    public static final ListBackedPropertyFactory FACTORY = ListBackedPropertyFactory.INSTANCE;
    private final int index;

    protected ListBackedProperty(final int index) {
        super(List.class, Object.class, "!LIST_BACKED[" + index + "]", l -> l.get(index), (l, v) -> ((List) l).set(index, v));
        this.index = index;
    }

    /**
     * Get the index used to access the list
     *
     * @return the key
     */
    public int getIndex() {
        return index;
    }

}
