package com.aqlab.dynamicobjectproperties.property;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBackedPropertyTest {
    @Test
    public void testGetter() {
        final int index = 1;
        final ObjectProperty<List<String>> p = ListBackedPropertyFactory.getListBackedProperty(index).castTargetType();
        final List<String> l = Arrays.asList("7", "4", "5");
        Assert.assertTrue(p.isReadable());
        Assert.assertEquals(l.get(index), p.get(l));
    }

    @Test
    public void testSetter() {
        final int index = 2;
        final ObjectProperty<List<String>> p = ListBackedPropertyFactory.getListBackedProperty(index).castTargetType();
        final List<String> l = new ArrayList<>(Arrays.asList("7", "4", "5"));
        l.set(index, "newValue");
        Assert.assertTrue(p.isWritable());
        Assert.assertEquals(l.get(index), p.get(l));
    }
}