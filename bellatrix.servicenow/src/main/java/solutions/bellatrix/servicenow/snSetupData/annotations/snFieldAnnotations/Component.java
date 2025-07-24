package solutions.bellatrix.servicenow.snSetupData.annotations.snFieldAnnotations;

import solutions.bellatrix.servicenow.components.serviceNow.SnDefaultComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Component {
    Class<? extends SnDefaultComponent> value();
}