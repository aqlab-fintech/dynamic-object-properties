package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyIntValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive int
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        int get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive int
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, int value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    protected FunctionalPropertyIntValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, int.class, uniqueIdentifier, getter != null, setter != null);
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
    public boolean canGetInt() {
        return isReadable();
    }

    @Override
    public int getInt(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setInt(bean, (int) value);
    }

    @Override
    public boolean canSetInt() {
        return isWritable();
    }

    @Override
    public void setInt(final ObjectT bean, final int value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
