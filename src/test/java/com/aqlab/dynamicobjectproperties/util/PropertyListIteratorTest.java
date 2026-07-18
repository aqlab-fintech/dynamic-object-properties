package com.aqlab.dynamicobjectproperties.util;

import com.aqlab.dynamicobjectproperties.property.BeanProperty;
import com.aqlab.dynamicobjectproperties.property.BeanPropertyFactory;
import com.aqlab.dynamicobjectproperties.property.ObjectProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * Regression tests for {@link PropertyList}'s {@code ListIterator}.
 * <p>
 * Bug 3: {@code PropertyListListIterator.set(Object)} called
 * {@code PropertyList.this.set(nextIndex(), o)} instead of
 * {@code PropertyList.this.set(previousIndex(), o)}. Per the {@link java.util.ListIterator}
 * contract, {@code set} replaces the last element returned by {@code next()} or
 * {@code previous()} — i.e. the element at {@code previousIndex()} after {@code next()} (or
 * {@code nextIndex()} after {@code previous()}). Using {@code nextIndex()} after {@code next()}
 * replaced the element AFTER the one just returned, silently corrupting the wrong slot.
 * <p>
 * These tests drive the iterator through {@code next()} calls and then use {@code set()} to
 * replace the just-returned element, asserting that the correct backing bean field is updated.
 */
public class PropertyListIteratorTest {

    public static class TestBean {
        private String a;
        private String b;
        private String c;

        public String getA() {
            return a;
        }

        public void setA(final String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(final String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(final String c) {
            this.c = c;
        }
    }

    private static List<ObjectProperty<TestBean>> buildProps() {
        return new ArrayList<>(Arrays.asList(
                BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "a"),
                BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "b"),
                BeanPropertyFactory.INSTANCE.getBeanProperty(TestBean.class, "c")));
    }

    /**
     * Bug 3 regression: after next() returns element at index i, set() must replace element i
     * (the one just returned), not element i+1. Before the fix, calling set() after next()
     * overwrote the NEXT slot.
     */
    @Test
    public void testSetAfterNextReplacesCurrentElement() {
        final TestBean bean = new TestBean();
        bean.setA("a0");
        bean.setB("b0");
        bean.setC("c0");

        final List<Object> view = ObjectPropertyUtils.createList(bean, buildProps());
        final ListIterator<Object> it = view.listIterator();

        // advance to index 0 and replace it
        Assert.assertEquals("a0", it.next());
        it.set("a1");

        Assert.assertEquals("bean.a should be updated", "a1", bean.getA());
        Assert.assertEquals("bean.b should be untouched", "b0", bean.getB());
        Assert.assertEquals("bean.c should be untouched", "c0", bean.getC());
        Assert.assertEquals("view[0] should reflect the update", "a1", view.get(0));
    }

    /**
     * Bug 3 regression: the same off-by-one must not corrupt the next slot when iterating
     * through multiple elements. Before the fix, set() after the second next() overwrote the
     * third slot.
     */
    @Test
    public void testSetAfterMultipleNextsReplacesCorrectSlot() {
        final TestBean bean = new TestBean();
        bean.setA("a0");
        bean.setB("b0");
        bean.setC("c0");

        final List<Object> view = ObjectPropertyUtils.createList(bean, buildProps());
        final ListIterator<Object> it = view.listIterator();

        Assert.assertEquals("a0", it.next()); // cursor now between 0 and 1
        Assert.assertEquals("b0", it.next()); // cursor now between 1 and 2
        it.set("b1");

        Assert.assertEquals("bean.a should be untouched", "a0", bean.getA());
        Assert.assertEquals("bean.b should be updated", "b1", bean.getB());
        Assert.assertEquals("bean.c should be untouched", "c0", bean.getC());
    }

    /**
     * Bug 3 regression: set() after previous() must replace the element just returned by
     * previous() (i.e. at nextIndex() after previous()). Before the fix the code used
     * nextIndex() unconditionally, which happened to be correct after previous() but wrong
     * after next(). This test documents the after-previous() path so both directions stay
     * correct if anyone touches the iterator again.
     */
    @Test
    public void testSetAfterPreviousReplacesCurrentElement() {
        final TestBean bean = new TestBean();
        bean.setA("a0");
        bean.setB("b0");
        bean.setC("c0");

        final List<Object> view = ObjectPropertyUtils.createList(bean, buildProps());
        final ListIterator<Object> it = view.listIterator(3); // cursor at end

        // move back to index 2 and replace it
        Assert.assertEquals("c0", it.previous());
        it.set("c1");

        Assert.assertEquals("bean.a should be untouched", "a0", bean.getA());
        Assert.assertEquals("bean.b should be untouched", "b0", bean.getB());
        Assert.assertEquals("bean.c should be updated", "c1", bean.getC());
    }

    /**
     * Bug 3 regression: iterate forward replacing every element via set(); each replacement
     * must hit the matching bean field. Before the fix, the first set() would clobber the
     * second slot, and the second set() would clobber the (non-existent) third+1 slot,
     * producing IndexOutOfBoundsException or silent data loss.
     */
    @Test
    public void testReplaceAllViaIterator() {
        final TestBean bean = new TestBean();
        bean.setA("a0");
        bean.setB("b0");
        bean.setC("c0");

        final List<Object> view = ObjectPropertyUtils.createList(bean, buildProps());
        final ListIterator<Object> it = view.listIterator();
        final List<String> replacements = Arrays.asList("aX", "bX", "cX");

        while (it.hasNext()) {
            it.next();
            it.set(replacements.get(it.previousIndex()));
        }

        Assert.assertEquals("aX", bean.getA());
        Assert.assertEquals("bX", bean.getB());
        Assert.assertEquals("cX", bean.getC());
    }

    /**
     * Sanity: the AbstractList.set(int, Object) path (not the iterator path) must also write
     * through to the bean — this was already correct but is included to guard against
     * regressions while fixing the iterator.
     */
    @Test
    public void testDirectSetByIndexWritesThrough() {
        final TestBean bean = new TestBean();
        bean.setA("a0");
        bean.setB("b0");
        bean.setC("c0");

        final List<Object> view = ObjectPropertyUtils.createList(bean, buildProps());
        final Object previous = view.set(1, "bZ");
        Assert.assertEquals("b0", previous);
        Assert.assertEquals("bZ", bean.getB());
    }
}
