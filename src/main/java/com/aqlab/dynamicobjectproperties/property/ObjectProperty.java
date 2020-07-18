package com.aqlab.dynamicobjectproperties.property;

/**
 * The root interface of the Dynamic Object Properties (DOP) library.
 *
 * @param <ObjectT> the target object type this property is defined against
 */
public interface ObjectProperty<ObjectT> {
    /**
     * The getter of this property. Primitve values will be returned as boxed. Only implemented if
     * this property is readable. Exception is thrown otherwise.
     *
     * @param bean     the instance
     * @param <ValueT> the expected return value type
     * @return the value
     */
    default <ValueT> ValueT get(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("'%s' of '%s' is not readable", getUniqueIdentifier(), getObjectType().getName()));
    }

    /**
     * Return whether {@link #getByte(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetByte() {
        return false;
    }

    /**
     * The byte primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default byte getByte(final ObjectT bean) {
        if (!canGetByte()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getChar(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetChar() {
        return false;
    }

    /**
     * The char primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default char getChar(final ObjectT bean) {
        if (!canGetChar()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getShort(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetShort() {
        return false;
    }

    /**
     * The short primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default short getShort(final ObjectT bean) {
        if (!canGetShort()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getInt(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetInt() {
        return false;
    }

    /**
     * The int primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default int getInt(final ObjectT bean) {
        if (!canGetInt()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getLong(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetLong() {
        return false;
    }

    /**
     * The long primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default long getLong(final ObjectT bean) {
        if (!canGetLong()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getFloat(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetFloat() {
        return false;
    }

    default float getFloat(final ObjectT bean) {
        if (!canGetFloat()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getDouble(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetDouble() {
        return false;
    }

    /**
     * The double primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default double getDouble(final ObjectT bean) {
        if (!canGetDouble()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return 0;
    }

    /**
     * Return whether {@link #getBoolean(Object)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canGetBoolean() {
        return false;
    }

    /**
     * The boolean primitive getter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is readable. Exception is thrown otherwise.
     *
     * @param bean the instance
     * @return the property value
     */
    default boolean getBoolean(final ObjectT bean) {
        if (!canGetBoolean()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return false;
    }

    /**
     * The setter of this property. Unboxing is supported for primitive types. Only implemented if
     * this property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void set(final ObjectT bean, final Object value) {
        throw new UnsupportedOperationException(String.format("'%s' of '%s' is not writable", getUniqueIdentifier(), getValueType().getName()));
    }

    /**
     * Return whether {@link #setByte(Object, byte)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetByte() {
        return false;
    }

    /**
     * The byte primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setByte(final ObjectT bean, final byte value) {
        if (!canSetByte()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setChar(Object, char)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetChar() {
        return false;
    }

    /**
     * The char primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setChar(final ObjectT bean, final char value) {
        if (!canSetChar()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setShort(Object, short)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetShort() {
        return false;
    }

    /**
     * The short primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setShort(final ObjectT bean, final short value) {
        if (!canSetShort()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setInt(Object, int)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetInt() {
        return false;
    }

    /**
     * The int primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setInt(final ObjectT bean, final int value) {
        if (!canSetInt()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setLong(Object, long)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetLong() {
        return false;
    }

    /**
     * The long primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setLong(final ObjectT bean, final long value) {
        if (!canSetLong()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setFloat(Object, float)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetFloat() {
        return false;
    }

    /**
     * The float primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setFloat(final ObjectT bean, final float value) {
        if (!canSetFloat()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setDouble(Object, double)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetDouble() {
        return false;
    }

    /**
     * The double primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setDouble(final ObjectT bean, final double value) {
        if (!canSetDouble()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether {@link #setBoolean(Object, boolean)} is implemented
     *
     * @return True if implement. False otherwise.
     */
    default boolean canSetBoolean() {
        return false;
    }

    /**
     * The boolean primitive setter in order to avoid boxing/unboxing. Only implemented if
     * this property is a primitive byte and the property is writable. Exception is thrown otherwise.
     *
     * @param bean  the instance
     * @param value the property value to set
     */
    default void setBoolean(final ObjectT bean, final boolean value) {
        if (!canSetBoolean()) {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    /**
     * Return whether property is readable
     *
     * @return True if it is readable. False otherwise.
     */
    default boolean isReadable() {
        return false;
    }

    /**
     * Return whether property is writable
     *
     * @return True if it is writable. False otherwise.
     */
    default boolean isWritable() {
        return false;
    }

    /**
     * Get the class of the target object of this property
     *
     * @return the Class of the target object of this property
     */
    Class<?> getObjectType();

    /**
     * Get the class of the value of this property
     *
     * @return the Class of the value of this property
     */
    Class<?> getValueType();

    /**
     * A unique identifier. It should be unique across the same type of properties and consistent across processes.
     * <p>
     * Suggested pattern: [Property class name / identifier][instance attribute(s)]<br>
     * E.g. "!MAP_BACKED[mapKey1]" for {@link MapBackedProperty} with key "mapKey1"
     * <p>
     * Note: As a convention, "!" is reserved for property types defined in the library.
     *
     * @return a string which uniquely identifies this property instance
     */
    String getUniqueIdentifier();

    /**
     * A helper which is useful casting the target object type of this property to its superclass
     *
     * @param <T> the new target object type
     * @return this property instance casted to the desired target object type
     */
    default <T> ObjectProperty<T> castTargetType() {
        return (ObjectProperty<T>) this;
    }
}

