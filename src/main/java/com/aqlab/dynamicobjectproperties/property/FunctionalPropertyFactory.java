package com.aqlab.dynamicobjectproperties.property;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The factory of {@link com.aqlab.dynamicobjectproperties.property.FunctionalProperty}.
 */
public final class FunctionalPropertyFactory {
    /**
     * The singleton instance
     */
    public static final FunctionalPropertyFactory INSTANCE = new FunctionalPropertyFactory();

    /**
     * Create a {@link FunctionalProperty} with non-primitive value type
     *
     * @param objectType       the target object type
     * @param valueType        the expected value type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyObjectValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyObjectValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final FunctionalPropertyObjectValue.Getter<T> getter, final FunctionalPropertyObjectValue.Setter<T> setter) {
        return new FunctionalPropertyObjectValue<>(objectType, valueType, uniqueIdentifier, getter, setter);
    }

    /**
     * Create a {@link FunctionalProperty} with non-primitive value type
     *
     * @param objectType       the target object type
     * @param valueType        the expected value type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @param <V>              the expected value type generic argument
     * @return an instance of {@link FunctionalPropertyObjectValue}
     */
    public static <T, V> FunctionalProperty<T> createFunctionalPropertyObjectValue(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final Function<T, V> getter, final BiConsumer<T, V> setter) {
        return new FunctionalPropertyObjectValue<>(objectType, valueType, uniqueIdentifier, getter == null ? null : FunctionalPropertyObjectValue.Getter.fromFunction(getter), setter == null ? null : FunctionalPropertyObjectValue.Setter.fromBiConsumer(setter));
    }

    /**
     * Create a {@link FunctionalProperty} with boolean value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyBooleanValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyBooleanValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyBooleanValue.Getter<T> getter, final FunctionalPropertyBooleanValue.Setter<T> setter) {
        return new FunctionalPropertyBooleanValue<>(objectType, uniqueIdentifier, getter, setter);
    }

    /**
     * Create a {@link FunctionalProperty} with byte value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyByteValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyByteValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyByteValue.Getter<T> getter, final FunctionalPropertyByteValue.Setter<T> setter) {
        return new FunctionalPropertyByteValue<>(objectType, uniqueIdentifier, getter, setter);
    }

    /**
     * Create a {@link FunctionalProperty} with char value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyCharValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyCharValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyCharValue.Getter<T> getter, final FunctionalPropertyCharValue.Setter<T> setter) {
        return new FunctionalPropertyCharValue<>(objectType, uniqueIdentifier, getter, setter);
    }

    /**
     * Create a {@link FunctionalProperty} with double value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyDoubleValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyDoubleValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyDoubleValue.Getter<T> getter, final FunctionalPropertyDoubleValue.Setter<T> setter) {
        return new FunctionalPropertyDoubleValue<>(objectType, uniqueIdentifier, getter, setter);
    }


    /**
     * Create a {@link FunctionalProperty} with float value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyFloatValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyFloatValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyFloatValue.Getter<T> getter, final FunctionalPropertyFloatValue.Setter<T> setter) {
        return new FunctionalPropertyFloatValue<>(objectType, uniqueIdentifier, getter, setter);
    }


    /**
     * Create a {@link FunctionalProperty} with int value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyIntValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyIntValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyIntValue.Getter<T> getter, final FunctionalPropertyIntValue.Setter<T> setter) {
        return new FunctionalPropertyIntValue<>(objectType, uniqueIdentifier, getter, setter);
    }


    /**
     * Create a {@link FunctionalProperty} with long value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyLongValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyLongValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyLongValue.Getter<T> getter, final FunctionalPropertyLongValue.Setter<T> setter) {
        return new FunctionalPropertyLongValue<>(objectType, uniqueIdentifier, getter, setter);
    }


    /**
     * Create a {@link FunctionalProperty} with short value
     *
     * @param objectType       the target object type
     * @param uniqueIdentifier a unique identifier. Refer to {@link ObjectProperty#getUniqueIdentifier} for details.
     * @param getter           the getter. Null if the property is not readable.
     * @param setter           the setter. Null if the property is not writable.
     * @param <T>              the target object type generic argument
     * @return an instance of {@link FunctionalPropertyShortValue}
     */
    public static <T> FunctionalProperty<T> createFunctionalPropertyShortValue(final Class<?> objectType, final String uniqueIdentifier, final FunctionalPropertyShortValue.Getter<T> getter, final FunctionalPropertyShortValue.Setter<T> setter) {
        return new FunctionalPropertyShortValue<>(objectType, uniqueIdentifier, getter, setter);
    }
}
