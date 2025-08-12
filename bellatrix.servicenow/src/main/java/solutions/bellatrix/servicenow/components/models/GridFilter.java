package solutions.bellatrix.servicenow.components.models;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.components.enums.OperationsAttributes;

import java.util.List;

@Data
@SuperBuilder
public class GridFilter {
    private String gridColumnHeader;
    private OperationsAttributes operation;
    private List<String> conditionValue;
}