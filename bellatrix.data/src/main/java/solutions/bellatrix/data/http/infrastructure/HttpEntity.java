package solutions.bellatrix.data.http.infrastructure;

import io.restassured.response.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.contracts.Queryable;

import java.util.Objects;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class HttpEntity<T> extends Entity<T> implements Queryable {
    private transient Response response;

    public boolean hasInvalidIdentifier() {
        return Objects.isNull(this.getIdentifier());
    }
}