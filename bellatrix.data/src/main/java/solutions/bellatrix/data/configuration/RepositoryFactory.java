package solutions.bellatrix.data.configuration;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.contracts.Entity;
import solutions.bellatrix.data.contracts.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public enum RepositoryFactory {
    INSTANCE;

    private final Map<Class<? extends Entity>, Class<? extends Repository>> repositories = new ConcurrentHashMap<>();

    public <T extends Entity> void registerRepository(Class<T> entityClass, Class<? extends Repository<T>> repositoryClass) {
        repositories.put(entityClass, repositoryClass);
    }

    public <T extends Entity> Repository<T> getRepository(Class<T> entityClass) {
        Class<? extends Repository> repositoryClass = repositories.get(entityClass);
        if (Objects.isNull(repositoryClass)) {
            throw new IllegalArgumentException("No repository registered for entity class: " + entityClass.getName());
        }
        return (Repository<T>)SingletonFactory.getInstance(repositoryClass);
    }
}