package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyCharValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive char
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        char get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive char
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, char value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    protected FunctionalPropertyCharValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, char.class, uniqueIdentifier, getter != null, setter != null);
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
    public boolean canGetChar() {
        return isReadable();
    }

    @Override
    public char getChar(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setChar(bean, (char) value);
    }

    @Override
    public boolean canSetChar() {
        return isWritable();
    }

    @Override
    public void setChar(final ObjectT bean, final char value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
