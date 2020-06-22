package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class FunctionalPropertyObjectValue<ObjectT> extends FunctionalProperty<ObjectT> {

    private final Function<ObjectT, ?> getter;
    private final BiConsumer<ObjectT, Object> setter;

    FunctionalPropertyObjectValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Function<ObjectT, ?> getter, final BiConsumer<ObjectT, ?> setter) {
        super(objectType, valueType, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = (BiConsumer<ObjectT, Object>) setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        throwIfInaccessible(bean, false);
        return (ValueT) getter.apply(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        throwIfInaccessible(bean, true);
        setter.accept(bean, value);
    }
}
