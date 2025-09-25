package solutions.bellatrix.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields that require automatic dependency creation.
 * When a field is marked with this annotation and its value is null,
 * the system will automatically create the dependent entity using
 * the registered factory and repository.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
    
    /**
     * The entity class that should be created as a dependency.
     * This class must have a registered factory and repository.
     */
    Class<?> entityType();
    
    /**
     * Optional: Custom factory method name to use for creating the dependency.
     * If not specified, the default factory method will be used.
     */
    String factoryMethod() default "createDefault";
    
    /**
     * Optional: Whether to create the dependency even if the field is not null.
     * Default is false (only create if field is null).
     */
    boolean forceCreate() default false;
}
