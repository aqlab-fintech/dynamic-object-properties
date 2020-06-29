package com.aqlab.fastbeanreflector.property;

public final class FunctionalPropertyCharValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface Getter<T> {
        char get(T t);
    }

    public interface Setter<T> {
        void set(T t, char value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    FunctionalPropertyCharValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return null;
        }
        final Object result = getter.get(bean);
        return (ValueT) result;
    }

    @Override
    public char getChar(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setChar(bean, (char) value);
    }

    @Override
    public void setChar(final ObjectT bean, final char value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
