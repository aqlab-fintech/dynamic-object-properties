package com.aqlab.dynamicobjectproperties.util;

import com.aqlab.dynamicobjectproperties.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.List;
import java.util.ListIterator;

class PropertyList<ObjectT> extends AbstractList<Object> {

    private final ObjectT targetObject;
    private final List<ObjectProperty<ObjectT>> propertyList;

    PropertyList(final ObjectT targetObject, final List<ObjectProperty<ObjectT>> propertyList) {
        this.targetObject = targetObject;
        this.propertyList = propertyList;
    }

    @Override
    public int size() {
        return propertyList.size();
    }

    @Override
    public Object get(final int index) {
        return propertyList.get(index).get(targetObject);
    }

    @Override
    public Object set(final int index, final Object element) {
        final Object original = get(index);
        propertyList.get(index).set(targetObject, element);
        return original;
    }

    @NotNull
    @Override
    public ListIterator<Object> listIterator(final int index) {
        return new PropertyListListIterator(propertyList.listIterator(index));
    }

    private class PropertyListListIterator implements ListIterator<Object> {

        private ListIterator<ObjectProperty<ObjectT>> backingIterator;

        public PropertyListListIterator(final ListIterator<ObjectProperty<ObjectT>> backingIterator) {
            this.backingIterator = backingIterator;
        }

        @Override
        public boolean hasNext() {
            return backingIterator.hasNext();
        }

        @Override
        public Object next() {
            final ObjectProperty<ObjectT> property = backingIterator.next();
            return property.get(targetObject);
        }

        @Override
        public boolean hasPrevious() {
            return backingIterator.hasPrevious();
        }

        @Override
        public Object previous() {
            final ObjectProperty<ObjectT> property = backingIterator.previous();
            return property.get(targetObject);
        }

        @Override
        public int nextIndex() {
            return backingIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return backingIterator.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(final Object o) {
            PropertyList.this.set(nextIndex(), o);
        }

        @Override
        public void add(final Object o) {
            throw new UnsupportedOperationException();
        }
    }

    @NotNull
    @Override
    public List<Object> subList(final int fromIndex, final int toIndex) {
        return new PropertyList<>(targetObject, propertyList.subList(fromIndex, toIndex));
    }
}
