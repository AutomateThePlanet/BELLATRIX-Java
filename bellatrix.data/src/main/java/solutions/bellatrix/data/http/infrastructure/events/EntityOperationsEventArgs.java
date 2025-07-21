package solutions.bellatrix.data.http.infrastructure.events;

import lombok.Getter;
import solutions.bellatrix.data.http.infrastructure.Entity;

@Getter
public abstract class EntityOperationsEventArgs {
    private final Entity entity;

    protected EntityOperationsEventArgs(Entity entity) {
        this.entity = entity;
    }
}