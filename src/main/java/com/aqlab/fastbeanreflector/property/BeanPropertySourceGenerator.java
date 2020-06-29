package com.aqlab.fastbeanreflector.property;

import java.beans.PropertyDescriptor;

public class BeanPropertySourceGenerator {

    private static final String CLASS_FRAME = "%s;\n\n" +
            "public class %s extends " + BeanProperty.class.getName() + "<%s> {\n\n" +
            "\tprivate static final %s.Getter<%s> STATIC_GETTER = %s;\n" +
            "\tprivate static final %s.Setter<%s> STATIC_SETTER = %s;\n\n" +
            "\t%s() {\n" +
            "\t\tsuper(new %s(%s.class, %s.class, \"%s\", STATIC_GETTER, STATIC_SETTER));\n" +
            "\t}\n" +
            "}\n";

    private static final String GETTER_TEMPLATE = "t -> t.%s()";
    private static final String SETTER_TEMPLATE = "(t, v) -> t.%s((%s)v)";
    private static final String NULL_STRING = "null";

    private final Class<?> objectType;
    private final PropertyDescriptor propertyDescriptor;
    private final String targetPackageName;
    private final boolean defaultPackage;
    private final String targetClassName;
    private String targetClassSource;

    BeanPropertySourceGenerator(final Class<?> objectType, final PropertyDescriptor propertyDescriptor) {
        if (objectType == null) {
            throw new NullPointerException("objectType");
        }
        if (objectType.isPrimitive()) {
            throw new IllegalArgumentException(String.format("Primitive type %s is not a supported object type", objectType.getName()));
        }

        this.objectType = objectType;
        this.propertyDescriptor = propertyDescriptor;

        targetPackageName = BeanProperty.class.getPackage().getName();
        defaultPackage = targetPackageName.length() == 0;
        final int hash = propertyDescriptor.hashCode();
        targetClassName = objectType.getName().replace('.', '_').replace("\\$", "__") +
                "_" + propertyDescriptor.getName() + "_BeanProperty_" + (hash > 0 ? hash : '0' + (-hash));
    }

    private static String formatClassNameForSource(final Class<?> clazz) {
        Class<?> componentType = clazz;
        int dim = 0;
        while (componentType.isArray()) {
            componentType = componentType.getComponentType();
            dim++;
        }
        final StringBuilder result = new StringBuilder(componentType.getName().replace('$', '.'));
        for (int i = 0; i < dim; i++) {
            result.append("[]");
        }
        return result.toString();
    }

    public String getTargetClassFullName() {
        return defaultPackage ? targetClassName : String.format("%s.%s", targetPackageName, targetClassName);
    }

    public String getTargetClassSource() {
        return targetClassSource;
    }

    void generateClassSource() {
        final Class<?> valueType = propertyDescriptor.getPropertyType();
        if (valueType == null) {
            throw new NullPointerException("valueType");
        }

        final String propertyName = propertyDescriptor.getName();
        final String objectTypeName = formatClassNameForSource(objectType);
        final String valueTypeName = formatClassNameForSource(valueType);
        String delegateTypeName = FunctionalProperty.class.getName();
        if (valueType.isPrimitive()) {
            delegateTypeName += valueTypeName.substring(0, 1).toUpperCase() + valueTypeName.substring(1) + "Value";
        } else {
            delegateTypeName += "ObjectValue";
        }

        targetClassSource = String.format(CLASS_FRAME,
                defaultPackage ? "" : "package " + targetPackageName, // package
                targetClassName, objectTypeName, // class definition
                delegateTypeName, objectTypeName, propertyDescriptor.getReadMethod() == null ? NULL_STRING : String.format(GETTER_TEMPLATE, propertyDescriptor.getReadMethod().getName()), // isReadable
                delegateTypeName, objectTypeName, propertyDescriptor.getWriteMethod() == null ? NULL_STRING : String.format(SETTER_TEMPLATE, propertyDescriptor.getWriteMethod().getName(), valueTypeName), // isWritable
                targetClassName, // constructor
                delegateTypeName, objectTypeName, valueTypeName, propertyName); // constructor of delegate
    }

}
