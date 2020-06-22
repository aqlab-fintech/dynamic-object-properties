package com.aqlab.fastbeanreflector.property;

public interface ObjectProperty<ObjectT> {
    default <ValueT> ValueT get(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("'%s' of '%s' is not readable", getUniqueIdentifier(), getObjectType().getName()));
    }

    default byte getByte(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not a byte", getUniqueIdentifier()));
    }

    default short getShort(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not a short", getUniqueIdentifier()));
    }

    default int getInt(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not an int", getUniqueIdentifier()));
    }

    default long getLong(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not a long", getUniqueIdentifier()));
    }

    default float getFloat(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not a float", getUniqueIdentifier()));
    }

    default double getDouble(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not a double", getUniqueIdentifier()));
    }

    default boolean getBoolean(final ObjectT bean) {
        throw new UnsupportedOperationException(String.format("%s: value is not a boolean", getUniqueIdentifier()));
    }

    default void set(final ObjectT bean, final Object value) {
        throw new UnsupportedOperationException(String.format("'%s' of '%s' is not writable", getUniqueIdentifier(), getValueType().getName()));
    }

    default void setByte(final ObjectT bean, final byte value) {
        throw new UnsupportedOperationException(String.format("%s: value is not a byte", getUniqueIdentifier()));
    }

    default void setShort(final ObjectT bean, final short value) {
        throw new UnsupportedOperationException(String.format("%s: value is not a short", getUniqueIdentifier()));
    }

    default void setInt(final ObjectT bean, final int value) {
        throw new UnsupportedOperationException(String.format("%s: value is not an int", getUniqueIdentifier()));
    }

    default void setLong(final ObjectT bean, final long value) {
        throw new UnsupportedOperationException(String.format("%s: value is not a long", getUniqueIdentifier()));
    }

    default void setFloat(final ObjectT bean, final float value) {
        throw new UnsupportedOperationException(String.format("%s: value is not a float", getUniqueIdentifier()));
    }

    default void setDouble(final ObjectT bean, final double value) {
        throw new UnsupportedOperationException(String.format("%s: value is not a double", getUniqueIdentifier()));
    }

    default void setBoolean(final ObjectT bean, final boolean value) {
        throw new UnsupportedOperationException(String.format("%s: value is not a boolean", getUniqueIdentifier()));
    }

    default boolean isReadable() {
        return false;
    }

    default boolean isWritable() {
        return false;
    }

    Class<?> getObjectType();

    Class<?> getValueType();

    String getUniqueIdentifier();

    default <T> ObjectProperty<T> cast() {
        return (ObjectProperty<T>) this;
    }
}
