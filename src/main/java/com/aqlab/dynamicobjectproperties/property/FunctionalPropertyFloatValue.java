package com.aqlab.dynamicobjectproperties.property;

public class FunctionalPropertyFloatValue<ObjectT> extends FunctionalProperty<ObjectT> {
    /**
     * A functional interface representing a getter returning primitive float
     *
     * @param <T> property target type
     */
    public interface Getter<T> {
        float get(T t);
    }

    /**
     * A functional interface representing a setter accepting primitive float
     *
     * @param <T> property target type
     */
    public interface Setter<T> {
        void set(T t, float value);
    }

    private final Getter<ObjectT> getter;
    private final Setter<ObjectT> setter;

    protected FunctionalPropertyFloatValue(final Class<?> objectType, final String uniqueIdentifier, final Getter<ObjectT> getter, final Setter<ObjectT> setter) {
        super(objectType, float.class, uniqueIdentifier, getter != null, setter != null);
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
    public boolean canGetFloat() {
        return isReadable();
    }

    @Override
    public float getFloat(final ObjectT bean) {
        throwIfNotAccessible(bean, true);
        if (bean == null) {
            return 0f;
        }
        return getter.get(bean);
    }

    @Override
    public void set(final ObjectT bean, final Object value) {
        setFloat(bean, (float) value);
    }

    @Override
    public boolean canSetFloat() {
        return isWritable();
    }

    @Override
    public void setFloat(final ObjectT bean, final float value) {
        throwIfNotAccessible(bean, false);
        setter.set(bean, value);
    }
}
