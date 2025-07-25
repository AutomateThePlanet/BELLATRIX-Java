package solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode()
public class AggregateStats {
    @SerializedName("count")
    private Integer count;

    @SerializedName("sum")
    private AggregateEntities sum;
}