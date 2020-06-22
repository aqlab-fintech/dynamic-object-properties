package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

public final class FunctionalPropertyByteValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface ByteGetter<T> {
        byte applyAsByte(T t);
    }

    public interface ByteSetter<T> {
        void accept(T t, byte value);
    }

    private final ByteGetter<ObjectT> getter;
    private final ByteSetter<ObjectT> setter;

    FunctionalPropertyByteValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final ByteGetter<ObjectT> getter, final ByteSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getByte(bean);
        return (ValueT) result;
    }

    @Override
    public byte getByte(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return getter.applyAsByte(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setByte(bean, (Byte) value);
    }

    @Override
    public void setByte(final ObjectT bean, final byte value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
