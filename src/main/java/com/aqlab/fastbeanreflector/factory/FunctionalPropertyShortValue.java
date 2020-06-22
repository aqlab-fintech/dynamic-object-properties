package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

public final class FunctionalPropertyShortValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface ShortGetter<T> {
        short applyAsShort(T t);
    }

    public interface ShortSetter<T> {
        void accept(T t, short value);
    }

    private final ShortGetter<ObjectT> getter;
    private final ShortSetter<ObjectT> setter;

    FunctionalPropertyShortValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final ShortGetter<ObjectT> getter, final ShortSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getShort(bean);
        return (ValueT) result;
    }

    @Override
    public short getShort(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return getter.applyAsShort(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setShort(bean, (Short) value);
    }

    @Override
    public void setShort(final ObjectT bean, final short value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
