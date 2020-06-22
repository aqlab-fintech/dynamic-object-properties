package com.aqlab.fastbeanreflector.property;

public class SelfProperty extends AbstractObjectProperty<Object> {
    public static final SelfProperty INSTANCE = new SelfProperty();

    private SelfProperty() {
        super(Object.class, Object.class, "_identity");
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
