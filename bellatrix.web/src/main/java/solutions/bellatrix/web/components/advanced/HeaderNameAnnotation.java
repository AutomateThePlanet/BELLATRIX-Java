package solutions.bellatrix.web.components.advanced;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HeaderNameAnnotation {
    String name();
    int order() default 0;
}
