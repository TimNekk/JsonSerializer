package timnekk;

import java.lang.reflect.Field;

public final class JsonSerializer {
    private JsonSerializer() {
    }

    public static String serialize(Object object) throws SerializationException {
        StringBuilder output = new StringBuilder();
        output.append("{");

        for (Field field : object.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(JsonSerialize.class)) {
                addFieldToStringBuilder(object, output, field);
            }
        }

        tryRemoveLastComma(output);
        output.append("}");

        return output.toString();
    }

    private static void tryRemoveLastComma(StringBuilder output) {
        if (output.charAt(output.length() - 1) == ',') {
            output.deleteCharAt(output.length() - 1);
        }
    }

    private static void addFieldToStringBuilder(Object object, StringBuilder output, Field field) throws SerializationException {
        JsonSerialize annotation = field.getAnnotation(JsonSerialize.class);

        field.setAccessible(true);
        Object value;

        try {
            value = field.get(object);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new SerializationException("Failed to get field value", e);
        }

        String name = getFieldName(field, annotation);
        output.append("\"").append(name).append("\":");

        if (isPrimitive(value)) {
            output.append(value);
        } else {
            output.append("\"").append(value).append("\"");
        }

        output.append(",");
    }

    private static String getFieldName(Field field, JsonSerialize annotation) {
        String name = annotation.value();

        if (name.isEmpty()) {
            name = field.getName();
        }

        return name;
    }

    private static boolean isPrimitive(Object value) {
        return value instanceof Integer ||
                value instanceof Long ||
                value instanceof Double ||
                value instanceof Float ||
                value instanceof Short ||
                value instanceof Byte ||
                value instanceof Boolean;
    }
}
