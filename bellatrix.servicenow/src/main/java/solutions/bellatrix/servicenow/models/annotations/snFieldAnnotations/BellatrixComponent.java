package solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import solutions.bellatrix.web.components.WebComponent;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BellatrixComponent {
    Class<? extends WebComponent> value();
}