package com.aqlab.fastbeanreflector.property;

public abstract class FunctionalProperty<ObjectT> extends AbstractObjectProperty<ObjectT> {

    private final boolean readable;
    private final boolean writable;

    FunctionalProperty(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final boolean readable, final boolean writable) {
        super(objectType, valueType, uniqueIdentifier);
        this.readable = readable;
        this.writable = writable;
    }

    @Override
    public boolean isReadable() {
        return readable;
    }

    @Override
    public boolean isWritable() {
        return writable;
    }
}
