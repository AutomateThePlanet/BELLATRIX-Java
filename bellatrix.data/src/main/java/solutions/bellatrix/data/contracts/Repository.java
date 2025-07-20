package solutions.bellatrix.data.contracts;

import solutions.bellatrix.data.http.infrastructure.Entity;

import java.util.List;

/**
 * The Repository interface defines the contract for a repository that manages entities.
 * It provides methods to get, create, update, and delete entities.
 *
 * @param <T> the type of entity managed by the repository
 */
public interface Repository<T extends Entity> {
    T getById(T entity);

    List<T> getAll();

    T create(T entity);

    T update(T entity);

    void delete(T entity);
}