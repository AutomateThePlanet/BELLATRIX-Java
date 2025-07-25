package solutions.bellatrix.servicenow.components.data;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.components.data.enums.OperationsAttributes;

import java.util.List;

@Data
@SuperBuilder
public class GridFilter {
    private String gridColumnHeader;
    private OperationsAttributes operation;
    private List<String> conditionValue;
}