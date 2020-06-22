package com.aqlab.fastbeanreflector.property;

import java.util.Map;

public class MapBackedProperty extends AbstractObjectProperty<Map<String, ?>> {

    private final String propertyName;

    public MapBackedProperty(final String propertyName) {
        this(propertyName, propertyName);
    }

    public MapBackedProperty(final String propertyName, final String uniqueIdentifier) {
        super(Map.class, Object.class, uniqueIdentifier == null ? propertyName : uniqueIdentifier);
        this.propertyName = propertyName;
    }

    @Override
    public <ValueT> ValueT get(final Map<String, ?> bean) {
        return (ValueT) bean.get(propertyName);
    }

    @Override
    public void set(final Map<String, ?> bean, final Object value) {
        ((Map) bean).put(propertyName, value);
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public Class<?> getValueType() {
        return Object.class;
    }

}
