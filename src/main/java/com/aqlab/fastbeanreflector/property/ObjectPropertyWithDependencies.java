package com.aqlab.fastbeanreflector.property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ObjectPropertyWithDependencies<ObjectT> extends AbstractObjectProperty<ObjectT> {

    public ObjectPropertyWithDependencies(final Class<?> objectType, final Class<?> valueType, final String uniqueIdentifier) {
        super(objectType, valueType, uniqueIdentifier);
    }

    public abstract List<ObjectProperty<?>> getDependents();

    public Set<ObjectProperty<?>> getNestedDependents() {
        return breathFirstSearch(null);
    }

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
