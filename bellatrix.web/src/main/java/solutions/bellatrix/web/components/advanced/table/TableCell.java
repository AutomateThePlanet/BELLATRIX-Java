package solutions.bellatrix.web.components.advanced.table;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.components.contracts.ComponentText;

public class TableCell extends WebComponent implements ComponentText, ComponentHtml {
    @Getter @Setter private int column;
    @Getter @Setter private int row;

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }
}

