package solutions.bellatrix.data.contracts;

import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.configuration.RepositoryFactory;

@SuperBuilder
public abstract class Entity {
    public Entity create() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.createEntity(this);
    }

    public Entity getById() {
        var repository = (Repository<Entity>)RepositoryFactory.INSTANCE.getRepository(this.getClass());
        return repository.getById(this);
    }

    abstract String getIdentifier();
}