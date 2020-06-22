package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.FunctionalProperty;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class FunctionalPropertyFactory {

    public static final FunctionalPropertyFactory INSTANCE = new FunctionalPropertyFactory();

    public static <T, V> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Function<T, V> getter, final BiConsumer<T, V> setter) {
        return new FunctionalPropertyObjectValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyBooleanValue.BooleanGetter<T> getter, final FunctionalPropertyBooleanValue.BooleanSetter<T> setter) {
        return new FunctionalPropertyBooleanValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyByteValue.ByteGetter<T> getter, final FunctionalPropertyByteValue.ByteSetter<T> setter) {
        return new FunctionalPropertyByteValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyDoubleValue.DoubleGetter<T> getter, final FunctionalPropertyDoubleValue.DoubleSetter<T> setter) {
        return new FunctionalPropertyDoubleValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyFloatValue.FloatGetter<T> getter, final FunctionalPropertyFloatValue.FloatSetter<T> setter) {
        return new FunctionalPropertyFloatValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyIntValue.IntGetter<T> getter, final FunctionalPropertyIntValue.IntSetter<T> setter) {
        return new FunctionalPropertyIntValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyLongValue.LongGetter<T> getter, final FunctionalPropertyLongValue.LongSetter<T> setter) {
        return new FunctionalPropertyLongValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    public static <T> FunctionalProperty<T> create(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyShortValue.ShortGetter<T> getter, final FunctionalPropertyShortValue.ShortSetter<T> setter) {
        return new FunctionalPropertyShortValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }
}
