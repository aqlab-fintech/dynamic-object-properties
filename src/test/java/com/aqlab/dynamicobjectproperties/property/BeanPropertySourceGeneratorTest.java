package com.aqlab.dynamicobjectproperties.property;


import net.openhft.compiler.CompilerUtils;
import org.junit.Assert;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.List;

public class BeanPropertySourceGeneratorTest {
    public static class TestClass {
        private String f1;
        private int[] f2;
        private List<Long> f3;
        private byte f4;

        public String getF1() {
            return f1;
        }

        public void setF1(final String f1) {
            this.f1 = f1;
        }

        public int[] getF2() {
            return f2;
        }

        public void setF2(final int[] f2) {
            this.f2 = f2;
        }

        public List<Long> getF3() {
            return f3;
        }

        public void setF3(final List<Long> f3) {
            this.f3 = f3;
        }

        public byte getF4() {
            return f4;
        }

        public void setF4(final byte f4) {
            this.f4 = f4;
        }
    }

    @Test
    public void testObjectPropertyTypeString() throws IntrospectionException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final BeanPropertySourceGenerator generator = new BeanPropertySourceGenerator(TestClass.class, new PropertyDescriptor("f1", TestClass.class));
        generator.generateClassSource();
        final Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(generator.getTargetClassFullName(), generator.getTargetClassSource());
        Assert.assertNotNull(clazz.newInstance());
    }

    @Test
    public void testObjectPropertyTypeIntArray() throws IntrospectionException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final BeanPropertySourceGenerator generator = new BeanPropertySourceGenerator(TestClass.class, new PropertyDescriptor("f2", TestClass.class));
        generator.generateClassSource();
        final Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(generator.getTargetClassFullName(), generator.getTargetClassSource());
        Assert.assertNotNull(clazz.newInstance());
    }

    @Test
    public void testObjectPropertyTypeListOfLong() throws IntrospectionException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final BeanPropertySourceGenerator generator = new BeanPropertySourceGenerator(TestClass.class, new PropertyDescriptor("f3", TestClass.class));
        generator.generateClassSource();
        final Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(generator.getTargetClassFullName(), generator.getTargetClassSource());
        Assert.assertNotNull(clazz.newInstance());
    }

    @Test
    public void testObjectPropertyTypePrimitiveByte() throws IntrospectionException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final BeanPropertySourceGenerator generator = new BeanPropertySourceGenerator(TestClass.class, new PropertyDescriptor("f4", TestClass.class));
        generator.generateClassSource();
        final Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(generator.getTargetClassFullName(), generator.getTargetClassSource());
        Assert.assertNotNull(clazz.newInstance());
    }

    @Test
    public void testObjectPropertyGetClass() throws IntrospectionException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final BeanPropertySourceGenerator generator = new BeanPropertySourceGenerator(TestClass.class, new PropertyDescriptor("class", TestClass.class, "getClass", null));
        generator.generateClassSource();
        final Class<?> clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(generator.getTargetClassFullName(), generator.getTargetClassSource());
        Assert.assertNotNull(clazz.newInstance());
    }
}
