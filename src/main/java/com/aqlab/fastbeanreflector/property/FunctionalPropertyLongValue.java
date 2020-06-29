package com.aqlab.fastbeanreflector.property;

public final class FunctionalPropertyLongValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface Getter<T> {
        long get(T t);
    }

    public interface Setter<T> {
        void set(T t, long v);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    FunctionalPropertyLongValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
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
    public long getLong(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0l;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setLong(bean, (long) value);
    }

    @Override
    public void setLong(final ObjectT bean, final long value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
