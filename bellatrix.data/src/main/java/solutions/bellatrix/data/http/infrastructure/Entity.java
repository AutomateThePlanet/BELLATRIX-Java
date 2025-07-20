package solutions.bellatrix.data.http.infrastructure;

import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

/**
 * The Entity class represents a base entity that can be persisted in a repository.
 * It provides methods to get, create, update, and delete the entity.
 *
 * @param <TIdentifier> the type of the identifier for the entity
 * @param <TEntity>     the type of the entity itself
 */
@SuperBuilder
public abstract class Entity<TIdentifier, TEntity> {
    public TEntity get() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return (TEntity)repository.getById(this);
    }

    public TEntity create() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return (TEntity)repository.create(this);
    }

    public TEntity update() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return (TEntity)repository.update(this);
    }

    public void delete() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        repository.delete(this);
    }

    public abstract TIdentifier getIdentifier();
}