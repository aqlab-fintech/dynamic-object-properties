package com.aqlab.fastbeanreflector.property;

import org.junit.Assert;
import org.junit.Test;

public class SelfPropertyTest {
    @Test
    public void testGetter() {
        final ObjectProperty<Object> p = SelfProperty.INSTANCE;
        final Object o = new Object();
        Assert.assertTrue(p.isReadable());
        Assert.assertEquals(o, p.get(o));
    }

    @Test
    public void testSetter() {
        final ObjectProperty<Object> p = SelfProperty.INSTANCE;
        final Object o = new Object();
        Assert.assertFalse(p.isWritable());
        try {
            p.set(o, null);
            Assert.fail("SelfProperty should not support 'set' operation");
        } catch (final UnsupportedOperationException e) {
        }
    }
}