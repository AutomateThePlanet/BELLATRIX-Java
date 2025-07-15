package solutions.bellatrix.data.contracts;

public interface Repository<T extends Entity> {
    T getById(T entity);

    T createEntity(T entity);
}