package solutions.bellatrix.web.components.advanced;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.table.TableCell;
import solutions.bellatrix.web.components.contracts.Component;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.findstrategies.FindStrategy;

import javax.swing.text.TableView;
import java.util.List;
import java.util.stream.Collectors;

public class TableHeaderRow extends WebComponent implements ComponentHtml {
    public List<TableCell> getHeaderCells() {
        return this.createAllByTag(TableCell.class, "th");
//        return this.createAllByTag(TableCell.class, "th", true).stream().map(e -> new TableCell(e)).collect(Collectors.toList());
    }

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    @Override
    public String getComponentName() {
        return null;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Class<?> getComponentClass() {
        return null;
    }

    @Override
    public WebElement getWrappedElement() {
        return null;
    }

    @Override
    public FindStrategy getFindStrategy() {
        return null;
    }
}
