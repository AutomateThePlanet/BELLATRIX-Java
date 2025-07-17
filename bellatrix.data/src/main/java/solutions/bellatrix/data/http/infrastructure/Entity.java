package solutions.bellatrix.data.http.infrastructure;

import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

@SuperBuilder
public abstract class Entity<Т> {
    public Entity get() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.getById(this);
    }

    public Entity create() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.create(this);
    }

    public Entity update() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.update(this);
    }

    public void delete() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        repository.delete(this);
    }

    public abstract Т getIdentifier();
}