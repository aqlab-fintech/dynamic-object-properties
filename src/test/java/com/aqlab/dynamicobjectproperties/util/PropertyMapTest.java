package com.aqlab.dynamicobjectproperties.util;

import com.aqlab.dynamicobjectproperties.property.BeanProperty;
import com.aqlab.dynamicobjectproperties.property.BeanPropertyFactory;
import com.aqlab.dynamicobjectproperties.property.FunctionalPropertyFactory;
import com.aqlab.dynamicobjectproperties.property.ObjectProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Regression tests for {@link PropertyMap}.
 * <p>
 * Bug 1: {@code PropertyMap.put(String, Object)} used {@code propertiesMap.get(o)} (the value)
 * instead of {@code propertiesMap.get(s)} (the key) to look up the backing property. As a result
 * every {@code put} threw "key does not exist" because the value is almost never a key in the
 * property map.
 * <p>
 * These tests verify that {@code put} writes through to the target bean via the property keyed
 * by the supplied String, and that the original value is returned.
 */
public class PropertyMapTest {

    public static class TestBean {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(final int age) {
            this.age = age;
        }
    }

    /**
     * Bug 1 regression: put() must resolve the property by the supplied key, not the value.
     * Before the fix, this threw IllegalArgumentException("name: key does not exist").
     */
    @Test
    public void testPutWritesThroughToBean() {
        final TestBean bean = new TestBean();
        bean.setName("alice");
        bean.setAge(30);

        final BeanProperty<TestBean> nameProp = BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "name");
        final BeanProperty<TestBean> ageProp = BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "age");

        final Map<String, ObjectProperty<TestBean>> backing = new LinkedHashMap<>();
        backing.put("name", nameProp);
        backing.put("age", ageProp);

        final Map<String, Object> view = ObjectPropertyUtils.createMap(bean, backing);

        // put should update the bean's name field, not throw
        final Object previous = view.put("name", "bob");
        Assert.assertEquals("put should return the previous value", "alice", previous);
        Assert.assertEquals("bean field should be updated through the map view", "bob", bean.getName());
        Assert.assertEquals("map view should reflect the new value", "bob", view.get("name"));
    }

    /**
     * Bug 1 regression: put() for an int property. The old code looked up the property using
     * the Integer value (e.g. 42) as the key, which never matched "age".
     */
    @Test
    public void testPutIntPropertyWritesThroughToBean() {
        final TestBean bean = new TestBean();
        bean.setAge(30);

        final BeanProperty<TestBean> ageProp = BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "age");

        final Map<String, ObjectProperty<TestBean>> backing = new LinkedHashMap<>();
        backing.put("age", ageProp);

        final Map<String, Object> view = ObjectPropertyUtils.createMap(bean, backing);

        final Object previous = view.put("age", 42);
        Assert.assertEquals("put should return the previous boxed value", Integer.valueOf(30), previous);
        Assert.assertEquals("bean int field should be updated", 42, bean.getAge());
    }

    /**
     * Bug 1 regression: put() on a non-existent key should throw, but the throw must be because
     * the key is genuinely absent, not because the value was used as a lookup key.
     */
    @Test
    public void testPutMissingKeyThrows() {
        final TestBean bean = new TestBean();
        final BeanProperty<TestBean> nameProp = BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "name");
        final Map<String, ObjectProperty<TestBean>> backing = new LinkedHashMap<>();
        backing.put("name", nameProp);

        final Map<String, Object> view = ObjectPropertyUtils.createMap(bean, backing);

        try {
            view.put("doesNotExist", "value");
            Assert.fail("Expected IllegalArgumentException for missing key");
        } catch (final IllegalArgumentException expected) {
            Assert.assertTrue("error message should mention the missing key",
                    expected.getMessage().contains("doesNotExist"));
        }
    }

    /**
     * Sanity: putAll must route through put() and write through to the bean.
     */
    @Test
    public void testPutAllWritesThroughToBean() {
        final TestBean bean = new TestBean();
        bean.setName("alice");
        bean.setAge(30);

        final List<BeanProperty<TestBean>> props = Arrays.asList(
                BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "name"),
                BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "age"));

        final Map<String, Object> view = ObjectPropertyUtils.createMapFromBeanProperties(bean, props);

        final Map<String, Object> updates = new LinkedHashMap<>();
        updates.put("name", "carol");
        updates.put("age", 25);
        view.putAll(updates);

        Assert.assertEquals("carol", bean.getName());
        Assert.assertEquals(25, bean.getAge());
    }

    /**
     * Sanity: entrySet().iterator().next().setValue() must also route through put() and write
     * through to the bean (this is the live-view contract).
     */
    @Test
    public void testEntrySetValueWritesThroughToBean() {
        final TestBean bean = new TestBean();
        bean.setName("alice");

        final BeanProperty<TestBean> nameProp = BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "name");
        final Map<String, ObjectProperty<TestBean>> backing = new LinkedHashMap<>();
        backing.put("name", nameProp);

        final Map<String, Object> view = ObjectPropertyUtils.createMap(bean, backing);

        final Map.Entry<String, Object> entry = view.entrySet().iterator().next();
        entry.setValue("dave");

        Assert.assertEquals("dave", bean.getName());
        Assert.assertEquals("dave", view.get("name"));
    }
}
