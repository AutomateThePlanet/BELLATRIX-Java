package solutions.bellatrix.web.components.advanced;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeaderRowIndex {
    private String headerName;
    private int rowspan;
    private int colspan;
    private int rowIndex;
}
