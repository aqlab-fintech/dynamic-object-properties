# Dynamic Object Properties (DOP)

A Java library for creating unified, type-safe property abstractions over plain objects. DOP lets you treat **bean properties**, **computed/functional properties**, and **chained/nested properties** through a single `ObjectProperty<T>` interface — no reflection overhead at call time, no code generation boilerplate.

## Features

### Unified Property Interface

All property types implement `ObjectProperty<T>`, providing consistent getters, setters, and capability queries (`canGetInt()`, `isReadable()`, `isWritable()`, etc.) across:

- **`BeanProperty`** — auto-generated accessors for standard Java bean getters/setters (via MethodHandle or `javac` compilation at runtime)
- **`FunctionalProperty`** — computed properties defined by lambda functions (e.g. `e -> ((int) e.getDoubleValue()) % 10`)
- **`ChainedProperty`** — derived properties that traverse object graphs (e.g. `company.branch.manager.name`)
- **`SelfProperty`** — properties that reference the object itself

### Dependency Management

DOP comes with built-in **dependency management** for derived properties. `ObjectPropertyWithDependencies` tracks which properties depend on which, enabling automatic change propagation and dependency graph traversal:

- **`getDependents()`** — returns the direct list of properties this property depends on
- **`getNestedDependents()`** — returns ALL transitively reachable dependents via breadth-first search (handles cycles, shared sub-chains, and nested chains)
- **`getRootDependents()`** — returns only leaf property nodes, excluding intermediate `ObjectPropertyWithDependencies` wrappers

When you chain properties together (e.g. `ChainedProperty.create(parseProperty, yearProperty)`), the framework automatically builds the dependency graph so that updates to source properties can cascade to all derived properties.

## Examples

All examples live in the `examples` source set (`src/examples/java/`). Build them with `./gradlew build` or run specific examples via `./gradlew runPerformanceBenchmark`.

### `FirstExampleMain`

The simplest introduction — creates a `BeanProperty` for a double field and a `FunctionalProperty` that computes the unit digit from it. Demonstrates the unified `ObjectProperty` interface in action.

```
!BEAN::H99271FB11E3A...::doubleValue => 123.45
!FUNC::unitDigit => 3
```

### `CompareAndReportMain`

Demonstrates `ObjectPropertyUtils.diff()` and `ObjectPropertyUtils.createList()` to compare two `ReportLine` objects and produce a tabular diff of all differing fields. Uses `BeanProperty` to enumerate and extract field values from both objects.

### `DateFormatExamplesMain`

A Swing-based interactive demo showing **dependency management** and **property chaining** in action. An editable table backed by `ObjectPropertyBackedTableModel` displays date formatting derived properties:

- Source: editable `AtomicReference<String>` (ISO date-time string)
- Derived: `ChainedProperty` chains parse the string → `LocalDateTime` → year, month, day, day-of-week, formatted date, formatted time
- Change the source cell and all derived columns update automatically

Uses `FunctionalProperty`, `ChainedProperty`, and `SelfProperty` together to build a live reactive table without any internal storage — all reads flow through the property graph.

### `PerformanceBenchmarkExample`

Compares DOP against raw reflection, `MethodHandle.invoke()`, and direct field access across cold-start and warm iterations (1 to 100,000). Exports results to CSV and Markdown for graphing. Run with:

```bash
./gradlew runPerformanceBenchmark
```

### Swing Support

- **`ObjectPropertyBackedTableModel`** — a `JTable` model that reads/writes entirely through `ObjectProperty` instances. Column classes, editability, and values are all driven by the property definitions.

## Quick Start

```java
// Bean property (auto-generated at runtime)
BeanProperty<ProfileBean> nameProperty = BeanPropertyFactory.INSTANCE
    .getBeanProperty(ProfileBean.class, "name");

// Computed property via lambda
FunctionalProperty<ProfileBean> nameLength = FunctionalPropertyFactory.INSTANCE
    .createFunctionalPropertyIntValue(
        ProfileBean.class, "!FUNC::nameLength",
        p -> p.getName() != null ? p.getName().length() : 0,
        null  // read-only
    );

// Chained property: profile.details.city
BeanProperty<ProfileBean> details = BeanPropertyFactory.INSTANCE
    .getBeanProperty(ProfileBean.class, "details");
BeanProperty<CityBean> city = BeanPropertyFactory.INSTANCE
    .getBeanProperty(CityBean.class, "city");
ChainedProperty<ProfileBean> cityProperty = ChainedProperty.create(details, city);

// All three share the same interface
ProfileBean bean = new ProfileBean();
bean.setName("Alex");

String name = nameProperty.get(bean);       // "Alex"
int len = nameLength.get(bean);             // 4
String c = cityProperty.get(bean);          // depends on bean.getDetails().getCity()
```

## Build

```bash
./gradlew build        # compiles main + test + examples
./gradlew test         # runs unit tests
./gradlew runPerformanceBenchmark  # runs the benchmark
```

Requires JDK 11+ for runtime code generation. Tests run on JDK 21 (Adoptium); benchmark execution uses JDK 11 for optimal `URLClassLoader` behaviour.
