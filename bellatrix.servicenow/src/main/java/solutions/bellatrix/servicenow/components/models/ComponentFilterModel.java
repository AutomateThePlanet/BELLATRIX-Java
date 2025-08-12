package solutions.bellatrix.servicenow.components.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ComponentFilterModel {
    @SerializedName("groupOperation")
    private String groupOperation;
    @SerializedName("groupedFilters")
    private ComponentFilterGroupModel[] vulnerability;
}