package solutions.bellatrix.data.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum RepositoryFactory implements AutoCloseable {
    INSTANCE;
    private final Map<Class<?>, Object> repositoryMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getRepository(Class<T> repositoryClass) {
        T repository = (T)repositoryMap.get(repositoryClass);

        if (repository==null) {
            synchronized (this) {
                repository = (T)repositoryMap.get(repositoryClass);
                if (repository==null) {
                    repository = createRepository(repositoryClass);
                    repositoryMap.put(repositoryClass, repository);
                }
            }
        }

        return repository;
    }

    @SuppressWarnings("unchecked")
    private <T> T createRepository(Class<T> repositoryClass) {
        try {
            return repositoryClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create repository instance for " + repositoryClass.getName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        for (Object repository : repositoryMap.values()) {
            if (repository instanceof AutoCloseable) {
                ((AutoCloseable)repository).close();
            }
        }
    }
}