package timnekk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonSerializerTest {

    @Test
    void noAnnotationShouldReturnEmptyJson() throws SerializationException {
        class TestClass {
            private int field = 1;
        }

        TestClass testClass = new TestClass();
        String expected = "{}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void annotationWithEmptyValueShouldReturnJsonWithFieldNameAsKey() throws SerializationException {
        class TestClass {
            @JsonSerialize
            private int field = 1;
        }

        TestClass testClass = new TestClass();
        String expected = "{\"field\":1}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void annotationWithValueShouldReturnJsonWithAnnotationValueAsKey() throws SerializationException {
        class TestClass {
            @JsonSerialize("test")
            private int field = 1;
        }

        TestClass testClass = new TestClass();
        String expected = "{\"test\":1}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void fieldsWithAnyAccessModifierShouldBeSerialized() throws SerializationException {
        class TestClass {
            @JsonSerialize
            public int publicField = 1;
            @JsonSerialize
            protected int protectedField = 2;
            @JsonSerialize
            int packagePrivateField = 3;
            @JsonSerialize
            private int privateField = 4;
        }

        TestClass testClass = new TestClass();
        String expected = "{\"publicField\":1,\"protectedField\":2,\"packagePrivateField\":3,\"privateField\":4}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void fieldsWithPrimitiveTypesShouldBeSerializedWithoutQuotes() throws SerializationException {
        class TestClass {
            @JsonSerialize
            public int intField = 1;
            @JsonSerialize
            public long longField = 2;
            @JsonSerialize
            public double doubleField = 4.0;
            @JsonSerialize
            public float floatField = 3.0f;
            @JsonSerialize
            public short shortField = 5;
            @JsonSerialize
            public byte byteField = 6;
            @JsonSerialize
            public boolean booleanField = true;
        }

        TestClass testClass = new TestClass();
        String expected = "{\"intField\":1,\"longField\":2,\"doubleField\":4.0,\"floatField\":3.0,\"shortField\":5,\"byteField\":6,\"booleanField\":true}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void fieldsWithNonPrimitiveTypesShouldBeSerializedWithQuotes() throws SerializationException {
        class NonPrimitiveClass {
            @Override
            public String toString() {
                return "nonPrimitive";
            }
        }

        class TestClass {
            @JsonSerialize
            public String stringField = "test";
            @JsonSerialize
            public NonPrimitiveClass nonPrimitiveField = new NonPrimitiveClass();
        }

        TestClass testClass = new TestClass();
        String expected = "{\"stringField\":\"test\",\"nonPrimitiveField\":\"nonPrimitive\"}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void fieldsWithNullValueShouldBeSerializedAsNullWithoutQuotes() throws SerializationException {
        class TestClass {
            @JsonSerialize
            public String stringField = null;
        }

        TestClass testClass = new TestClass();
        String expected = "{\"stringField\":null}";
        String actual = JsonSerializer.serialize(testClass);
        assertEquals(expected, actual);
    }

    @Test
    void serializingNullShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> JsonSerializer.serialize(null));
    }
}