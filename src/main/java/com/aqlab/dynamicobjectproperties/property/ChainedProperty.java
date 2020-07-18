package com.aqlab.dynamicobjectproperties.property;

import java.util.Arrays;
import java.util.List;

/**
 * This represents a property which is the result of a chain of properties.
 * <p>
 * For example
 * <ol>
 *     <li>A {@code Company} object with a {@code getLocation()} returning a {@code Branch} object</li>
 *     <li>A {@code Branch} object with a {@code getManager()} returning a {@code Staff} object</li>
 *     <li>A {@code Staff} object with a {@code getName()} returning a {@code String}</li>
 * </ol>
 * <p>
 * One can create a {@link ChainedProperty} as above, targeting {@code Company}, called {@code branchManagerName}.
 *
 * @param <ObjectT> the target object type generic argument
 */
public class ChainedProperty<ObjectT> extends ObjectPropertyWithDependencies<ObjectT> {

    private final ObjectProperty<Object> lastProperty;
    private final List<ObjectProperty<?>> chain;


    /**
     * A factory method for the ease of creation of {@link ChainedProperty}
     *
     * @param chain a list of {@link ObjectProperty} in the order of access
     * @param <T>   the target object type generic argument
     * @return an instance of {@link ChainedProperty}
     */
    public static <T> ChainedProperty<T> create(final ObjectProperty<?>... chain) {
        return create(Arrays.asList(chain));
    }

    /**
     * A factory method for the ease of creation of {@link ChainedProperty}
     *
     * @param chain a list of {@link ObjectProperty} in the order of access
     * @param <T>   the target object type generic argument
     * @return an instance of {@link ChainedProperty}
     */
    public static <T> ChainedProperty<T> create(final List<ObjectProperty<?>> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new IllegalArgumentException("chain is empty");
        }

        final Class<?> objectType = chain.get(0).getObjectType();
        int lastIndex = 0;
        final StringBuilder uniqueIdentifierBuilder = new StringBuilder().append("!CHAIN[");

        while (lastIndex < chain.size() - 1) {
            final ObjectProperty<Object> current = chain.get(lastIndex).castTargetType();
            if (current == null) {
                throw new IllegalArgumentException(String.format("null property at chain[%d]", lastIndex));
            }
            if (!current.isReadable()) {
                throw new IllegalArgumentException(String.format("not readable at chain[%d]", lastIndex));
            }
            uniqueIdentifierBuilder.append(current.getUniqueIdentifier()).append('>');
            lastIndex++;
        }

        final ObjectProperty<Object> lastProperty = chain.get(chain.size() - 1).castTargetType();
        final Class<?> valueType = lastProperty.getValueType();
        uniqueIdentifierBuilder.append(lastProperty.getUniqueIdentifier()).append(']');
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
        return lastProperty.castTargetType().get(getLastTargetObject(bean));
    }

    @Override
    public boolean canGetByte() {
        return lastProperty.canGetByte();
    }

    @Override
    public byte getByte(final ObjectT bean) {
        if (!canGetByte()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getByte(getLastTargetObject(bean));
    }


    @Override
    public boolean canGetChar() {
        return lastProperty.canGetChar();
    }

    @Override
    public char getChar(final ObjectT bean) {
        if (!canGetChar()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getChar(getLastTargetObject(bean));
    }


    @Override
    public boolean canGetShort() {
        return lastProperty.canGetShort();
    }

    @Override
    public short getShort(final ObjectT bean) {
        if (!canGetShort()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getShort(getLastTargetObject(bean));
    }


    @Override
    public boolean canGetInt() {
        return lastProperty.canGetInt();
    }

    @Override
    public int getInt(final ObjectT bean) {
        if (!canGetInt()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getInt(getLastTargetObject(bean));
    }

    @Override
    public boolean canGetLong() {
        return lastProperty.canGetLong();
    }

    @Override
    public long getLong(final ObjectT bean) {
        if (!canGetLong()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getLong(getLastTargetObject(bean));
    }

    @Override
    public boolean canGetFloat() {
        return lastProperty.canGetFloat();
    }

    @Override
    public float getFloat(final ObjectT bean) {
        if (!canGetFloat()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getFloat(getLastTargetObject(bean));
    }

    @Override
    public boolean canGetDouble() {
        return lastProperty.canGetDouble();
    }

    @Override
    public double getDouble(final ObjectT bean) {
        if (!canGetDouble()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getDouble(getLastTargetObject(bean));
    }

    @Override
    public boolean canGetBoolean() {
        return lastProperty.canGetBoolean();
    }

    @Override
    public boolean getBoolean(final ObjectT bean) {
        if (!canGetBoolean()) {
            throw new UnsupportedOperationException("not implemented");
        }
        return lastProperty.castTargetType().getBoolean(getLastTargetObject(bean));
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        lastProperty.castTargetType().set(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetByte() {
        return lastProperty.canSetByte();
    }

    @Override
    public void setByte(final ObjectT bean, final byte value) {
        if (!canSetByte()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setByte(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetChar() {
        return lastProperty.canSetChar();
    }

    @Override
    public void setChar(final ObjectT bean, final char value) {
        if (!canSetChar()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setChar(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetShort() {
        return lastProperty.canSetShort();
    }

    @Override
    public void setShort(final ObjectT bean, final short value) {
        if (!canSetShort()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setShort(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetInt() {
        return lastProperty.canSetInt();
    }

    @Override
    public void setInt(final ObjectT bean, final int value) {
        if (!canSetInt()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setInt(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetLong() {
        return lastProperty.canSetLong();
    }

    @Override
    public void setLong(final ObjectT bean, final long value) {
        if (!canSetLong()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setLong(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetFloat() {
        return lastProperty.canSetFloat();
    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {
        if (!canSetFloat()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setFloat(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetDouble() {
        return lastProperty.canSetDouble();
    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {
        if (!canSetDouble()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setDouble(getLastTargetObject(bean), value);
    }

    @Override
    public boolean canSetBoolean() {
        return lastProperty.canSetBoolean();
    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {
        if (!canSetBoolean()) {
            throw new UnsupportedOperationException("not implemented");
        }
        lastProperty.castTargetType().setBoolean(getLastTargetObject(bean), value);
    }

    private Object getLastTargetObject(final Object bean) {
        Object result = bean;
        for (int i = 0; i < chain.size() - 1 && result != null; i++) {
            result = chain.get(i).castTargetType().get(result);
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

    /**
     * Get the chain of {@link ObjectProperty} in the order of access
     *
     * @return the chain of {@link ObjectProperty} in the order of access
     */
    @Override
    public List<ObjectProperty<?>> getDependents() {
        return chain;
    }
}
