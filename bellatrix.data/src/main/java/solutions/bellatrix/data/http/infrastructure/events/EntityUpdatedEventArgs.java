package solutions.bellatrix.data.http.infrastructure.events;

import solutions.bellatrix.data.http.infrastructure.Entity;

public class EntityUpdatedEventArgs extends EntityOperationsEventArgs {
    public EntityUpdatedEventArgs(Entity entity) {
        super(entity);
    }
}