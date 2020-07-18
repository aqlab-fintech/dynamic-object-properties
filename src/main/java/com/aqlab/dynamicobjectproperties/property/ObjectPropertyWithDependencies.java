package com.aqlab.dynamicobjectproperties.property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A basic implementation of {@link com.aqlab.dynamicobjectproperties.property.ObjectProperty}, with dependencies
 *
 * @param <ObjectT> the target object type
 */
public abstract class ObjectPropertyWithDependencies<ObjectT> extends AbstractObjectProperty<ObjectT> {
    /**
     * The constructor
     *
     * @param objectType       the target object type
     * @param valueType        the value type
     * @param uniqueIdentifier a unique identifier
     */
    public ObjectPropertyWithDependencies(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier) {
        super(objectType, valueType, uniqueIdentifier);
    }

    /**
     * Get a list of dependents in the order specific to the particular implementation
     *
     * @return the list of dependents
     */
    public abstract List<ObjectProperty<?>> getDependents();

    /**
     * Get a set of dependents recursively. The set do not contains any duplicate by definition.
     *
     * @return the set of dependents and nested dependents
     */
    public Set<ObjectProperty<?>> getNestedDependents() {
        return breathFirstSearch(null);
    }

    /**
     * Get a set of dependents recursively. Intemediate dependents (i.e. instanceof {@link ObjectPropertyWithDependencies} are excluded.
     * <p>
     * The set do not contains any duplicate by definition.
     *
     * @return the set of dependents and nested dependents
     */
    public Set<ObjectProperty<?>> getRootDependents() {
        return breathFirstSearch(p -> !(p instanceof ObjectPropertyWithDependencies));
    }

    private Set<ObjectProperty<?>> breathFirstSearch(final Predicate<ObjectProperty<?>> filter) {
        final List<ObjectProperty<?>> queue = new ArrayList<>(getDependents());
        final Set<ObjectProperty<?>> visited = new HashSet<>();
        final Set<ObjectProperty<?>> result = new HashSet<>();

        for (int i = 0; i < queue.size(); i++) {
            final ObjectProperty<?> current = queue.get(i);
            if (filter == null || filter.test(current)) {
                result.add(current);
            }

            visited.add(current);

            if (current instanceof ObjectPropertyWithDependencies) {
                ((ObjectPropertyWithDependencies<?>) current).getDependents().stream().filter(visited::contains).forEach(queue::add);
            }
        }

        return result;
    }
}
