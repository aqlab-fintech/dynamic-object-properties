package com.aqlab.dynamicobjectproperties.property;

import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for the 8 primitive-specialized {@code FunctionalProperty*Value} classes.
 * <p>
 * Bug 4: each primitive subclass had two null-bean code paths that disagreed with each other:
 * <ul>
 *   <li>{@code getXxx(null)} returned the primitive default (e.g. {@code 0}, {@code false},
 *       {@code 0d}) — safe, no NPE.</li>
 *   <li>{@code get(null)} (the generic boxed path) returned {@code null}.</li>
 * </ul>
 * A caller doing {@code Integer v = (Integer) property.get(bean);} with a null bean would get
 * {@code null}, and a subsequent unboxing assignment ({@code int x = v;}) would throw NPE —
 * even though {@code property.getInt(bean)} on the same null bean returned {@code 0}. The two
 * access paths for the same property + same bean must agree.
 * <p>
 * After the fix {@code get(null)} returns the boxed default that matches {@code getXxx(null)}
 * for every primitive type.
 */
public class FunctionalPropertyPrimitiveNullTest {

    // a throwaway target type; the bean is always null in these tests so the getter/setter
    // bodies never run
    private static final Class<?> OBJECT_TYPE = Object.class;

    // -------- Boolean --------
    @Test
    public void testBooleanGetNullConsistentWithGetBooleanNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyBooleanValue(OBJECT_TYPE, "!test/bool",
                        b -> true, null);
        Assert.assertEquals("getBoolean(null) default", true, p.getBoolean(null));
        Assert.assertEquals("get(null) must match getBoolean(null)", Boolean.TRUE, p.get(null));
    }

    // -------- Byte --------
    @Test
    public void testByteGetNullConsistentWithGetByteNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyByteValue(OBJECT_TYPE, "!test/byte",
                        b -> (byte) 7, null);
        Assert.assertEquals("getByte(null) default", (byte) 0, p.getByte(null));
        Assert.assertEquals("get(null) must match getByte(null)", Byte.valueOf((byte) 0), p.get(null));
    }

    // -------- Char --------
    @Test
    public void testCharGetNullConsistentWithGetCharNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyCharValue(OBJECT_TYPE, "!test/char",
                        b -> 'x', null);
        Assert.assertEquals("getChar(null) default", (char) 0, p.getChar(null));
        Assert.assertEquals("get(null) must match getChar(null)", Character.valueOf((char) 0), p.get(null));
    }

    // -------- Short --------
    @Test
    public void testShortGetNullConsistentWithGetShortNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyShortValue(OBJECT_TYPE, "!test/short",
                        b -> (short) 7, null);
        Assert.assertEquals("getShort(null) default", (short) 0, p.getShort(null));
        Assert.assertEquals("get(null) must match getShort(null)", Short.valueOf((short) 0), p.get(null));
    }

    // -------- Int --------
    @Test
    public void testIntGetNullConsistentWithGetIntNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyIntValue(OBJECT_TYPE, "!test/int",
                        b -> 7, null);
        Assert.assertEquals("getInt(null) default", 0, p.getInt(null));
        Assert.assertEquals("get(null) must match getInt(null)", Integer.valueOf(0), p.get(null));
    }

    // -------- Long --------
    @Test
    public void testLongGetNullConsistentWithGetLongNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyLongValue(OBJECT_TYPE, "!test/long",
                        b -> 7L, null);
        Assert.assertEquals("getLong(null) default", 0L, p.getLong(null));
        Assert.assertEquals("get(null) must match getLong(null)", Long.valueOf(0L), p.get(null));
    }

    // -------- Float --------
    @Test
    public void testFloatGetNullConsistentWithGetFloatNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyFloatValue(OBJECT_TYPE, "!test/float",
                        b -> 7f, null);
        Assert.assertEquals("getFloat(null) default", 0f, p.getFloat(null), 0f);
        Assert.assertEquals("get(null) must match getFloat(null)", Float.valueOf(0f), p.get(null));
    }

    // -------- Double --------
    @Test
    public void testDoubleGetNullConsistentWithGetDoubleNull() {
        final FunctionalProperty<Object> p = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyDoubleValue(OBJECT_TYPE, "!test/double",
                        b -> 7d, null);
        Assert.assertEquals("getDouble(null) default", 0d, p.getDouble(null), 0d);
        Assert.assertEquals("get(null) must match getDouble(null)", Double.valueOf(0d), p.get(null));
    }

    // -------- Unboxing safety --------
    /**
     * Bug 4 regression (the actual NPE scenario): a caller assigns the result of get() to a
     * primitive via unboxing. Before the fix this threw NullPointerException because get(null)
     * returned null. After the fix it must unbox cleanly to the primitive default.
     */
    @Test
    public void testUnboxingGetNullDoesNotThrow() {
        final FunctionalProperty<Object> intProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyIntValue(OBJECT_TYPE, "!test/unbox-int",
                        b -> 7, null);
        final FunctionalProperty<Object> boolProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyBooleanValue(OBJECT_TYPE, "!test/unbox-bool",
                        b -> false, null);
        final FunctionalProperty<Object> doubleProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyDoubleValue(OBJECT_TYPE, "!test/unbox-double",
                        b -> 1d, null);

        // these unboxing assignments would NPE before the fix
        int i = (Integer) intProp.get(null);
        boolean z = (Boolean) boolProp.get(null);
        double d = (Double) doubleProp.get(null);

        Assert.assertEquals(0, i);
        Assert.assertEquals(true, z); // getBoolean(null) returns true for this class
        Assert.assertEquals(0d, d, 0d);
    }

    // -------- Non-null sanity (make sure the fix didn't break the happy path) --------
    @Test
    public void testNonNullBeanStillReturnsActualValue() {
        final FunctionalProperty<Object> intProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyIntValue(OBJECT_TYPE, "!test/nonnull-int",
                        b -> 42, null);
        Assert.assertEquals(42, intProp.getInt(new Object()));
        Assert.assertEquals(Integer.valueOf(42), intProp.get(new Object()));
    }

    // -------- Identity consistency (the specific requirement) --------
    /**
     * Every call to {@code get(null)} on the same property must return the SAME boxed
     * instance (identity-equal, {@code ==}), not just an equal one. This is the specific
     * guarantee the user asked for: for {@code Float} and {@code Double} — whose
     * {@code valueOf} does not cache — this rules out allocating a new box on every call.
     * For the integer box types it also guarantees a stable identity rather than relying on
     * the JDK's cached-range behaviour.
     * <p>
     * We compare repeated calls against each other (not against a freshly-boxed value in
     * the test) because for the non-cached types a fresh {@code Double.valueOf(0d)} in the
     * test would itself be a distinct instance.
     */
    @Test
    public void testGetNullReturnsSameBoxedInstanceAcrossCalls() {
        final FunctionalProperty<Object> boolProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyBooleanValue(OBJECT_TYPE, "!test/id-bool", b -> true, null);
        final FunctionalProperty<Object> byteProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyByteValue(OBJECT_TYPE, "!test/id-byte", b -> (byte) 0, null);
        final FunctionalProperty<Object> charProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyCharValue(OBJECT_TYPE, "!test/id-char", b -> 'a', null);
        final FunctionalProperty<Object> shortProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyShortValue(OBJECT_TYPE, "!test/id-short", b -> (short) 0, null);
        final FunctionalProperty<Object> intProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyIntValue(OBJECT_TYPE, "!test/id-int", b -> 0, null);
        final FunctionalProperty<Object> longProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyLongValue(OBJECT_TYPE, "!test/id-long", b -> 0L, null);
        final FunctionalProperty<Object> floatProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyFloatValue(OBJECT_TYPE, "!test/id-float", b -> 0f, null);
        final FunctionalProperty<Object> doubleProp = FunctionalPropertyFactory.INSTANCE
                .createFunctionalPropertyDoubleValue(OBJECT_TYPE, "!test/id-double", b -> 0d, null);

        // repeated calls must return the same instance (identity, ==)
        assertSameAcrossCalls(boolProp);
        assertSameAcrossCalls(byteProp);
        assertSameAcrossCalls(charProp);
        assertSameAcrossCalls(shortProp);
        assertSameAcrossCalls(intProp);
        assertSameAcrossCalls(longProp);
        assertSameAcrossCalls(floatProp);
        assertSameAcrossCalls(doubleProp);
    }

    private static void assertSameAcrossCalls(final FunctionalProperty<Object> prop) {
        final Object a = prop.get(null);
        final Object b = prop.get(null);
        final Object c = prop.get(null);
        Assert.assertNotNull("get(null) must not return null", a);
        Assert.assertSame("repeated get(null) must return the same instance (call 2)", a, b);
        Assert.assertSame("repeated get(null) must return the same instance (call 3)", a, c);
    }
}
