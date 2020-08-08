package com.aqlab.dynamicobjectproperties.property;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.beans.PropertyDescriptor;

public class BeanPropertySourceGenerator {

    private static final String CLASS_FRAME = "%s;\n\n" +
            "public class %s extends " + BeanProperty.class.getName() + "<%s> {\n\n" +
            "\tprivate static final %s.Getter<%s> STATIC_GETTER = %s;\n" +
            "\tprivate static final %s.Setter<%s> STATIC_SETTER = %s;\n\n" +
            "\tpublic %s() {\n" +
            "\t\tsuper(FunctionalPropertyFactory.INSTANCE.createFunctionalProperty%s(%s.class, %s\"%s\", STATIC_GETTER, STATIC_SETTER), \"%s\");\n" +
            "\t}\n" +
            "}\n";

    private static final String GETTER_TEMPLATE = "t -> t.%s()";
    private static final String SETTER_TEMPLATE = "(t, v) -> t.%s((%s)v)";
    private static final String NULL_STRING = "null";

    private final Class<?> objectType;
    private final PropertyDescriptor propertyDescriptor;
    private final String targetPackageName;
    private final String targetClassName;
    private final String uniqueIdentifier;

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
        final String objectClassShortName = buildObjectClassShortName();

        targetClassName = objectClassShortName.replace('.', '_').replace("\\$", "__") +
                "_" + propertyDescriptor.getName() + "_BeanProperty";
        uniqueIdentifier = "!BEAN::" + objectClassShortName + "::" + propertyDescriptor.getName();
    }

    private String buildObjectClassShortName() {
        final String objectPackageNameHash = "H" + Hashing.sha256()
                .hashString(objectType.getName(), Charsets.UTF_8)
                .toString().substring(0, 12).toUpperCase();
        final String[] objectPackageNames = objectType.getName().split("\\.");
        final StringBuilder objectClassShortNameBuilder = new StringBuilder();
        objectClassShortNameBuilder.append(objectPackageNameHash).append('.');
        for (int i = 0; i < objectPackageNames.length - 2; i++) {
            objectClassShortNameBuilder.append(objectPackageNames[i].charAt(0));
        }
        if (objectPackageNames.length > 0) {
            if (objectPackageNames.length > 1) {
                objectClassShortNameBuilder.append('.');
            }
            objectClassShortNameBuilder.append(objectPackageNames[objectPackageNames.length - 2]);
            objectClassShortNameBuilder.append('.');
        }
        objectClassShortNameBuilder.append(objectPackageNames[objectPackageNames.length - 1]);
        return objectClassShortNameBuilder.toString();
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
        return String.format("%s.%s", targetPackageName, targetClassName);
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
        final String delegateTypeSufix;
        final String valueTypeNameConstructorArgFormatted;
        if (valueType.isPrimitive()) {
            valueTypeNameConstructorArgFormatted = "";
            delegateTypeSufix = valueTypeName.substring(0, 1).toUpperCase() + valueTypeName.substring(1) + "Value";
        } else {
            valueTypeNameConstructorArgFormatted = valueTypeName + ".class, ";
            delegateTypeSufix = "ObjectValue";
        }

        targetClassSource = String.format(CLASS_FRAME,
                "package " + targetPackageName, // package
                targetClassName, objectTypeName, // class definition
                FunctionalProperty.class.getName() + delegateTypeSufix, objectTypeName, propertyDescriptor.getReadMethod() == null ? NULL_STRING : String.format(GETTER_TEMPLATE, propertyDescriptor.getReadMethod().getName()), // isReadable
                FunctionalProperty.class.getName() + delegateTypeSufix, objectTypeName, propertyDescriptor.getWriteMethod() == null ? NULL_STRING : String.format(SETTER_TEMPLATE, propertyDescriptor.getWriteMethod().getName(), valueTypeName), // isWritable
                targetClassName, // constructor
                delegateTypeSufix, objectTypeName, valueTypeNameConstructorArgFormatted, uniqueIdentifier, propertyName); // constructor of delegate
    }

}
