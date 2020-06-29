package com.aqlab.fastbeanreflector.property;

public final class FunctionalPropertyFloatValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface Getter<T> {
        float get(T t);
    }

    public interface Setter<T> {
        void set(T t, float value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    FunctionalPropertyFloatValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
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
    public float getFloat(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0f;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setFloat(bean, (float) value);
    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
