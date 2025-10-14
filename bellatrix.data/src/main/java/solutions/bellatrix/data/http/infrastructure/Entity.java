package solutions.bellatrix.data.http.infrastructure;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.annotations.Dependency;
import solutions.bellatrix.data.configuration.RepositoryProvider;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.contracts.EntityFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static solutions.bellatrix.data.http.infrastructure.DependencyResolver.getDependencyFields;

@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("unchecked")
public abstract class Entity<TIdentifier, TEntity> {
    public TEntity get() {
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        return (TEntity)repository.getById(this);
    }

    public TEntity create() {
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        return (TEntity)repository.create(this);
    }

    public TEntity createWithDependencies() {
        DependencyResolver.createDependencies(this);
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        this.setIdentifier((String)repository.create(this).getIdentifier());
        return (TEntity)this;
    }

    public TEntity update() {
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        return (TEntity)repository.update(this);
    }

    public void delete() {
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        repository.delete(this);
    }

    public void deleteDependenciesAndSelf() {
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        repository.delete(this);
        DependencyResolver.deleteDependencies(this);
    }

    public TEntity getWithDependencies() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        var record = repository.getById(this);
        List<Field> dependencyFields = getDependencyFields(record.getClass());

        for (Field field : dependencyFields) {
            field.setAccessible(true);
            Dependency dependencyAnnotation = field.getAnnotation(Dependency.class);

            Entity currentValue = (Entity)field.get(this);
            // Skip if field already has a value and forceCreate is false
            if (currentValue != null && !dependencyAnnotation.forceCreate()) {
                break;
            }

            Field f = record.getClass().getDeclaredField(String.format("id%s", Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1)));
            f.setAccessible(true);
            var dependencyId = f.get(record).toString();

            Class<?> fieldType = field.getType();
            EntityFactory<?> factory = DependencyResolver.findFactoryForEntity(fieldType);
            if (factory == null) {
                throw new IllegalStateException("No factory found for entity type: " + dependencyAnnotation.entityType().getSimpleName());
            }

            Entity newEntity = factory.buildDefault();

            newEntity.setIdentifier(dependencyId);

            field.set(record, newEntity.getWithDependencies());
        }

        return (TEntity) record;
    }

    public TEntity updateWithDependencies() {
        DependencyResolver.updateDependencies(this);
        var repository = (Repository<Entity>)RepositoryProvider.INSTANCE.get(this.getClass());
        this.setIdentifier((String)repository.update(this).getIdentifier());
        return (TEntity)this;
    }

    public abstract TIdentifier getIdentifier();
    public abstract void setIdentifier(String id);
}