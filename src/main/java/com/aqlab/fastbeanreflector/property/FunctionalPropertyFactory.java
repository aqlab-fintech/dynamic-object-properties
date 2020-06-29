package com.aqlab.fastbeanreflector.property;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class FunctionalPropertyFactory {

    public static final FunctionalPropertyFactory INSTANCE = new FunctionalPropertyFactory();

    public static <T, V> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Function<T, V> getter, final BiConsumer<T, V> setter) {
        return new FunctionalPropertyObjectValue<>(objectType, valueType, uniqueIdentifier, FunctionalPropertyObjectValue.Getter.fromFunction(getter), FunctionalPropertyObjectValue.Setter.fromBiConsumer(setter));
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyBooleanValue.Getter<T> getter, final FunctionalPropertyBooleanValue.Setter<T> setter) {
        return new FunctionalPropertyBooleanValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyByteValue.Getter<T> getter, final FunctionalPropertyByteValue.Setter<T> setter) {
        return new FunctionalPropertyByteValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyDoubleValue.Getter<T> getter, final FunctionalPropertyDoubleValue.Setter<T> setter) {
        return new FunctionalPropertyDoubleValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyFloatValue.Getter<T> getter, final FunctionalPropertyFloatValue.Setter<T> setter) {
        return new FunctionalPropertyFloatValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyIntValue.Getter<T> getter, final FunctionalPropertyIntValue.Setter<T> setter) {
        return new FunctionalPropertyIntValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyLongValue.Getter<T> getter, final FunctionalPropertyLongValue.Setter<T> setter) {
        return new FunctionalPropertyLongValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyShortValue.Getter<T> getter, final FunctionalPropertyShortValue.Setter<T> setter) {
        return new FunctionalPropertyShortValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }
}
