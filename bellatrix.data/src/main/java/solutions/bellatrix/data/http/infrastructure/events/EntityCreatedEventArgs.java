package solutions.bellatrix.data.http.infrastructure.events;

import solutions.bellatrix.data.http.infrastructure.Entity;

public class EntityCreatedEventArgs extends EntityOperationsEventArgs {
    public EntityCreatedEventArgs(Entity entity) {
        super(entity);
    }
}