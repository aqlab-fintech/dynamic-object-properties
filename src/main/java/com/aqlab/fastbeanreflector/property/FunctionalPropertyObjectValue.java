package com.aqlab.fastbeanreflector.property;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class FunctionalPropertyObjectValue<ObjectT> extends FunctionalProperty<ObjectT> {

    public interface Getter<T> {
        Object get(T t);

        static <T> Getter<T> fromFunction(final Function<T, ?> f) {
            return t -> f.apply(t);
        }
    }

    public interface Setter<T> {
        void set(T t, Object v);

        static <T, V> Setter<T> fromBiConsumer(final BiConsumer<T, V> f) {
            return (t, v) -> f.accept(t, (V) v);
        }
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    FunctionalPropertyObjectValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
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
        return (ValueT) getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
