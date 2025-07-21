package solutions.bellatrix.data.contracts;

import solutions.bellatrix.data.http.infrastructure.Entity;

import java.util.List;

public interface Repository<T extends Entity> {
    T getById(T entity);

    List<T> getAll();

    T create(T entity);

    T update(T entity);

    void delete(T entity);
}