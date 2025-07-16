package solutions.bellatrix.data.http.infrastructure;

import com.google.gson.annotations.SerializedName;
import io.restassured.response.Response;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.contracts.Queryable;

import java.util.Objects;

@Data
@SuperBuilder
public abstract class HttpEntity implements Entity<String>, Queryable {
    private transient Response response;

    @SerializedName("id")
    private String id;

    @Override
    public String getIdentifier() {
        return id;
    }

    public boolean hasInvalidIdentifier() {
        return Objects.isNull(this.getIdentifier()) || this.getIdentifier().isEmpty();
    }
}