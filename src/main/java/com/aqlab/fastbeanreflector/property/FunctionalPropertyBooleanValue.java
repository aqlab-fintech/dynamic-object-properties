package com.aqlab.fastbeanreflector.property;

public final class FunctionalPropertyBooleanValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface Getter<T> {
        boolean get(T t);
    }

    public interface Setter<T> {
        void set(T t, boolean value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    FunctionalPropertyBooleanValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
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
    public boolean getBoolean(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return true;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setBoolean(bean, (boolean) value);
    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
