package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

public final class FunctionalPropertyBooleanValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface BooleanGetter<T> {
        boolean applyAsBoolean(T t);
    }

    public interface BooleanSetter<T> {
        void accept(T t, boolean value);
    }

    private final BooleanGetter<ObjectT> getter;
    private final BooleanSetter<ObjectT> setter;

    FunctionalPropertyBooleanValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final BooleanGetter<ObjectT> getter, final BooleanSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getBoolean(bean);
        return (ValueT) result;
    }

    @Override
    public boolean getBoolean(final ObjectT bean) {
        throwIfInaccessible(bean, true);
        return getter.applyAsBoolean(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setBoolean(bean, (Boolean) value);
    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {
        throwIfInaccessible(bean, false);
        setter.accept(bean, value);
    }
}
