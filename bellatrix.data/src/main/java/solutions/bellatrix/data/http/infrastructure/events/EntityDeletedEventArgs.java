package solutions.bellatrix.data.http.infrastructure.events;

import solutions.bellatrix.data.http.infrastructure.Entity;

public class EntityDeletedEventArgs extends EntityOperationsEventArgs {
    public EntityDeletedEventArgs(Entity entity) {
        super(entity);
    }
}