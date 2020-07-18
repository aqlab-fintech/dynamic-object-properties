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

    protected FunctionalPropertyShortValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, short.class, uniqueIdentifier, getter != null, setter != null);
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
