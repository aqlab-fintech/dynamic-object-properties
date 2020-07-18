package com.aqlab.dynamicobjectproperties.property;

/**
 * This represents a property that is defined by custom getter and setter implementation.
 * <p>
 * It is an easy way to define derived properties. E.g.
 * <ul>
 *     <li>Format a double property value to string</li>
 *     <li>Sum of 2 int properties</li>
 * </ul>
 * <p>
 * This has to be created via {@link #FACTORY}.
 *
 * @param <ObjectT> the target object type
 */
public abstract class FunctionalProperty<ObjectT> extends AbstractObjectProperty<ObjectT> {
    /**
     * The singleton instance of the {@link com.aqlab.dynamicobjectproperties.property.FunctionalPropertyFactory}.
     */
    public static final FunctionalPropertyFactory FACTORY = FunctionalPropertyFactory.INSTANCE;

    private final boolean readable;
    private final boolean writable;

    FunctionalProperty(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final boolean readable, final boolean writable) {
        super(objectType, valueType, uniqueIdentifier);
        this.readable = readable;
        this.writable = writable;
    }

    @Override
    public boolean isReadable() {
        return readable;
    }

    @Override
    public boolean isWritable() {
        return writable;
    }
}
