package solutions.bellatrix.servicenow.infrastructure.facades;


import org.modelmapper.ModelMapper;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;
import solutions.bellatrix.servicenow.utilities.ApiEntitiesDeleter;

import java.util.Collection;

public class TestDataFacade {
    @Deprecated(since = "Use MapperUtility instead of this field.", forRemoval = true)
    protected final ModelMapper modelMapper;
    private final ApiEntitiesDeleter entitiesDeleter;

    public TestDataFacade() {
        entitiesDeleter = new ApiEntitiesDeleter();
        modelMapper = new ModelMapper();
    }

    protected void addEntityToBeDeleted(ApiEntity entity) {
        entitiesDeleter.addEntity(entity);
    }

    protected void addEntitiesToBeDeleted(Collection<? extends ApiEntity> entities) {
        entitiesDeleter.addEntities(entities);
    }

    public void deleteTestData() {
        entitiesDeleter.delete();
    }
}