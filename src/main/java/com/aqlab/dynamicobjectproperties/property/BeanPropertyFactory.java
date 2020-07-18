package com.aqlab.dynamicobjectproperties.property;

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


/**
 * The factory of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty}.
 * <p>
 * Instances are created using code generation. This means
 * <ul>
 *     <li>This is VM-independent (as long as the VM is supported by <a href="https://github.com/OpenHFT/Java-Runtime-Compiler">OpenHFT Java Compiler</a> library.</li>
 *     <li>There is an overhead when an instance is first created. The factory caches {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} instances.
 *     The same instance is always returned for the same property name of the same bean class.</li>
 * </ul>
 */
public class BeanPropertyFactory {
    /**
     * The singleton instance
     */
    public static final BeanPropertyFactory INSTANCE = new BeanPropertyFactory();

    private Map<Class<?>, Map<String, BeanProperty<?>>> propertiesByClass = new ConcurrentHashMap<>();

    private BeanPropertyFactory() {
    }

    /**
     * Create an instance of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} if not already created. Return a cached instance otherwise.
     *
     * @param objectType   the target object type
     * @param propertyName the bean property name
     * @param <ObjectT>    the target object type as a type parameter
     * @return the specified bean property
     */
    public <ObjectT> BeanProperty<ObjectT> getBeanProperty(final Class<?> objectType, final String propertyName) {
        final Collection<BeanProperty<ObjectT>> result = getBeanProperties(objectType, new String[]{propertyName});
        if (result.isEmpty()) {
            return null;
        } else {
            return result.iterator().next();
        }
    }

    /**
     * Create instances of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} for all bean properties.
     * <p>
     * Bean properties are discovered using {@link java.beans.Introspector java.beans.Introspector}.
     *
     * @param objectType the target object type
     * @param <ObjectT>  the target object type as a type parameter
     * @return a collection of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} as discovered
     */
    public <ObjectT> Collection<BeanProperty<ObjectT>> getAllBeanProperties(final Class<?> objectType) {
        return getBeanProperties(objectType, (String[]) null);
    }

    /**
     * Create instances of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} for all bean properties, excluding the "class" property.
     * <p>
     * The "class" property is defined in {@link Object#getClass()}. To only consider user-defined properties, "class" property should be excluded.
     * <p>
     * Bean properties are discovered using {@link java.beans.Introspector java.beans.Introspector}.
     *
     * @param objectType the target object type
     * @param <ObjectT>  the target object type as a type parameter
     * @return a collection of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty}
     */
    public <ObjectT> Collection<BeanProperty<ObjectT>> getAllBeanPropertiesExcludeClass(final Class<?> objectType) {
        return getAllBeanPropertiesWithFilter(objectType, p -> !"class".equals(p.getUniqueIdentifier()));
    }

    /**
     * Create instances of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} for all bean properties, excluding certain properties by a custom filter.
     * <p>
     * Bean properties are discovered using {@link java.beans.Introspector java.beans.Introspector}.
     *
     * @param objectType the target object type
     * @param filter     a filter accepting a {@link com.aqlab.dynamicobjectproperties.property.BeanProperty}
     * @param <ObjectT>  the target object type as a type parameter
     * @return a collection of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} of all properties which the filter returns true
     */
    public <ObjectT> Collection<BeanProperty<ObjectT>> getAllBeanPropertiesWithFilter(final Class<?> objectType, final Predicate<BeanProperty<ObjectT>> filter) {
        final Collection<BeanProperty<ObjectT>> allBeanProperties = getAllBeanProperties(objectType);
        return allBeanProperties.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * Create instances of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} for all bean properties specified.
     * <p>
     * If the array of property names is empty or null, all properties
     * <p>
     * Bean properties are discovered using {@link java.beans.Introspector java.beans.Introspector}.
     *
     * @param objectType    the target object type
     * @param propertyNames a list of property names
     * @param <ObjectT>     the target object type as a type parameter
     * @return a collection of {@link com.aqlab.dynamicobjectproperties.property.BeanProperty} of all properties specified by names
     */
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
