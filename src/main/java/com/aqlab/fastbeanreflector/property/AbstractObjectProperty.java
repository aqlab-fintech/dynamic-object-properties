package com.aqlab.fastbeanreflector.property;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public abstract class AbstractObjectProperty<ObjectT> implements ObjectProperty<ObjectT> {

    private final Class<?> objectType;
    private final Class<?> valueType;
    private final String uniqueIdentifier;

    public AbstractObjectProperty(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier) {
        this.objectType = objectType;
        this.valueType = valueType;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    protected void throwIfNotAccessible(final ObjectT bean, final boolean readable) {
        if (bean == null) {
            throw new NullPointerException("bean");
        }

        final boolean accessible;
        final String accessName;
        if (readable) {
            accessible = isReadable();
            accessName = "readable";
        } else {
            accessible = isWritable();
            accessName = "writable";
        }
        if (!accessible) {
            throw new UnsupportedOperationException(String.format("'%s' of '%s' is not %s", getUniqueIdentifier(), getObjectType().getName(), accessName));
        }
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    @Override
    public Class<?> getValueType() {
        return valueType;
    }

    @Override
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("objectType", getObjectType().getSimpleName())
                .add("propertyName", getUniqueIdentifier())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getObjectType(), getUniqueIdentifier());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o.getClass() == getClass()) {
            final ObjectProperty<?> otherProperty = (ObjectProperty<?>) o;
            return Objects.equals(getObjectType(), otherProperty.getObjectType()) &&
                    Objects.equals(getUniqueIdentifier(), otherProperty.getUniqueIdentifier());
        }

        return false;
    }
}
