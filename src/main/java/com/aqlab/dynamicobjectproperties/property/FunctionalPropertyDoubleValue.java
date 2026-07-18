package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyDoubleValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive double
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        double get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive double
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, double v);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    /**
     * The boxed value returned by {@link #get(Object)} when the bean is null.
     * <p>
     * {@link Double#valueOf(double)} does NOT cache instances (unlike the integer box types),
     * so every call would allocate a new Double. Keeping a constant guarantees every call
     * returns the same instance and avoids the allocation. It is also identity-consistent
     * with {@link #getDouble(Object)} which returns the primitive {@code 0d}.
     */
    private static final Double NULL_BEAN_VALUE = Double.valueOf(0d);

    protected FunctionalPropertyDoubleValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, Double.TYPE, uniqueIdentifier, getter != null, setter != null);
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
    public boolean canGetDouble() {
        return isReadable();
    }

    @Override
    public double getDouble(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0d;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setDouble(bean, (double) value);
    }

    @Override
    public boolean canSetDouble() {
        return isWritable();
    }

    @Override
    public void setDouble(final ObjectT bean, final double value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
