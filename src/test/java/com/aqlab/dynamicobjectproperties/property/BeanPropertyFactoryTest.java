package com.aqlab.dynamicobjectproperties.property;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanPropertyFactoryTest {
    @Test
    public void testObjectPropertyTypeString() {
        final BeanProperty<TestClass> p = BeanPropertyFactory.INSTANCE.getBeanProperty(TestClass.class, "f1");
        Assert.assertNotNull(p);
    }

    @Test
    public void testObjectPropertyTypeIntArray() {
        final BeanProperty<TestClass> p = BeanPropertyFactory.INSTANCE.getBeanProperty(TestClass.class, "f2");
        Assert.assertNotNull(p);
    }

    @Test
    public void testObjectPropertyTypeListOfLong() {
        final BeanProperty<TestClass> p = BeanPropertyFactory.INSTANCE.getBeanProperty(TestClass.class, "f3");
        Assert.assertNotNull(p);
    }

    @Test
    public void testObjectPropertyTypePrimitiveByte() {
        final BeanProperty<TestClass> p = BeanPropertyFactory.INSTANCE.getBeanProperty(TestClass.class, "f4");
        Assert.assertNotNull(p);
    }

    @Test
    public void testObjectPropertyGetClass() {
        final BeanProperty<TestClass> p = BeanPropertyFactory.INSTANCE.getBeanProperty(TestClass.class, "class");
        Assert.assertNotNull(p);
    }

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
}
