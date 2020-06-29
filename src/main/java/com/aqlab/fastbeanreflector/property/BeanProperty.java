package com.aqlab.fastbeanreflector.property;

public class BeanProperty<ObjectT> implements ObjectProperty<ObjectT> {

    public static final BeanPropertyFactory FACTORY = BeanPropertyFactory.INSTANCE;

    private final FunctionalProperty<ObjectT> delegate;

    BeanProperty(final FunctionalProperty<ObjectT> delegate) {
        this.delegate = delegate;
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
    public <ValueT> ValueT get(final ObjectT bean) {
        return delegate.get(bean);
    }

    @Override
    public byte getByte(final ObjectT bean) {
        return delegate.getByte(bean);
    }

    @Override
    public char getChar(final ObjectT bean) {
        return delegate.getChar(bean);
    }

    @Override
    public short getShort(final ObjectT bean) {
        return delegate.getShort(bean);
    }

    @Override
    public int getInt(final ObjectT bean) {
        return delegate.getInt(bean);
    }

    @Override
    public long getLong(final ObjectT bean) {
        return delegate.getLong(bean);
    }

    @Override
    public float getFloat(final ObjectT bean) {
        return delegate.getFloat(bean);
    }

    @Override
    public double getDouble(final ObjectT bean) {
        return delegate.getDouble(bean);
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
    public void setByte(final ObjectT bean, final byte value) {
        delegate.setByte(bean, value);
    }

    @Override
    public void setChar(final ObjectT bean, final char value) {
        delegate.setChar(bean, value);
    }

    @Override
    public void setShort(final ObjectT bean, final short value) {
        delegate.setShort(bean, value);
    }

    @Override
    public void setInt(final ObjectT bean, final int value) {
        delegate.setInt(bean, value);
    }

    @Override
    public void setLong(final ObjectT bean, final long value) {
        delegate.setLong(bean, value);
    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {
        delegate.setFloat(bean, value);
    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {
        delegate.setDouble(bean, value);
    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {
        delegate.setBoolean(bean, value);
    }
}
