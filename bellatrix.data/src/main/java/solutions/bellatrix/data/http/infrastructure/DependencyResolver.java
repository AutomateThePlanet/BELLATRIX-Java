package solutions.bellatrix.data.http.infrastructure;

import solutions.bellatrix.data.annotations.Dependency;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.contracts.EntityFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utility class for automatically resolving entity dependencies.
 * This class handles the creation of dependent entities when fields
 * are marked with @Dependency annotations using recursive resolution
 * to handle complex dependency chains.
 */
public class DependencyResolver {
    
    // Track entities being resolved to prevent circular dependencies
    private static final Set<Class<?>> resolvingEntities = new HashSet<>();
    
    /**
     * Resolves all dependencies for the given entity by creating
     * dependent entities where fields are marked with @Dependency
     * and the field value is null. Uses recursive resolution to handle
     * complex dependency chains.
     * 
     * @param entity The entity to resolve dependencies for
     * @param <T> The entity type
     * @return The entity with all dependencies resolved
     */
    public static <T> T resolveDependencies(T entity) {
        if (entity == null) {
            return null;
        }
        
        // Clear the resolving set for each top-level call
        resolvingEntities.clear();
        return resolveDependenciesRecursive(entity);
    }
    
    /**
     * Recursively resolves dependencies for an entity, ensuring that
     * dependencies are created in the correct order (bottom-up).
     * 
     * @param entity The entity to resolve dependencies for
     * @param <T> The entity type
     * @return The entity with all dependencies resolved
     */
    private static <T> T resolveDependenciesRecursive(T entity) {
        if (entity == null) {
            return null;
        }
        
        Class<?> entityClass = entity.getClass();
        
        // Check for circular dependency
        if (resolvingEntities.contains(entityClass)) {
            throw new RuntimeException("Circular dependency detected for entity: " + entityClass.getSimpleName());
        }
        
        // Add to resolving set
        resolvingEntities.add(entityClass);
        
        try {
            // Get all fields with @Dependency annotations
            List<Field> dependencyFields = getDependencyFields(entityClass);
            
            // Resolve each dependency field
            for (Field field : dependencyFields) {
                resolveFieldDependencyRecursive(entity, field);
            }
            
            return entity;
        } finally {
            // Remove from resolving set
            resolvingEntities.remove(entityClass);
        }
    }
    
    /**
     * Gets all fields with @Dependency annotations from the given class.
     * 
     * @param entityClass The entity class to scan
     * @return List of fields with @Dependency annotations
     */
    private static List<Field> getDependencyFields(Class<?> entityClass) {
        List<Field> dependencyFields = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        
        for (Field field : fields) {
            if (field.getAnnotation(Dependency.class) != null) {
                dependencyFields.add(field);
            }
        }
        
        return dependencyFields;
    }
    
    /**
     * Recursively resolves a single field dependency by creating the required entity
     * and setting its ID in the field. This method ensures that the dependency entity
     * is fully resolved (including its own dependencies) before being created.
     * 
     * @param entity The parent entity
     * @param field The field that requires dependency resolution
     */
    private static void resolveFieldDependencyRecursive(Object entity, Field field) {
        try {
            field.setAccessible(true);
            Dependency dependencyAnnotation = field.getAnnotation(Dependency.class);
            Object currentValue = field.get(entity);
            
            // Skip if field already has a value and forceCreate is false
            if (currentValue != null && !dependencyAnnotation.forceCreate()) {
                return;
            }
            
            // Create the dependency entity using the factory
            Object dependencyEntity = createDependencyEntity(dependencyAnnotation);
            
            // Recursively resolve dependencies for the dependency entity
            // This ensures that all dependencies are resolved bottom-up
            dependencyEntity = resolveDependenciesRecursive(dependencyEntity);
            
            // Get the repository directly using the entity type from the annotation
            @SuppressWarnings("unchecked")
            Repository<Entity> repository = (Repository<Entity>) RepositoryFactory.INSTANCE.getRepository((Class<? extends Entity>) dependencyAnnotation.entityType());
            
            // Create the fully resolved entity in the repository (this will set the ID)
            Entity createdEntity = repository.create((Entity) dependencyEntity);
            
            // Extract the ID from the created entity
            String entityId = extractEntityId(createdEntity);
            
            // Set the ID in the field
            field.set(entity, entityId);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve dependency for field: " + field.getName(), e);
        }
    }
    
    /**
     * Creates a dependency entity using the specified factory method.
     * 
     * @param dependencyAnnotation The dependency annotation containing creation info
     * @return The created dependency entity
     */
    private static Object createDependencyEntity(Dependency dependencyAnnotation) {
        try {
            // Find the factory for the entity type
            EntityFactory<?> factory = findFactoryForEntity(dependencyAnnotation.entityType());
            if (factory == null) {
                throw new IllegalStateException("No factory found for entity type: " + dependencyAnnotation.entityType().getSimpleName());
            }
            
            // Use the specified factory method or default
            String methodName = dependencyAnnotation.factoryMethod();
            Method factoryMethod = factory.getClass().getMethod(methodName);
            
            return factoryMethod.invoke(factory);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create dependency entity", e);
        }
    }
    
    /**
     * Finds the factory for the given entity type by looking for
     * factory classes that implement EntityFactory.
     * 
     * @param entityType The entity class to find a factory for
     * @return The factory instance or null if not found
     */
    private static EntityFactory<?> findFactoryForEntity(Class<?> entityType) {
        String entityName = entityType.getSimpleName();
        String factoryClassName = entityName + "RepositoryFactory";
        
        try {
            // Try to find the factory in the same package as the entity
            String packageName = entityType.getPackage().getName();
            Class<?> factoryClass = Class.forName(packageName + "." + factoryClassName);
            
            // For static classes, create a temporary instance to call methods
            return (EntityFactory<?>) factoryClass.getDeclaredConstructor().newInstance();
            
        } catch (Exception e) {
            throw new RuntimeException("Factory not found for entity type: " + entityType.getSimpleName(), e);
        }
    }
    
    /**
     * Extracts the ID from an entity object by calling the getId() method.
     * 
     * @param entity The entity to extract ID from
     * @return The entity ID as a string
     */
    private static String extractEntityId(Object entity) {
        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract ID from entity: " + entity.getClass().getSimpleName(), e);
        }
    }
}