package solutions.bellatrix.web.components.advanced.grid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.IHeaderInfo;
import solutions.bellatrix.web.findstrategies.FindStrategy;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlColumnData implements IHeaderInfo {
    private String headerName;
    private int order;
    private FindStrategy findStrategy;
    private Class componentType;

    public ControlColumnData(String headerName, int order) {
        this(headerName, order, null, null);
    }
}

