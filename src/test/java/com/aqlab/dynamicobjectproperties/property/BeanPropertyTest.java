package com.aqlab.dynamicobjectproperties.property;

import org.junit.Assert;
import org.junit.Test;

public class BeanPropertyTest {
    public static class TestClass {
        private int f1;
        private final byte f2 = 9;
        private TestClass f3;
        private Double f4;
        private char f5;

        public int getF1() {
            return f1;
        }

        public void setF1(final int f1) {
            this.f1 = f1;
        }

        public byte getF2() {
            return f2;
        }

        public TestClass getF3() {
            return f3;
        }

        public void setF3(final TestClass f3) {
            this.f3 = f3;
        }

        public void setF4(final Double f4) {
            this.f4 = f4;
        }

        public char getF5() {
            return f5;
        }

        public void setF5(final char f5) {
            this.f5 = f5;
        }
    }

    @Test
    public void testPrimitiveIntGetter() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f1");
        final TestClass instance = new TestClass();
        property.set(instance, 10);
        Assert.assertEquals(instance.getF1(), property.getInt(instance));
        Assert.assertEquals(Integer.valueOf(instance.getF1()), property.get(instance));
    }

    @Test
    public void testPrimitiveIntAttributes() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f1");
        Assert.assertEquals(TestClass.class, property.getObjectType());
        Assert.assertEquals(int.class, property.getValueType());
        Assert.assertEquals("f1", property.getPropertyName());
        Assert.assertTrue(property.isReadable());
        Assert.assertTrue(property.canGetInt());
        Assert.assertTrue(property.isWritable());
        Assert.assertTrue(property.canSetInt());
    }

    @Test
    public void testPrimitiveByteGetter() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f2");
        final TestClass instance = new TestClass();
        Assert.assertEquals(instance.getF2(), property.getByte(instance));
        Assert.assertEquals(Byte.valueOf(instance.getF2()), property.get(instance));
    }

    @Test
    public void testPrimitiveByteAttributes() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f2");
        Assert.assertEquals(TestClass.class, property.getObjectType());
        Assert.assertEquals(byte.class, property.getValueType());
        Assert.assertEquals("f2", property.getPropertyName());
        Assert.assertTrue(property.isReadable());
        Assert.assertTrue(property.canGetByte());
        Assert.assertFalse(property.isWritable());
        Assert.assertFalse(property.canSetByte());
    }

    @Test
    public void testTestClassGetterSetter() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f3");
        final TestClass instance = new TestClass();
        property.set(instance, new TestClass());
        Assert.assertEquals(instance.getF3(), property.get(instance));
    }

    @Test
    public void testTestClassAttributes() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f3");
        Assert.assertEquals(TestClass.class, property.getObjectType());
        Assert.assertEquals(TestClass.class, property.getValueType());
        Assert.assertEquals("f3", property.getPropertyName());
        Assert.assertTrue(property.isReadable());
        Assert.assertTrue(property.isWritable());
    }

    @Test
    public void testDoubleSetter() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f4");
        final TestClass instance = new TestClass();
        property.set(instance, 0.5);
        Assert.assertEquals(instance.f4, 0.5, 1e-6);
    }

    @Test
    public void testObjectDoubleAttributes() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f4");
        Assert.assertEquals(TestClass.class, property.getObjectType());
        Assert.assertEquals(Double.class, property.getValueType());
        Assert.assertEquals("f4", property.getPropertyName());
        Assert.assertFalse(property.isReadable());
        Assert.assertFalse(property.canGetDouble());
        Assert.assertTrue(property.isWritable());
        Assert.assertFalse(property.canSetDouble());
    }

    @Test
    public void testTestClassCharGetter() {
        final BeanProperty<TestClass> property = BeanProperty.FACTORY.getBeanProperty(TestClass.class, "f5");
        final TestClass instance = new TestClass();
        property.set(instance, 'c');
        Assert.assertEquals(instance.getF5(), property.getChar(instance));
    }
}
