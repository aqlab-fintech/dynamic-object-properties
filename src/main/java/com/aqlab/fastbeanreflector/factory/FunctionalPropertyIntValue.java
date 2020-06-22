package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

public final class FunctionalPropertyIntValue<ObjectT> extends FunctionalProperty<ObjectT> {

    public interface IntGetter<T> extends ToIntFunction<T> {
    }

    public interface IntSetter<T> extends ObjIntConsumer<T> {
    }

    private final IntGetter<ObjectT> getter;
    private final IntSetter<ObjectT> setter;

    FunctionalPropertyIntValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final IntGetter<ObjectT> getter, final IntSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getInt(bean);
        return (ValueT) result;
    }

    @Override
    public int getInt(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return getter.applyAsInt(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setInt(bean, (Integer) value);
    }

    @Override
    public void setInt(final ObjectT bean, final int value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
