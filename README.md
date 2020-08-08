# Dynamic Object Properties (DOP)
## What is this?
Dynamic Object Properties (DOP) extends the concept of object properties beyond Java bean properties.

#### An Example
````
public class FirstExampleMain {
    // a data class
    public static class Example {
        private double doubleValue;

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(final double doubleValue) {
            this.doubleValue = doubleValue;
        }
    }

    public static void main(final String[] argv) {
        // define a Java bean property
        final BeanProperty<Example> doubleValueProperty = BeanPropertyFactory.INSTANCE.getBeanProperty(
                /* target object class */ Example.class,
                /* property name */ "doubleValue"
        );

        // define a derived property (a new property defined by functions)
        final FunctionalProperty<Example> unitDigitProperty = FunctionalPropertyFactory.INSTANCE.createFunctionalPropertyIntValue(
                /* target object class */ Example.class,
                /* unique identifier */ "!FUNC::unitDigit",
                /* getter implementation */ e -> ((int) e.getDoubleValue()) % 10,
                /* setter implementation */ null
        );

        // create an instance of the target object
        final Example targetObject = new Example();
        targetObject.setDoubleValue(123.45);

        // given the target object, prints the unique identifier and the value
        for (final ObjectProperty<Example> property : Arrays.asList(doubleValueProperty, unitDigitProperty)) {
            System.out.println(property.getUniqueIdentifier() + " => " + property.get(targetObject));
        }

        /**
         !BEAN::H99271FB11E3A.cad.examples.FirstExampleMain$Example::doubleValue => 123.45
         !FUNC::unitDigit => 3
         **/
    }
}
````

#### Encapsulation
One can implement the above as ObjectProperty instances. An ObjectProperty encapsulates
* a getter and/or a setter
* the class of the object this property is defined on top of
* the class of the value of the property
* a descriptive unique name

And this allows reflection-like operations like
* print a property's name
* get or set a property by name
* filter properties by the class of the value...

DOP's ObjectProperty encapsulation unifies Java bean and derived properties and thus making your code cleaner. Scenarios are
* developers do not have the control of the definition
  * managed by a different team
  * vendor application
  * JDK classes (YES!!)
* the getter and setter logic is repeated everywhere / defined as static utility method

#### Reflection-free
As the whole idea starts from avoiding reflection overhead and boosting latency-sensitive application's performance, DOP is **reflection-free** after initialization. (`BeanProperty` does use reflection when an instance is created)

#### Implementations
Specialized `ObjectProperty` types defined in this library include
1. `FunctionalProperty`
1. `BeanProperty`
1. `ChainedProperty`
1. `MapBackedProperty`
1. `ListBackedProperty`

## How to use
You can find more details and examples in the wiki.

## License
MIT License

Copyright (c) 2020 aqlab-fintech
