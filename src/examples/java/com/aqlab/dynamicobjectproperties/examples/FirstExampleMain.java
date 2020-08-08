package com.aqlab.dynamicobjectproperties.examples;

import com.aqlab.dynamicobjectproperties.property.*;

import java.util.Arrays;

public class FirstExampleMain {
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
}