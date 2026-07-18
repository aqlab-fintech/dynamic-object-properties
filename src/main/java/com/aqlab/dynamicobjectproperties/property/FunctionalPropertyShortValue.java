package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyShortValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive short
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        short get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive short
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, short value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    /**
     * The boxed value returned by {@link #get(Object)} when the bean is null.
     * Kept as a constant so every call returns the same instance (identity-consistent
     * with {@link #getShort(Object)} which returns the primitive {@code 0}).
     */
    private static final Short NULL_BEAN_VALUE = Short.valueOf((short) 0);

    protected FunctionalPropertyShortValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, Short.TYPE, uniqueIdentifier, getter != null, setter != null);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public <ValueT> ValueT get(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return (ValueT) NULL_BEAN_VALUE;
        }
        final Object result = getter.get(bean);
        return (ValueT) result;
    }

    @Override
    public boolean canGetShort() {
        return isReadable();
    }

    @Override
    public short getShort(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setShort(bean, (short) value);
    }

    @Override
    public boolean canSetShort() {
        return isWritable();
    }

    @Override
    public void setShort(final ObjectT bean, final short value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
