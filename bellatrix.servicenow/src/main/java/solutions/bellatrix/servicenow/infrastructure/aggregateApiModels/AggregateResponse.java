package solutions.bellatrix.servicenow.infrastructure.aggregateApiModels;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode()
public class AggregateResponse {
    @SerializedName("stats")
    private AggregateStats stats;

    public Double getSumValue(String nameOfAggregateField) {
        return stats.getSum().getEntities().stream().filter(x -> x.getName().equals(nameOfAggregateField)).map(AggregateField::getValue).toList().get(0);
    }
}