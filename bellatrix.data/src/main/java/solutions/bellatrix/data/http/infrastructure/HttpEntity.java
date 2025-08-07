package solutions.bellatrix.data.http.infrastructure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.contracts.Queryable;

import java.util.Objects;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class HttpEntity<TIdentifier, TEntity> extends Entity<TIdentifier, TEntity> implements Queryable {
    private transient HttpResponse response;

    public boolean hasInvalidIdentifier() {
        return Objects.isNull(this.getIdentifier());
    }
}