package com.aqlab.fastbeanreflector.util;

import com.aqlab.fastbeanreflector.factory.BeanPropertyFactory;
import com.aqlab.fastbeanreflector.property.ObjectProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ObjectPropertyUtils {
    private ObjectPropertyUtils() {
    }

    public static <ObjectT> Map<String, Object> createPropertyMap(final ObjectT targetObject, final Collection<ObjectProperty<ObjectT>> properties) {
        return new PropertyMap<>(targetObject, properties);
    }

    public static <ObjectT> Collection<ObjectProperty<ObjectT>> diff(final ObjectT o1, final ObjectT o2, final Collection<? extends ObjectProperty<ObjectT>> properties) {
        if (o1 == o2) {
            return Collections.emptySet();
        }
        if (o1 == null || o2 == null) {
            return (Collection) properties;
        }
        return properties.stream().filter(p -> !Objects.equals(p.get(o1), p.get(o2))).collect(Collectors.toSet());
    }

    public static <ObjectT> Collection<ObjectProperty<ObjectT>> diff(final ObjectT o1, final ObjectT o2, final Class<?> objectType) {
        return diff(o1, o2, BeanPropertyFactory.INSTANCE.getAllBeanProperties(objectType));
    }
}
