package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

import java.util.function.ObjDoubleConsumer;
import java.util.function.ToDoubleFunction;

public final class FunctionalPropertyDoubleValue<ObjectT> extends FunctionalProperty<ObjectT> {

    public interface DoubleGetter<T> extends ToDoubleFunction<T> {
    }

    public interface DoubleSetter<T> extends ObjDoubleConsumer<T> {
    }

    private final DoubleGetter<ObjectT> getter;
    private final DoubleSetter<ObjectT> setter;

    FunctionalPropertyDoubleValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final DoubleGetter<ObjectT> getter, final DoubleSetter<ObjectT> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        final Object result = getDouble(bean);
        return (ValueT) result;
    }

    @Override
    public double getDouble(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return getter.applyAsDouble(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setDouble(bean, (Double) value);
    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
