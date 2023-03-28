package solutions.bellatrix.web.components.advanced;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderInfo implements IHeaderInfo {
    private String headerName;
    private int order;

    public HeaderInfo(String headerName) {
        this.headerName = headerName;
    }
}
