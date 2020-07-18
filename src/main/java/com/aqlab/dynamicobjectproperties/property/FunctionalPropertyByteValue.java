package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyByteValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive byte
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        byte get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive byte
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, byte value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    protected FunctionalPropertyByteValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, Byte.TYPE, uniqueIdentifier, getter != null, setter != null);
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
    public boolean canGetByte() {
        return isReadable();
    }

    @Override
    public byte getByte(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setByte(bean, (byte) value);
    }

    @Override
    public boolean canSetByte() {
        return isWritable();
    }

    @Override
    public void setByte(final ObjectT bean, final byte value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
