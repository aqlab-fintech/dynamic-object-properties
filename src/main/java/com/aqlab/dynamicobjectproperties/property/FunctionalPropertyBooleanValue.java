package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyBooleanValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive boolean
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        boolean get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive boolean
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, boolean value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    protected FunctionalPropertyBooleanValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, Boolean.TYPE, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return null;
        }
        final Object result = getter.get(bean);
        return (ValueT) result;
    }

    @Override
    public boolean canGetBoolean() {
        return isReadable();
    }

    @Override
    public boolean getBoolean(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return true;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setBoolean(bean, (boolean) value);
    }

    @Override
    public boolean canSetBoolean() {
        return isWritable();
    }

    @Override
    public void setBoolean(final ObjectT bean, final boolean value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
