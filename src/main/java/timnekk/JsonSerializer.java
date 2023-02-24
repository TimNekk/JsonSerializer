package timnekk;

import java.lang.reflect.Field;

public final class JsonSerializer {
    private JsonSerializer() {
    }

    /**
     * Serializes object to JSON string.
     * <p>
     * Only fields with {@link JsonSerialize} annotation will be serialized.
     * If annotation has value, it will be used as key in JSON string.
     * Otherwise, field name will be used as key.
     * <p>
     * If field value is primitive, it will be serialized as is.
     * Otherwise, it will be serialized as string.
     * <p>
     * If field value is null, it will be serialized as null.
     *
     * @param object Object to serialize.
     * @return JSON string.
     * @throws SerializationException If serialization fails.
     * @throws IllegalArgumentException If object is null.
     */
    public static String serialize(Object object) throws SerializationException {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        StringBuilder output = new StringBuilder();
        output.append("{");

        for (Field field : object.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(JsonSerialize.class)) {
                continue;
            }

            addFieldToStringBuilder(object, output, field);
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
                value instanceof Boolean ||
                value == null;
    }
}
