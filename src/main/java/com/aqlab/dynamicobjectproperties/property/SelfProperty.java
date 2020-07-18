package com.aqlab.dynamicobjectproperties.property;

/**
 * This represents a property which the value is the target object itself.
 * <p>
 * {@link #set(Object, Object)} and the primitive getters and setters are not implemented
 */
public class SelfProperty extends AbstractObjectProperty<Object> {

    private static final SelfProperty INSTANCE = new SelfProperty();

    /**
     * Return the singleton instance, casted to the desired target object type.
     *
     * @param <T> target object type generic parameter
     * @return the singleton instance, casted
     */
    public static <T> ObjectProperty<T> getInstance() {
        return INSTANCE.castTargetType();
    }

    private SelfProperty() {
        super(Object.class, Object.class, "!IDENTITY");
    }

    @Override
    public <ValueT> ValueT get(final Object bean) {
        return (ValueT) bean;
    }

    @Override
    public boolean isReadable() {
        return true;
    }
}
