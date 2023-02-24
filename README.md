<div align="center">
    <h1>Json Serializer</h1>
    <p>Java library for serializing any objects</p>
    <img src="https://www.johncalderaio.com/img/skills/json.png" height="300" alt="Logo">
</div>

### Usage

Use `JsonSerialize` annotation to mark fields that should be serialized. 

```java
import timnekk.JsonSerialize;

public class Example {
    @JsonSerialize
    private String name;
    @JsonSerialize
    private int age;
}
```

Add arguments to `JsonSerialize` annotation to change the name of the field in the output.

```java
import timnekk.JsonSerialize;

public class Example {
    @JsonSerialize("User name")
    private String name;
    @JsonSerialize("Amount of years")
    private int age;
}
```

Use `JsonSerializer.serialize()` to serialize objects.

```java
import timnekk.JsonSerialize;
import timnekk.JsonSerializer;
import timnekk.SerializationException;

class Example {
    @JsonSerialize
    private String name;
    @JsonSerialize("Amount of years")
    private int age;
}

public class App {
    public static void main(String[] args) throws SerializationException {
        Example example = new Example();
        example.name = "John";
        example.age = 20;
        
        String json = JsonSerializer.serialize(example);
        System.out.println(json);  // {"name":"John","Amount of years":20}
    }
}
```