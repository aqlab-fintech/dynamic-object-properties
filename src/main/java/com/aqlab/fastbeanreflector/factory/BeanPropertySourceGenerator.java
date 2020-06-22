package com.aqlab.fastbeanreflector.factory;

import com.aqlab.fastbeanreflector.property.BeanProperty;

import java.beans.PropertyDescriptor;

class BeanPropertySourceGenerator {

    private static final String CLASS_FRAME = "%s;\n\n" +
            "public class %s extends " + BeanProperty.class.getName() + "<%s> {\n" +
            "\tpublic boolean isReadable() { return %b; }\n\n" +
            "\tpublic boolean isWritable() { return %b; }\n\n" +
            "\tpublic String getUniqueIdentifier() { return \"%s\"; }\n\n" +
            "\tpublic Class<?> getObjectType() { return %s.class; }\n\n" +
            "\tpublic Class<?> getValueType() { return %s.class; }\n\n" +
            "%s\n" +
            "%s\n" +
            "}\n";
    private static final String GETTER_TEMPLATE = "\tpublic %s get%s(final %s objectType) {\n" +
            "\t\treturn %sobjectType.%s();\n" +
            "\t}\n";
    private static final String GETTER_TEMPLATE_WITH_BOXING = "\tpublic <ValueT> ValueT get(final %s objectType) {\n" +
            "\t\tObject o = get%s(objectType);\n" +
            "\t\treturn (ValueT) o;\n" +
            "\t}\n";
    private static final String SETTER_TEMPLATE = "\tpublic void set%s(final %s objectType, final %s value) {\n" +
            "\t\tobjectType.%s(%svalue);\n" +
            "\t}\n";
    private static final String SETTER_TEMPLATE_WITH_BOXING = "\tpublic void set(final %s objectType, final %s value) {\n" +
            "\t\tset%s(objectType, (%s) value);\n" +
            "\t}\n";

    private static final String GETTER_OBJECT_RETURN_TYPE_CAST = "(ValueT) ";
    private static final String GETTER_OBJECT_RETURN_TYPE_GENERICS = "<ValueT> ValueT";
    private static final String SETTER_OBJECT_PARAM_TYPE = "Object";

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
        final String className = objectType.getName().replace('.', '_').replace("\\$", "__") +
                "_" + propertyDescriptor.getName() + "_BeanProperty_" + propertyDescriptor.hashCode();
        targetClassName = defaultPackage ? className : String.format("%s.%s", targetPackageName, className);
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

    public String getTargetClassName() {
        return targetClassName;
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
        final String className = objectType.getSimpleName().replace('.', '_').replace("\\$", "__") +
                "_" + propertyName + "_BeanProperty_" + objectType.getSimpleName().hashCode();
        final String objectTypeName = formatClassNameForSource(objectType);
        final String valueTypeName = formatClassNameForSource(valueType);
        targetClassSource = String.format(CLASS_FRAME,
                defaultPackage ? "" : "package " + targetPackageName, // package
                className, objectTypeName, // class definition
                propertyDescriptor.getReadMethod() != null, // isReadable
                propertyDescriptor.getWriteMethod() != null, // isWritable
                targetClassName, // getUniqueIdentifier
                objectTypeName, // getObjectType
                valueTypeName, // getValueType
                generateGetterSource(), // get
                generateSetterSource()); // set
    }

    private String generateGetterSource() {
        if (propertyDescriptor.getReadMethod() == null) {
            return "";
        }

        final Class<?> valueType = propertyDescriptor.getPropertyType();
        final String objectTypeName = formatClassNameForSource(objectType);

        if (valueType.isPrimitive()) {
            final String primitiveType = valueType.getName();
            final String primitiveTypeCamelCase = Character.toUpperCase(primitiveType.charAt(0)) + primitiveType.substring(1);
            return String.format(GETTER_TEMPLATE,
                    primitiveType, primitiveTypeCamelCase, objectTypeName, // primitive getter prototype
                    "", propertyDescriptor.getReadMethod().getName()) // getter body
                    + String.format(GETTER_TEMPLATE_WITH_BOXING,
                    objectTypeName, // boxing getter prototype
                    primitiveTypeCamelCase); // getter body
        } else {
            return String.format(GETTER_TEMPLATE,
                    GETTER_OBJECT_RETURN_TYPE_GENERICS, "", objectTypeName, // prototype
                    GETTER_OBJECT_RETURN_TYPE_CAST, propertyDescriptor.getReadMethod().getName()); // getter body
        }
    }

    private String generateSetterSource() {
        if (propertyDescriptor.getWriteMethod() == null) {
            return "";
        }

        final Class<?> valueType = propertyDescriptor.getPropertyType();
        final String objectTypeName = formatClassNameForSource(objectType);

        if (valueType.isPrimitive()) {
            final String primitiveType = valueType.getName();
            final String primitiveTypeCamelCase = Character.toUpperCase(primitiveType.charAt(0)) + primitiveType.substring(1);
            return String.format(SETTER_TEMPLATE,
                    primitiveTypeCamelCase, objectTypeName, primitiveType, // primitive setter prototype
                    propertyDescriptor.getWriteMethod().getName(), "") // setter body
                    + String.format(SETTER_TEMPLATE_WITH_BOXING,
                    objectTypeName, SETTER_OBJECT_PARAM_TYPE, // boxing setter prototype
                    primitiveTypeCamelCase, primitiveType); // setter body
        } else {
            return String.format(SETTER_TEMPLATE,
                    "", objectTypeName, SETTER_OBJECT_PARAM_TYPE, // prototype
                    propertyDescriptor.getWriteMethod().getName(), "(" + formatClassNameForSource(valueType) + ") "); // setter body
        }
    }
}
