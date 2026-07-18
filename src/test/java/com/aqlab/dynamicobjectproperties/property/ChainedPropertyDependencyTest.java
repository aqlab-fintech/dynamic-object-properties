package com.aqlab.dynamicobjectproperties.property;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Regression tests for {@link ObjectPropertyWithDependencies#getNestedDependents()} and
 * {@link ObjectPropertyWithDependencies#getRootDependents()}.
 * <p>
 * Bug 2: the breadth-first search in {@code ObjectPropertyWithDependencies.breathFirstSearch}
 * filtered nested dependents with {@code .filter(visited::contains)} — the exact opposite of
 * what a BFS queue needs. Only already-visited nodes were ever added to the queue, so once the
 * initial dependents were consumed the traversal stopped. Any nested {@code ChainedProperty}
 * contributed nothing to the result set, and in graphs containing cycles or shared sub-chains
 * the method effectively returned only the direct dependents.
 * <p>
 * These tests build a small property graph (company -> branch -> manager -> name) and assert
 * that nested dependents are correctly discovered.
 */
public class ChainedPropertyDependencyTest {

    // ---- test domain ----
    public static class Company {
        private Branch branch;

        public Branch getBranch() {
            return branch;
        }

        public void setBranch(final Branch branch) {
            this.branch = branch;
        }
    }

    public static class Branch {
        private Staff manager;

        public Staff getManager() {
            return manager;
        }

        public void setManager(final Staff manager) {
            this.manager = manager;
        }
    }

    public static class Staff {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(final int id) {
            this.id = id;
        }
    }

    /**
     * Build the bean properties for the domain above.
     */
    private static List<BeanProperty<?>> beanProperties() {
        return Arrays.asList(
                BeanPropertyFactory.INSTANCE.getBeanProperty(Company.class, "branch"),
                BeanPropertyFactory.INSTANCE.getBeanProperty(Branch.class, "manager"),
                BeanPropertyFactory.INSTANCE.getBeanProperty(Staff.class, "name"),
                BeanPropertyFactory.INSTANCE.getBeanProperty(Staff.class, "id"));
    }

    @SuppressWarnings("unchecked")
    private static BeanProperty<Company> companyBranchProp() {
        return (BeanProperty<Company>) beanProperties().get(0);
    }

    @SuppressWarnings("unchecked")
    private static BeanProperty<Branch> branchManagerProp() {
        return (BeanProperty<Branch>) beanProperties().get(1);
    }

    @SuppressWarnings("unchecked")
    private static BeanProperty<Staff> staffNameProp() {
        return (BeanProperty<Staff>) beanProperties().get(2);
    }

    @SuppressWarnings("unchecked")
    private static BeanProperty<Staff> staffIdProp() {
        return (BeanProperty<Staff>) beanProperties().get(3);
    }

    /**
     * Build a chain: Company.branch -> Branch.manager -> Staff.name
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ChainedProperty<Company> buildBranchManagerNameChain() {
        final List<BeanProperty<?>> props = beanProperties();
        final BeanProperty companyBranch = props.get(0);
        final BeanProperty branchManager = props.get(1);
        final BeanProperty staffName = props.get(2);
        return ChainedProperty.create(companyBranch, branchManager, staffName);
    }

    /**
     * Bug 2 regression: getNestedDependents() must return ALL transitively reachable dependents,
     * not just the direct chain entries. Before the fix the BFS never enqueued unvisited nodes,
     * so for a 3-element chain only the 3 direct dependents were returned (and for nested
     * chains even those could be wrong). After the fix, nested dependents of any sub-chain are
     * also included.
     * <p>
     * Here we compose two chains that share a suffix and check the nested dependents of the
     * outer chain include the inner chain's members.
     */
    @Test
    public void testGetNestedDependentsIncludesAllChainMembers() {
        final BeanProperty<Branch> branchManager = branchManagerProp();
        final BeanProperty<Staff> staffName = staffNameProp();

        // inner chain: Branch.manager -> Staff.name
        final ChainedProperty<Branch> inner = ChainedProperty.create(branchManager, staffName);

        // outer chain: Company.branch -> (Branch.manager -> Staff.name)
        final BeanProperty<Company> companyBranch = companyBranchProp();
        final ChainedProperty<Company> outer = ChainedProperty.create(companyBranch, inner);

        final Set<ObjectProperty<?>> nested = outer.getNestedDependents();

        // nested must include the inner chain and both direct bean properties it wraps
        Assert.assertTrue("nested dependents must include the inner ChainedProperty",
                nested.contains(inner));
        Assert.assertTrue("nested dependents must include companyBranch",
                nested.contains(companyBranch));
        Assert.assertTrue("nested dependents must include branchManager (reached via inner)",
                nested.contains(branchManager));
        Assert.assertTrue("nested dependents must include staffName (reached via inner)",
                nested.contains(staffName));
    }

    /**
     * Bug 2 regression: for a flat (non-nested) chain, getNestedDependents() must still return
     * every chain member. Before the fix, the inverted filter could drop members when the
     * queue/visited sets interacted badly. This is the simplest reproducer.
     */
    @Test
    public void testGetNestedDependentsForFlatChain() {
        final ChainedProperty<Company> chain = buildBranchManagerNameChain();
        final Set<ObjectProperty<?>> nested = chain.getNestedDependents();

        final List<BeanProperty<?>> props = beanProperties();
        Assert.assertEquals("all 3 chain members must be reported as nested dependents",
                3, nested.size());
        Assert.assertTrue(nested.contains(props.get(0))); // companyBranch
        Assert.assertTrue(nested.contains(props.get(1))); // branchManager
        Assert.assertTrue(nested.contains(props.get(2))); // staffName
    }

    /**
     * Bug 2 regression: getRootDependents() excludes intermediate
     * {@code ObjectPropertyWithDependencies} nodes and returns only the leaf property nodes.
     * Before the fix the inverted BFS filter meant nested dependents of a sub-chain were never
     * visited, so root dependents of an outer chain that wrapped an inner chain were missing
     * the inner chain's leaves.
     */
    @Test
    public void testGetRootDependentsExcludesIntermediateChains() {
        final BeanProperty<Branch> branchManager = branchManagerProp();
        final BeanProperty<Staff> staffName = staffNameProp();
        final BeanProperty<Staff> staffId = staffIdProp();

        // inner chain: Branch.manager -> Staff.name
        final ChainedProperty<Branch> inner = ChainedProperty.create(branchManager, staffName);

        // outer chain: Company.branch -> (Branch.manager -> Staff.name)
        final BeanProperty<Company> companyBranch = companyBranchProp();
        final ChainedProperty<Company> outer = ChainedProperty.create(companyBranch, inner);

        final Set<ObjectProperty<?>> roots = outer.getRootDependents();

        // root dependents must NOT contain any ChainedProperty (intermediate)
        for (final ObjectProperty<?> p : roots) {
            Assert.assertFalse("root dependents must not include intermediate ChainedProperty",
                    p instanceof ChainedProperty);
        }
        // root dependents must include the leaf bean properties: companyBranch, branchManager, staffName
        Assert.assertTrue("companyBranch must be a root dependent", roots.contains(companyBranch));
        Assert.assertTrue("branchManager must be a root dependent (leaf under inner chain)",
                roots.contains(branchManager));
        Assert.assertTrue("staffName must be a root dependent (leaf under inner chain)",
                roots.contains(staffName));
    }

    /**
     * Bug 2 regression: getNestedDependents() must not loop forever on a graph that shares a
     * common suffix sub-chain (diamond shape). Before the fix the broken filter happened to
     * avoid infinite loops by never enqueueing anything — so this test mainly guards against
     * the fixed version over-correcting into an infinite loop.
     */
    @Test
    public void testGetNestedDependentsTerminatesOnSharedSuffix() {
        final BeanProperty<Branch> branchManager = branchManagerProp();
        final BeanProperty<Staff> staffName = staffNameProp();

        // shared suffix chain: Branch.manager -> Staff.name
        final ChainedProperty<Branch> suffix = ChainedProperty.create(branchManager, staffName);

        // two outer chains that both end in the shared suffix
        final BeanProperty<Company> companyBranch = companyBranchProp();
        final ChainedProperty<Company> outerA = ChainedProperty.create(companyBranch, suffix);
        final ChainedProperty<Company> outerB = ChainedProperty.create(companyBranch, suffix);

        // compose a top chain over outerA and outerB-like structure: companyBranch -> outerA
        // (we can only chain by object type, so just inspect outerA's nested set)
        final Set<ObjectProperty<?>> nestedA = outerA.getNestedDependents();
        final Set<ObjectProperty<?>> nestedB = outerB.getNestedDependents();

        // both should report the same set of nested dependents
        Assert.assertEquals(nestedA, nestedB);
        Assert.assertTrue(nestedA.contains(suffix));
        Assert.assertTrue(nestedA.contains(companyBranch));
        Assert.assertTrue(nestedA.contains(branchManager));
        Assert.assertTrue(nestedA.contains(staffName));
    }

    /**
     * Bug 2 regression: getDependents() (the direct list) must remain unchanged — only the
     * nested traversal was broken.
     */
    @Test
    public void testGetDirectDependentsUnchanged() {
        final ChainedProperty<Company> chain = buildBranchManagerNameChain();
        final List<ObjectProperty<?>> direct = chain.getDependents();
        Assert.assertEquals("direct dependents must be the 3-element chain", 3, direct.size());

        final Set<String> ids = direct.stream()
                .map(ObjectProperty::getUniqueIdentifier)
                .collect(Collectors.toSet());
        Assert.assertEquals(3, ids.size());
    }
}
