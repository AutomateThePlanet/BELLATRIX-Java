package solutions.bellatrix.data.http.contracts;

import solutions.bellatrix.data.http.infrastructure.DependencyResolver;
import solutions.bellatrix.data.http.infrastructure.Entity;

public interface EntityFactory<T extends Entity> {
    
    T buildDefault();
    
    default T createWithDependencies() {
        T entity = buildDefault();
        return DependencyResolver.resolveDependencies(entity);
    }
}