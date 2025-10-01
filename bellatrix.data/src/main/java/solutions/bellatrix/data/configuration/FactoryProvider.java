package solutions.bellatrix.data.configuration;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.data.http.contracts.EntityFactory;
import solutions.bellatrix.data.http.infrastructure.Entity;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public enum FactoryProvider {
    INSTANCE;

    private final Map<Class<? extends Entity>, Class<? extends EntityFactory>> factories = new ConcurrentHashMap<>();

    public <T extends Entity> void register(Class<T> entityClass, Class<? extends EntityFactory<T>> factoryClass) {
        factories.put(entityClass, factoryClass);
    }

    public <T extends Entity> EntityFactory<T> get(Class<T> entityClass) {
        var factoryClassType = factories.get(entityClass);

        if (Objects.isNull(factoryClassType)) {
            throw new IllegalArgumentException("No factory registered for entity class: " + entityClass.getName());
        }

        return (EntityFactory<T>)SingletonFactory.getInstance(factoryClassType);
    }
}