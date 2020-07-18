package com.aqlab.dynamicobjectproperties.property;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapBackedPropertyTest {
    @Test
    public void testGetter() {
        final String key = "key";
        final ObjectProperty<Map<String, Double>> p = new MapBackedProperty(key).castTargetType();
        final Map<String, Double> m = new HashMap<>();
        m.put(key, 12.45);
        Assert.assertTrue(p.isReadable());
        Assert.assertEquals(m.get(key), p.get(m), 1e-6);
    }

    @Test
    public void testSetter() {
        final String key = "key";
        final ObjectProperty<Map<String, Double>> p = new MapBackedProperty(key).castTargetType();
        final Map<String, Double> m = new HashMap<>();
        p.set(m, 12.45);
        Assert.assertTrue(p.isWritable());
        Assert.assertEquals(m.get(key), p.get(m), 1e-6);
    }
}