package solutions.bellatrix.data.http.infrastructure;

import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;

public interface Entity<Т> {
    default Entity get() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.getById(this);
    }

    default Entity create() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.create(this);
    }

    default Entity update() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.update(this);
    }

    default void delete() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        repository.delete(this);
    }

    Т getIdentifier();
}