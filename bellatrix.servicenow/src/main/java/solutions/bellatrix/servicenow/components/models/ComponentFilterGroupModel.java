package solutions.bellatrix.servicenow.components.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ComponentFilterGroupModel {
    @SerializedName("operation")
    private String operation;
    @SerializedName("condition")
    private String condition;
    @SerializedName("operationType")
    private String operationType;
    @SerializedName("filterValue")
    private String filterValue;
}