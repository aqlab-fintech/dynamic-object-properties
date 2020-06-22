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
        final Object o = getLastTargetObject(bean);
        if (o == null) {
            return null;
        }

        return lastProperty.cast().get(o);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        lastProperty.cast().set(getLastTargetObject(bean), value);
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
