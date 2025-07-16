package solutions.bellatrix.data.contracts;

import com.google.gson.annotations.SerializedName;
import io.restassured.response.Response;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@SuperBuilder
public abstract class HttpEntity extends Entity implements Queryable {
    @SerializedName("id")
    private String id;

    private transient Response response;

    @Override
    public String getIdentifier() {
        return id;
    }

    public boolean hasInvalidIdentifier() {
        return Objects.isNull(this.getIdentifier()) || this.getIdentifier().isEmpty();
    }
}