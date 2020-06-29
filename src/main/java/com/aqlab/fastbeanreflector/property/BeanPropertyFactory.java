package com.aqlab.fastbeanreflector.property;

import net.openhft.compiler.CompilerUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanPropertyFactory {
    public static final BeanPropertyFactory INSTANCE = new BeanPropertyFactory();

    private Map<Class<?>, Map<String, BeanProperty<?>>> propertiesByClass = new ConcurrentHashMap<>();

    private BeanPropertyFactory() {
    }

    public <ObjectT> BeanProperty<ObjectT> getBeanProperty(final Class<?> objectType, final String propertyName) {
        final Collection<BeanProperty<ObjectT>> result = getBeanProperties(objectType, new String[]{propertyName});
        if (result.isEmpty()) {
            return null;
        } else {
            return result.iterator().next();
        }
    }

    public <ObjectT> Collection<BeanProperty<ObjectT>> getAllBeanProperties(final Class<?> objectType) {
        return getBeanProperties(objectType, new String[0]);
    }

    public <ObjectT> Collection<BeanProperty<ObjectT>> getAllBeanPropertiesExcludeClass(final Class<?> objectType) {
        return getAllBeanPropertiesWithFilter(objectType, p -> "class".equals(p.getUniqueIdentifier()));
    }

    public <ObjectT> Collection<BeanProperty<ObjectT>> getAllBeanPropertiesWithFilter(final Class<?> objectType, final Predicate<BeanProperty<ObjectT>> filter) {
        final Collection<BeanProperty<ObjectT>> allBeanProperties = getAllBeanProperties(objectType);
        return allBeanProperties.stream().filter(filter).collect(Collectors.toList());
    }

    public <ObjectT> Collection<BeanProperty<ObjectT>> getBeanProperties(final Class<?> objectType, final String... propertyNames) {
        if (objectType == null) {
            throw new NullPointerException("objectType");
        }

        final Map<String, BeanProperty<?>> allProperties = propertiesByClass.computeIfAbsent(objectType,
                k -> {
                    final Map<String, BeanProperty<?>> propertiesMap = new LinkedHashMap<>();
                    final BeanInfo beanInfo;
                    try {
                        beanInfo = Introspector.getBeanInfo(k);
                        Stream.of(beanInfo.getPropertyDescriptors()).forEach(pd -> {
                            try {
                                registerBeanProperty(k, pd, propertiesMap);
                            } catch (final IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                                throw new RuntimeException("Unexpected error instantiating BeanProperty", e);
                            }
                        });
                        return propertiesMap;
                    } catch (final IntrospectionException e) {
                        throw new RuntimeException("Unexpected error introspecting bean class", e);
                    }

                });


        if (propertyNames == null || propertyNames.length == 0) {
            final Collection beanProperties = allProperties.values();
            return beanProperties;
        } else {
            return (Collection) Stream.of(propertyNames).map(n -> allProperties.get(n)).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    private void registerBeanProperty(final Class<?> objectType, final PropertyDescriptor pd, final Map<String, BeanProperty<?>> propertiesMap)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final BeanPropertySourceGenerator generator = new BeanPropertySourceGenerator(objectType, pd);
        generator.generateClassSource();
        final Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(generator.getTargetClassFullName(), generator.getTargetClassSource());
        propertiesMap.put(pd.getName(), (BeanProperty<?>) clazz.newInstance());
    }
}
