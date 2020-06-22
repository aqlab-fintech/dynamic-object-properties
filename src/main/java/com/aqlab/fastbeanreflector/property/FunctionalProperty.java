package com.aqlab.fastbeanreflector.property;

public abstract class FunctionalProperty<ObjectT> extends AbstractObjectProperty<ObjectT> {

    private final boolean readable;
    private final boolean writable;

    public FunctionalProperty(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier, final boolean readable, final boolean writable) {
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

    protected void throwIfInaccessible(final ObjectT bean, final boolean readable) {
        if (bean == null) {
            throw new NullPointerException("bean");
        }

        final boolean accessible;
        final String accessName;
        if (readable) {
            accessible = isReadable();
            accessName = "readable";
        } else {
            accessible = isWritable();
            accessName = "writable";
        }
        if (!accessible) {
            throw new UnsupportedOperationException(String.format("'%s' of '%s' is not %s", getUniqueIdentifier(), getObjectType().getName(), accessName));
        }
    }
}
