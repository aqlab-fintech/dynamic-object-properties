package com.aqlab.dynamicobjectproperties.property;

/**
 * A BeanProperty implementation backed by MethodHandle delegates.
 * This is the Java equivalent of C# LINQ Expression.Compile() —
 * no code generation, no class loading, just JVM method handles.
 *
 * @param <ObjectT> the target object type
 */
final class MethodHandleBeanProperty<ObjectT> extends BeanProperty<ObjectT> {

    MethodHandleBeanProperty(
            final Class<?> objectType,
            final Class<?> valueType,
            final String propertyName,
            final boolean readable,
            final boolean writable,
            final FunctionalProperty<ObjectT> delegate) {
        super(delegate, propertyName);
    }
}
