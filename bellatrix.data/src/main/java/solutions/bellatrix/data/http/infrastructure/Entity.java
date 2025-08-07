package solutions.bellatrix.data.http.infrastructure;

import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

@SuperBuilder
@SuppressWarnings("unchecked")
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