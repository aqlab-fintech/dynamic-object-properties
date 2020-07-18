package com.aqlab.dynamicobjectproperties.util;

import com.aqlab.dynamicobjectproperties.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class PropertyMap<ObjectT> implements Map<String, Object> {
    private final ObjectT targetObject;
    private final Map<String, ObjectProperty<ObjectT>> propertiesMap;

    PropertyMap(final ObjectT targetObject, final Map<String, ObjectProperty<ObjectT>> propertiesMap) {
        this.targetObject = targetObject;
        this.propertiesMap = propertiesMap;
    }

    @Override
    public int size() {
        return propertiesMap.size();
    }

    @Override
    public boolean isEmpty() {
        return propertiesMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object o) {
        return propertiesMap.containsKey(o);
    }

    @Override
    public boolean containsValue(final Object o) {
        return propertiesMap.values().stream().anyMatch(p -> Objects.equals(o, p.get(targetObject)));
    }

    @Override
    public Object get(final Object o) {
        final ObjectProperty<ObjectT> p = propertiesMap.get(o);
        if (p == null) {
            return null;
        }
        return p.get(targetObject);
    }

    @Nullable
    @Override
    public Object put(final String s, final Object o) {
        final ObjectProperty<ObjectT> p = propertiesMap.get(o);
        if (p == null) {
            throw new IllegalArgumentException(String.format("%s: key does not exist", s));
        }
        final Object original = p.get(targetObject);
        p.set(targetObject, o);
        return original;
    }

    @Override
    public Object remove(final Object o) {
        throw new UnsupportedOperationException("PropertyMap does not support removal");
    }

    @Override
    public void putAll(@NotNull final Map<? extends String, ?> map) {
        for (final Entry<? extends String, ?> entry : map.entrySet()) {
            final ObjectProperty<ObjectT> p = propertiesMap.get(entry.getKey());
            if (p != null) {
                p.set(targetObject, entry.getValue());
            }
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("PropertyMap does not support clearing");
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return propertiesMap.keySet();
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return propertiesMap.values().stream().map(p -> p.get(targetObject)).collect(Collectors.toList());
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return propertiesMap.keySet().stream().map(k -> new PropertyMapEntry(k)).collect(Collectors.toSet());
    }

    private class PropertyMapEntry implements Entry<String, Object> {
        private final String propertyName;

        PropertyMapEntry(final String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String getKey() {
            return propertyName;
        }

        @Override
        public Object getValue() {
            return get(propertyName);
        }

        @Override
        public Object setValue(final Object o) {
            return put(propertyName, o);
        }
    }
}
