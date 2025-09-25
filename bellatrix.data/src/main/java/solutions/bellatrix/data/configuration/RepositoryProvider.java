package solutions.bellatrix.data.configuration;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.infrastructure.Entity;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public enum RepositoryProvider {
    INSTANCE;

    private final Map<Class<? extends Entity>, Class<? extends Repository>> repositories = new ConcurrentHashMap<>();

    public <T extends Entity> void register(Class<T> entityClass, Class<? extends Repository<T>> repositoryClass) {
        repositories.put(entityClass, repositoryClass);
    }

    public <T extends Entity> Repository<T> get(Class<T> entityClass) {
        var repositoryClassType = repositories.get(entityClass);

        if (Objects.isNull(repositoryClassType)) {
            throw new IllegalArgumentException("No repository registered for entity class: " + entityClass.getName());
        }

        return (Repository<T>)SingletonFactory.getInstance(repositoryClassType);
    }
}