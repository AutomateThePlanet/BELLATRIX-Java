package solutions.bellatrix.data.http.infrastructure;

import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.data.annotations.Dependency;
import solutions.bellatrix.data.configuration.RepositoryProvider;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.contracts.EntityFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility class for automatically resolving entity dependencies.
 * This class handles the creation of dependent entities when fields
 * are marked with @Dependency annotations using recursive resolution
 * to handle complex dependency chains.
 */
public class DependencyResolver {
    
    // Track entities being resolved to prevent circular dependencies
    private static final Set<Class<?>> buildingEntities = new HashSet<>();
    private static final Set<Class<?>> creatingEntities = new HashSet<>();
    private static final Set<Class<?>> deletingEntities = new HashSet<>();
    
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
    public static <T> T buildDependencies(T entity) {
        if (entity == null) {
            return null;
        }
        
        // Clear the resolving set for each top-level call
        buildingEntities.clear();
        return buildDependenciesRecursive(entity);
    }

    public static <T> T createDependencies(T entity) {
        if (entity == null) {
            return null;
        }

        // Clear the resolving set for each top-level call
        creatingEntities.clear();
        return createDependenciesRecursive(entity);
    }

    public static <T> void deleteDependencies(T entity) {
        if (entity == null) return;
        deletingEntities.clear();
        deleteDependenciesRecursive(entity);
    }

    /**
     * Recursively resolves dependencies for an entity, ensuring that
     * dependencies are created in the correct order (bottom-up).
     * 
     * @param entity The entity to resolve dependencies for
     * @param <T> The entity type
     * @return The entity with all dependencies resolved
     */
    private static <T> T buildDependenciesRecursive(T entity) {
        if (entity == null) {
            return null;
        }

        Class<?> entityClass = entity.getClass();
        Log.info("Building dependencies for entity: " + entityClass.getSimpleName());

        // Check for circular dependency
        if (buildingEntities.contains(entityClass)) {
            throw new RuntimeException("Circular dependency detected for entity: " + entityClass.getSimpleName());
        }
        
        // Add to resolving set
        buildingEntities.add(entityClass);
        
        try {
            // Get all fields with @Dependency annotations
            List<Field> dependencyFields = getDependencyFields(entityClass);
            
            // Resolve each dependency field
            for (Field field : dependencyFields) {
                buildFieldDependencyRecursive(entity, field);
            }
            
            return entity;
        } finally {
            // Remove from resolving set
            buildingEntities.remove(entityClass);
        }
    }

    private static <T> T createDependenciesRecursive(T entity) {
        if (entity == null) {
            return null;
        }

        Class<?> entityClass = entity.getClass();

        // Check for circular dependency
        if (creatingEntities.contains(entityClass)) {
            throw new RuntimeException("Circular dependency detected for entity: " + entityClass.getSimpleName());
        }

        // Add to resolving set
        creatingEntities.add(entityClass);

        try {
            // Get all fields with @Dependency annotations
            List<Field> dependencyFields = getDependencyFields(entityClass);

            // Resolve each dependency field
            for (Field field : dependencyFields) {
                createFieldDependencyRecursive(entity, field);
            }

            return entity;
        } finally {
            // Remove from resolving set
            creatingEntities.remove(entityClass);
        }
    }

    private static <T> T deleteDependenciesRecursive(T entity) {
        if (entity == null) {
            return null;
        }

        Class<?> entityClass = entity.getClass();

        // Check for circular dependency
        if (deletingEntities.contains(entityClass)) {
            throw new RuntimeException("Circular dependency detected for entity: " + entityClass.getSimpleName());
        }

        // Add to resolving set
        deletingEntities.add(entityClass);

        try {
            // Get all fields with @Dependency annotations
            List<Field> dependencyFields = getDependencyFields(entityClass);

            // Resolve each dependency field
            for (Field field : dependencyFields) {
                deleteFieldDependencyRecursive(entity, field);
            }

            return entity;
        } finally {
            // Remove from resolving set
            deletingEntities.remove(entityClass);
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
    private static void buildFieldDependencyRecursive(Object entity, Field field) {
        try {
            field.setAccessible(true);
            Dependency dependencyAnnotation = field.getAnnotation(Dependency.class);
            Object currentValue = field.get(entity);
            
            // Skip if field already has a value and forceCreate is false
            if (currentValue != null && !dependencyAnnotation.forceCreate()) {
                return;
            }

            Log.info("Building dependency for field '" + field.getName() + "' of type '" + dependencyAnnotation.entityType().getSimpleName() + "'.");
            // Create the dependency entity using the factory
            Object dependencyEntity = buildDependencyEntity(dependencyAnnotation);
            
            // Recursively resolve dependencies for the dependency entity
            // This ensures that all dependencies are resolved bottom-up
            dependencyEntity = buildDependenciesRecursive(dependencyEntity);
//
//            // Get the factory directly using the entity type from the annotation
//            @SuppressWarnings("unchecked")
//            EntityFactory<Entity> factory = (EntityFactory<Entity>) FactoryProvider.INSTANCE.get((Class<? extends Entity>) dependencyAnnotation.entityType());
//
//            // Create the fully resolved entity in the factory (this will set the ID)
//            Entity builtEntity = factory.buildDefault();

            // Set the ID in the field
            Log.info("Setting dependency field '" + field.getName() + "' with ID from built entity.");

            if(trySetViaSetter(entity, field, dependencyEntity)) {
                Log.info("Set the dependency field '" + field.getName() + "' via setter.");
            } else {
                field.set(entity, dependencyEntity);
                Log.info("Set the dependency field '" + field.getName() + "' directly.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve dependency for field: " + field.getName(), e);
        }
    }

    private static void createFieldDependencyRecursive(Object entity, Field field) {
        try {
            field.setAccessible(true);
            Dependency dependencyAnnotation = field.getAnnotation(Dependency.class);
            Entity currentValue = (Entity)field.get(entity);

            // Skip if field already has a value and forceCreate is false
            if (currentValue != null && !dependencyAnnotation.forceCreate() && currentValue.getIdentifier() != null) {
                return;
            }

            // Create the dependency entity using the repository
            Object builtDependencyEntity = currentValue == null ? buildDependencyEntity(dependencyAnnotation) : currentValue;

            // Recursively resolve dependencies for the dependency entity
            // This ensures that all dependencies are resolved bottom-up
            builtDependencyEntity = createDependenciesRecursive(builtDependencyEntity);

            // Get the repository directly using the entity type from the annotation
            @SuppressWarnings("unchecked")
            Repository<Entity> repository = (Repository<Entity>) RepositoryProvider.INSTANCE.get((Class<? extends Entity>) dependencyAnnotation.entityType());

            Log.info("Creating dependency entity for field '" + field.getName() + "' of type '" + dependencyAnnotation.entityType().getSimpleName() + "'.");
            Entity createdEntity = repository.create((Entity) builtDependencyEntity);
            ((Entity) builtDependencyEntity).setIdentifier((String)createdEntity.getIdentifier());

            if (trySetViaSetter(entity, field, builtDependencyEntity)) {
                Log.info("Set the dependency field '" + field.getName() + "' via setter.");
            } else {
                field.set(entity, builtDependencyEntity);
                Log.info("Set the dependency field '" + field.getName() + "' directly.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve dependency for field: " + field.getName(), e);
        }
    }

    private static void deleteFieldDependencyRecursive(Object entity, Field field) {
        try {
            field.setAccessible(true);
            Dependency dependencyAnnotation = field.getAnnotation(Dependency.class);
            Entity currentValue = (Entity)field.get(entity);

            // Skip if field already has a value and forceCreate is false
            if (currentValue == null || currentValue.getIdentifier() == null) {
                return;
            }

            // Create the dependency entity using the repository
            Object builtDependencyEntity = currentValue == null ? buildDependencyEntity(dependencyAnnotation) : currentValue;

            // Get the repository directly using the entity type from the annotation
            @SuppressWarnings("unchecked")
            Repository<Entity> repository = (Repository<Entity>) RepositoryProvider.INSTANCE.get((Class<? extends Entity>) dependencyAnnotation.entityType());

            Log.info("Deleting dependency entity for field '" + field.getName() + "' of type '" + dependencyAnnotation.entityType().getSimpleName() + "'.");
            repository.delete((Entity) builtDependencyEntity);

            // Recursively resolve dependencies for the dependency entity
            // This ensures that all dependencies are resolved bottom-up
            deleteDependenciesRecursive(builtDependencyEntity);


        } catch (Exception e) {
            throw new RuntimeException("Failed to delete dependency for field: " + field.getName(), e);
        }
    }
    
    /**
     * Creates a dependency entity using the specified factory method.
     * 
     * @param dependencyAnnotation The dependency annotation containing creation info
     * @return The created dependency entity
     */
    private static Object buildDependencyEntity(Dependency dependencyAnnotation) {
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

    private static boolean trySetViaSetter(Object target, Field field, Object value) {
        Class<?> clazz = target.getClass();
        Class<?> fieldType = field.getType();
        List<String> candidates = new ArrayList<>();

        String fieldName = field.getName();
        String cap = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        // Conventional JavaBean setter
        candidates.add("set" + cap);

        // Boolean field named like "isActive" → setter "setActive"
        if ((fieldType == boolean.class || fieldType == Boolean.class)
                && fieldName.startsWith("is")
                && fieldName.length() > 2
                && Character.isUpperCase(fieldName.charAt(2))) {
            candidates.add("set" + fieldName.substring(2));
        }

        // Fluent/Lombok-style setter (e.g., ".name(value)")
        candidates.add(fieldName);

        // Try to find a compatible method (single parameter assignable from value)
        for (String name : candidates) {
            for (Method m : clazz.getMethods()) { // includes inherited public methods
                if (!m.getName().equals(name) || m.getParameterCount() != 1) continue;
                Class<?> paramType = m.getParameterTypes()[0];
                if (value == null ? !paramType.isPrimitive() : paramType.isAssignableFrom(value.getClass())) {
                    try {
                        if (!m.canAccess(target)) m.setAccessible(true);
                        m.invoke(target, value);
                        return true;
                    } catch (ReflectiveOperationException ignore) {
                        // try next candidate
                    }
                }
            }
        }
        return false;
    }
}