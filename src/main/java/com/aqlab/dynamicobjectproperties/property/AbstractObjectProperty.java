package com.aqlab.dynamicobjectproperties.property;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * A basic implementation of {@link com.aqlab.dynamicobjectproperties.property.ObjectProperty}
 *
 * @param <ObjectT> the target object type
 */
public abstract class AbstractObjectProperty<ObjectT> implements ObjectProperty<ObjectT> {

    private final Class<?> objectType;
    private final Class<?> valueType;
    private final String uniqueIdentifier;

    /**
     * The constructor
     *
     * @param objectType       the target object type
     * @param valueType        the value type
     * @param uniqueIdentifier a unique identifier
     */
    public AbstractObjectProperty(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier) {
        this.objectType = objectType;
        this.valueType = valueType;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    /**
     * An internal helper method to throw exception when inaccessible
     *
     * @param bean     the target instance
     * @param readable True for readability check. False for writability check.
     */
    protected void throwIfNotAccessible(final ObjectT bean, final boolean readable) {
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

    /**
     * Hash code is generated using the property class, target object type and the unique identifier
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getObjectType(), getUniqueIdentifier());
    }

    /**
     * Check if the incoming object is equal to this object: the property class, target object type and the unique identifier
     *
     * @return true if the 3 attributes of the incoming object equals to the curent object
     */
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