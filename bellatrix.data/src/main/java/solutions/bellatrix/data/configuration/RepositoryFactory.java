package solutions.bellatrix.data.configuration;

import solutions.bellatrix.data.contracts.Entity;
import solutions.bellatrix.data.contracts.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum RepositoryFactory {
    INSTANCE;

    private final Map<Class<?>, Repository<?>> repositories = new ConcurrentHashMap<>();

    public <T extends Entity> void registerRepository(Class<?> entityClass, Repository<?> repository) {
        repositories.put(entityClass, repository);
    }

    public  Repository<?> getRepository(Class<?> entityClass) {
        Repository<?> repository =repositories.get(entityClass);
        if (repository==null) {
            throw new IllegalArgumentException("No repository registered for entity class: " + entityClass.getName());
        }
        return repository;
    }
}