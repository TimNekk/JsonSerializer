package timnekk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation for fields that should be serialized.
 * <p>
 * If annotation has value, it will be used as key in JSON string.
 * Otherwise, field name will be used as key.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonSerialize {
    String value() default "";
}
