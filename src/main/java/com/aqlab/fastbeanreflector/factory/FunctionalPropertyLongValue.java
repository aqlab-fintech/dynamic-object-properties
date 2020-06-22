package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

import java.util.function.ObjLongConsumer;
import java.util.function.ToLongFunction;

public final class FunctionalPropertyLongValue<ObjectT> extends FunctionalProperty<ObjectT> {
    public interface LongGetter<T> extends ToLongFunction<T> {
    }

    public interface LongSetter<T> extends ObjLongConsumer<T> {
    }

    private final LongGetter<ObjectT> getter;
    private final LongSetter<ObjectT> setter;

    FunctionalPropertyLongValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final LongGetter<ObjectT> getter, final LongSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getLong(bean);
        return (ValueT) result;
    }

    @Override
    public long getLong(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return getter.applyAsLong(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setLong(bean, (Long) value);
    }

    @Override
    public void setLong(final ObjectT bean, final long value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
