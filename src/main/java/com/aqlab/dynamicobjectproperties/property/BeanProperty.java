package com.aqlab.dynamicobjectproperties.property;

/**
 * This represents a bean property.
 * <p>
 * This has to be created via {@link #FACTORY}.
 * <p>
 * This implementation accesses the properties directly by code generation, i.e. native access time.
 *
 * @param <ObjectT> the target object type
 */
public abstract class BeanProperty<ObjectT> implements ObjectProperty<ObjectT> {

    /**
     * The singleton instance of the {@link com.aqlab.dynamicobjectproperties.property.BeanPropertyFactory}.
     */
    public static final BeanPropertyFactory FACTORY = BeanPropertyFactory.INSTANCE;

    private final FunctionalProperty<ObjectT> delegate;
    private final String propertyName;

    protected BeanProperty(final FunctionalProperty<ObjectT> delegate, final String propertyName) {
        if (BeanPropertyFactory.GENERATED_CLASSES_LOADER != getClass().getClassLoader()) {
            // only allow subclasses from BeanPropertyFactory
            throw new IllegalArgumentException("invalid class loader");
        }
        this.delegate = delegate;
        this.propertyName = propertyName;
    }

    /**
     * The name of this Java bean property
     *
     * @return the name
     */
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public boolean isReadable() {
        return delegate.isReadable();
    }

    @Override
    public boolean isWritable() {
        return delegate.isWritable();
    }

    @Override
    public Class<?> getObjectType() {
        return delegate.getObjectType();
    }

    @Override
    public Class<?> getValueType() {
        return delegate.getValueType();
    }

    @Override
    public String getUniqueIdentifier() {
        return delegate.getUniqueIdentifier();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        return delegate.equals(o);
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        return delegate.get(bean);
    }

    @Override
    public boolean canGetByte() {
        return delegate.canGetByte();
    }

    @Override
    public byte getByte(final ObjectT bean) {
        return delegate.getByte(bean);
    }

    @Override
    public boolean canGetChar() {
        return delegate.canGetChar();
    }

    @Override
    public char getChar(final ObjectT bean) {
        return delegate.getChar(bean);
    }

    @Override
    public boolean canGetShort() {
        return delegate.canGetShort();
    }

    @Override
    public short getShort(final ObjectT bean) {
        return delegate.getShort(bean);
    }

    @Override
    public boolean canGetInt() {
        return delegate.canGetInt();
    }

    @Override
    public int getInt(final ObjectT bean) {
        return delegate.getInt(bean);
    }

    @Override
    public boolean canGetLong() {
        return delegate.canGetLong();
    }

    @Override
    public long getLong(final ObjectT bean) {
        return delegate.getLong(bean);
    }

    @Override
    public boolean canGetFloat() {
        return delegate.canGetFloat();
    }

    @Override
    public float getFloat(final ObjectT bean) {
        return delegate.getFloat(bean);
    }

    @Override
    public boolean canGetDouble() {
        return delegate.canGetDouble();
    }

    @Override
    public double getDouble(final ObjectT bean) {
        return delegate.getDouble(bean);
    }

    @Override
    public boolean canGetBoolean() {
        return delegate.canGetBoolean();
    }

    @Override
    public boolean getBoolean(final ObjectT bean) {
        return delegate.getBoolean(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        delegate.set(bean, value);
    }

    @Override
    public boolean canSetByte() {
        return delegate.canSetByte();
    }

    @Override
    public void setByte(final ObjectT bean, final byte value) {
        delegate.setByte(bean, value);
    }

    @Override
    public boolean canSetChar() {
        return delegate.canSetChar();
    }

    @Override
    public void setChar(final ObjectT bean, final char value) {
        delegate.setChar(bean, value);
    }

    @Override
    public boolean canSetShort() {
        return delegate.canSetShort();
    }

    @Override
    public void setShort(final ObjectT bean, final short value) {
        delegate.setShort(bean, value);
    }

    @Override
    public boolean canSetInt() {
        return delegate.canSetInt();
    }

    @Override
    public void setInt(final ObjectT bean, final int value) {
        delegate.setInt(bean, value);
    }

    @Override
    public boolean canSetLong() {
        return delegate.canSetLong();
    }

    @Override
    public void setLong(final ObjectT bean, final long value) {
        delegate.setLong(bean, value);
    }

    @Override
    public boolean canSetFloat() {
        return delegate.canSetFloat();
    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {
        delegate.setFloat(bean, value);
    }

    @Override
    public boolean canSetDouble() {
        return delegate.canSetDouble();
    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {
        delegate.setDouble(bean, value);
    }

    @Override
    public boolean canSetBoolean() {
        return delegate.canSetBoolean();
    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {
        delegate.setBoolean(bean, value);
    }

    @Override
    public <T> ObjectProperty<T> castTargetType() {
        return delegate.castTargetType();
    }
}

