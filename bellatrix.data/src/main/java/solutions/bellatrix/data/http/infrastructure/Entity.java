package solutions.bellatrix.data.http.infrastructure;

import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.configuration.RepositoryProvider;
import solutions.bellatrix.data.contracts.Repository;

@SuperBuilder
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

    public abstract TIdentifier getIdentifier();
    public abstract void setIdentifier(String id);
}