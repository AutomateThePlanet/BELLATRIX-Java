package solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode()
public class AggregateEntities {
    @SerializedName("name")
    private List<AggregateField> entities;
}