package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.EntityDeleterRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApiEntitiesDeleter {
    private final List<ApiEntity> entitiesToBeDeleted;
    private final EntityDeleterRepository entityDeleterRepository;

    public ApiEntitiesDeleter() {
        entitiesToBeDeleted = new ArrayList<>();
        entityDeleterRepository = new EntityDeleterRepository();
    }

    public void addEntity(ApiEntity entity) {
        entitiesToBeDeleted.add(entity);
    }

    public void addEntities(Collection<? extends ApiEntity> entities) {
        entitiesToBeDeleted.addAll(entities);
    }

    public void delete() {
        if (entitiesToBeDeleted.isEmpty()) {
            return;
        }

        var distinctEntitiesToBeDeleted = entitiesToBeDeleted.stream().distinct().toList();

        distinctEntitiesToBeDeleted.forEach(entityDeleterRepository::deleteAsync);
    }
}