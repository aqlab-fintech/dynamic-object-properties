package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyLongValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive long
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        long get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive long
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, long v);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    protected FunctionalPropertyLongValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, long.class, uniqueIdentifier, getter != null, setter != null);
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
    public boolean canGetLong() {
        return isReadable();
    }

    @Override
    public long getLong(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0l;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setLong(bean, (long) value);
    }

    @Override
    public boolean canSetLong() {
        return isWritable();
    }

    @Override
    public void setLong(final ObjectT bean, final long value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
