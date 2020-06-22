package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

public final class FunctionalPropertyFloatValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface FloatGetter<T> {
        float applyAsFloat(T t);
    }

    public interface FloatSetter<T> {
        void accept(T t, float value);
    }

    private final FloatGetter<ObjectT> getter;
    private final FloatSetter<ObjectT> setter;

    FunctionalPropertyFloatValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FloatGetter<ObjectT> getter, final FloatSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getFloat(bean);
        return (ValueT) result;
    }

    @Override
    public float getFloat(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return getter.applyAsFloat(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setFloat(bean, (Float) value);
    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
