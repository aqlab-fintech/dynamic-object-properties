package com.aqlab.dynamicobjectproperties.property;

import java.beans.PropertyDescriptor;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates {@link BeanProperty} backed by LambdaMetafactory-generated lambdas.
 *
 * <p>This is the Java equivalent of C# LINQ Expression.Compile():
 * LambdaMetafactory generates real JVM bytecode at runtime, not adapter wrappers.
 * After JIT warmup, performance approaches direct field access.
 */
final class BeanPropertyMethodHandleGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanPropertyMethodHandleGenerator.class);

    private final Class<?> objectType;
    private final PropertyDescriptor propertyDescriptor;

    BeanPropertyMethodHandleGenerator(final Class<?> objectType, final PropertyDescriptor pd) {
        this.objectType = Objects.requireNonNull(objectType);
        this.propertyDescriptor = Objects.requireNonNull(pd);
    }

    /**
     * Create a FunctionalProperty delegate backed by LambdaMetafactory-generated lambdas.
     */
    @SuppressWarnings("unchecked")
    <ObjectT> FunctionalProperty<ObjectT> createDelegate() {
        final String propertyName = propertyDescriptor.getName();
        final Method getter = propertyDescriptor.getReadMethod();
        final Method setter = propertyDescriptor.getWriteMethod();
        final Class<?> valueType = propertyDescriptor.getPropertyType();
        final Class<ObjectT> objType = (Class<ObjectT>) objectType;

        final boolean readable = getter != null;
        final boolean writable = setter != null;

        if (valueType.isPrimitive()) {
            return createPrimitiveDelegate(propertyName, valueType, objType, getter, setter);
        }

        final java.util.function.Function<ObjectT, Object> getterFn = getter != null
                ? createGetterLambda(objType, getter) : null;
        final java.util.function.BiConsumer<ObjectT, Object> setterFn = setter != null
                ? createSetterLambda(objType, setter) : null;

        return FunctionalPropertyFactory.INSTANCE.createFunctionalPropertyObjectValue(
                objectType, valueType, propertyName, getterFn, setterFn
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <ObjectT> FunctionalProperty<ObjectT> createPrimitiveDelegate(
            final String propertyName,
            final Class<?> valueType,
            final Class<ObjectT> objType,
            final Method getter,
            final Method setter) {

        final String name = valueType.getName();

        // Adapt getter handle to accept Object bean, return boxed primitive
        final MethodHandle adaptedGetter = getter != null
                ? adaptGetterForPrimitive(getter, valueType, objType) : null;
        final MethodHandle adaptedSetter = setter != null
                ? adaptSetterForPrimitive(setter, valueType, objType) : null;

        switch (name) {
            case "boolean": {
                final FunctionalPropertyBooleanValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (boolean) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyBooleanValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyBooleanValue(objectType, propertyName, gFn, sFn);
            }
            case "byte": {
                final FunctionalPropertyByteValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (byte) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyByteValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyByteValue(objectType, propertyName, gFn, sFn);
            }
            case "char": {
                final FunctionalPropertyCharValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (char) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyCharValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyCharValue(objectType, propertyName, gFn, sFn);
            }
            case "short": {
                final FunctionalPropertyShortValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (short) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyShortValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyShortValue(objectType, propertyName, gFn, sFn);
            }
            case "int": {
                final FunctionalPropertyIntValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (int) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyIntValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyIntValue(objectType, propertyName, gFn, sFn);
            }
            case "long": {
                final FunctionalPropertyLongValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (long) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyLongValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyLongValue(objectType, propertyName, gFn, sFn);
            }
            case "float": {
                final FunctionalPropertyFloatValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (float) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyFloatValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyFloatValue(objectType, propertyName, gFn, sFn);
            }
            case "double": {
                final FunctionalPropertyDoubleValue.Getter<ObjectT> gFn = adaptedGetter != null
                        ? bean -> { try { return (double) adaptedGetter.invoke(bean); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                final FunctionalPropertyDoubleValue.Setter<ObjectT> sFn = adaptedSetter != null
                        ? (bean, val) -> { try { adaptedSetter.invoke(bean, val); } catch (Throwable t) { throw new RuntimeException(t); } }
                        : null;
                return (FunctionalProperty<ObjectT>) FunctionalPropertyFactory.INSTANCE
                        .createFunctionalPropertyDoubleValue(objectType, propertyName, gFn, sFn);
            }
            default:
                throw new IllegalArgumentException("Unsupported primitive type: " + valueType);
        }
    }

    private MethodHandle adaptGetterForPrimitive(Method getter, Class<?> primitiveType, Class<?> objType) {
        try {
            MethodHandle mh = MethodHandles.lookup().unreflect(getter);
            // Adapt to accept Object, return primitive (auto-boxing on return from invoke())
            MethodType adaptedType = MethodType.methodType(primitiveType, Object.class);
            return mh.asType(adaptedType);
        } catch (Exception e) {
            LOGGER.warn("Failed to adapt getter for primitive: {}", e.getMessage());
            return null;
        }
    }

    private MethodHandle adaptSetterForPrimitive(Method setter, Class<?> primitiveType, Class<?> objType) {
        try {
            MethodHandle mh = MethodHandles.lookup().unreflect(setter);
            // Adapt to accept (Object, Object) where second param is unboxed to primitive
            MethodType adaptedType = MethodType.methodType(void.class, Object.class, Object.class);
            return mh.asType(adaptedType);
        } catch (Exception e) {
            LOGGER.warn("Failed to adapt setter for primitive: {}", e.getMessage());
            return null;
        }
    }

    private <ObjectT> java.util.function.Function<ObjectT, Object> createGetterLambda(
            final Class<ObjectT> objType, final Method getter) {

        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodHandle getterHandle = lookup.unreflect(getter);
            final Class<?> returnType = getter.getReturnType();

            final MethodType erasedType = MethodType.methodType(Object.class, Object.class);
            final MethodType instantiatedType = MethodType.methodType(returnType, objType);

            final CallSite callSite = LambdaMetafactory.metafactory(
                    lookup,
                    "apply",
                    MethodType.methodType(java.util.function.Function.class),
                    erasedType,
                    getterHandle,
                    instantiatedType
            );

            @SuppressWarnings("unchecked")
            final java.util.function.Function<ObjectT, Object> fn =
                    (java.util.function.Function<ObjectT, Object>) callSite.getTarget().invokeExact();
            return fn;

        } catch (final Throwable e) {
            LOGGER.warn("LambdaMetafactory getter failed for {}.{}: {}",
                    objectType.getSimpleName(), getter.getName(), e.getMessage());
            return null;
        }
    }

    private <ObjectT> java.util.function.BiConsumer<ObjectT, Object> createSetterLambda(
            final Class<ObjectT> objType, final Method setter) {

        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodHandle setterHandle = lookup.unreflect(setter);
            final Class<?> paramType = setter.getParameterTypes()[0];

            if (paramType.isPrimitive()) {
                return (bean, value) -> {
                    try {
                        MethodType targetType = setterHandle.type();
                        MethodType adaptedType = MethodType.methodType(targetType.returnType(), Object.class, paramType);
                        MethodHandle adaptedHandle = setterHandle.asType(adaptedType);

                        switch (paramType.getName()) {
                            case "boolean": adaptedHandle.invokeExact(bean, ((Boolean) value).booleanValue()); break;
                            case "byte":    adaptedHandle.invokeExact(bean, ((Byte) value).byteValue()); break;
                            case "char":    adaptedHandle.invokeExact(bean, ((Character) value).charValue()); break;
                            case "short":   adaptedHandle.invokeExact(bean, ((Short) value).shortValue()); break;
                            case "int":     adaptedHandle.invokeExact(bean, ((Integer) value).intValue()); break;
                            case "long":    adaptedHandle.invokeExact(bean, ((Long) value).longValue()); break;
                            case "float":   adaptedHandle.invokeExact(bean, ((Float) value).floatValue()); break;
                            case "double":  adaptedHandle.invokeExact(bean, ((Double) value).doubleValue()); break;
                            default:        adaptedHandle.invokeExact(bean, value); break;
                        }
                    } catch (final Throwable t) {
                        throw new RuntimeException(t);
                    }
                };
            }

            final MethodType erasedType = MethodType.methodType(void.class, Object.class, Object.class);
            final MethodType instantiatedType = MethodType.methodType(void.class, objType, paramType);

            final CallSite callSite = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(java.util.function.BiConsumer.class),
                    erasedType,
                    setterHandle,
                    instantiatedType
            );

            @SuppressWarnings("unchecked")
            final java.util.function.BiConsumer<ObjectT, Object> fn =
                    (java.util.function.BiConsumer<ObjectT, Object>) callSite.getTarget().invokeExact();
            return fn;

        } catch (final Throwable e) {
            LOGGER.warn("LambdaMetafactory setter failed for {}.{}: {}",
                    objectType.getSimpleName(), setter.getName(), e.getMessage());
            return null;
        }
    }
}
