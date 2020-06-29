package com.aqlab.fastbeanreflector.property;

public final class FunctionalPropertyDoubleValue<ObjectT> extends FunctionalProperty<ObjectT> {

    public interface Getter<T> {
        double get(T t);
    }

    public interface Setter<T> {
        void set(T t, double v);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    FunctionalPropertyDoubleValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
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
    public double getDouble(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0d;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setDouble(bean, (double) value);
    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
