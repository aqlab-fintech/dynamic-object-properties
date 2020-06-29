package com.aqlab.fastbeanreflector.property;

import java.util.List;

public class ChainedProperty<ObjectT> extends ObjectPropertyWithDependencies<ObjectT> {

    private final ObjectProperty<Object> lastProperty;
    private final List<ObjectProperty<?>> chain;

    public static <T> ChainedProperty<T> create(final List<ObjectProperty<?>> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new IllegalArgumentException("chain is empty");
        }

        final Class<?> objectType = chain.get(0).getObjectType();
        int lastIndex = 0;
        final StringBuilder uniqueIdentifierBuilder = new StringBuilder();

        while (lastIndex < chain.size() - 1) {
            final ObjectProperty<Object> current = chain.get(lastIndex).cast();
            if (current == null) {
                throw new IllegalArgumentException(String.format("null property at chain[%d]", lastIndex));
            }
            if (!current.isReadable()) {
                throw new IllegalArgumentException(String.format("not readable at chain[%d]", lastIndex));
            }
            uniqueIdentifierBuilder.append(current.getUniqueIdentifier()).append('.');
            lastIndex++;
        }

        final ObjectProperty<Object> lastProperty = chain.get(chain.size() - 1).cast();
        final Class<?> valueType = lastProperty.getValueType();
        uniqueIdentifierBuilder.append(lastProperty.getUniqueIdentifier());
        final String uniqueIdentifier = uniqueIdentifierBuilder.toString();
        return new ChainedProperty<>(objectType, valueType, uniqueIdentifier, lastProperty, chain);
    }

    private ChainedProperty(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final ObjectProperty<Object> lastProperty, final List<ObjectProperty<?>> chain) {
        super(objectType, valueType, uniqueIdentifier);
        this.lastProperty = lastProperty;
        this.chain = chain;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        return lastProperty.cast().get(getLastTargetObject(bean));
    }

    @Override
    public byte getByte(final ObjectT bean) {
        return lastProperty.cast().getByte(getLastTargetObject(bean));
    }

    @Override
    public char getChar(final ObjectT bean) {
        return 0;
    }

    @Override
    public short getShort(final ObjectT bean) {
        return 0;
    }

    @Override
    public int getInt(final ObjectT bean) {
        return 0;
    }

    @Override
    public long getLong(final ObjectT bean) {
        return 0;
    }

    @Override
    public float getFloat(final ObjectT bean) {
        return 0;
    }

    @Override
    public double getDouble(final ObjectT bean) {
        return 0;
    }

    @Override
    public boolean getBoolean(final ObjectT bean) {
        return false;
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        lastProperty.cast().set(getLastTargetObject(bean), value);
    }

    @Override
    public void setByte(final ObjectT bean, final byte value) {

    }

    @Override
    public void setChar(final ObjectT bean, final char value) {

    }

    @Override
    public void setShort(final ObjectT bean, final short value) {

    }

    @Override
    public void setInt(final ObjectT bean, final int value) {

    }

    @Override
    public void setLong(final ObjectT bean, final long value) {

    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {

    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {

    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {

    }

    private Object getLastTargetObject(final Object bean) {
        Object result = bean;
        for (int i = 0; i < chain.size() - 1 && result != null; i++) {
            result = chain.get(i).cast().get(result);
        }

        return result;
    }

    @Override
    public boolean isReadable() {
        return lastProperty.isReadable();
    }

    @Override
    public boolean isWritable() {
        return lastProperty.isWritable();
    }

    @Override
    public List<ObjectProperty<?>> getDependents() {
        return chain;
    }
}
