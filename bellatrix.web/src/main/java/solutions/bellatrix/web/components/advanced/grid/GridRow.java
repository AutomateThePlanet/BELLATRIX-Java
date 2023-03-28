package solutions.bellatrix.web.components.advanced.grid;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.services.ComponentCreateService;

import javax.lang.model.element.Element;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
public class GridRow extends WebComponent implements ComponentHtml {
    private Grid parentGrid;
    private int index;

    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    public GridCell getCell(int column) {
        return parentGrid.getCell(index, column);
    }

    public GridCell getCell(String headerName) {
        return parentGrid.getCell(headerName, index);
    }

    public <TComponent extends WebComponent> List<TComponent> getCells(Predicate<? super WebComponent> selector) throws InstantiationException, IllegalAccessException {
        return (List<TComponent>)getCells(WebComponent.class).stream().filter(selector).collect(Collectors.toList());
    }


    public List<GridCell> getCells() {
        List<GridCell> listOfCells = new ArrayList<>();
        var rowCells = parentGrid.getTableService().getRowCells(index);
        for (int rowCellsIndex = 0; rowCellsIndex < rowCells.size(); rowCellsIndex++) {
            var rowCell = rowCells.get(rowCellsIndex);
            String rowCellCssLocator = rowCell.cssSelector();
            GridCell cell = createByCss(GridCell.class, rowCellCssLocator);
            parentGrid.setCellMetaData(cell, index, rowCellsIndex);
            listOfCells.add(cell);
        }
        return listOfCells;
    }

    public <TComponent extends WebComponent> List<TComponent> getCells(Class<TComponent> componentType) throws InstantiationException, IllegalAccessException {
        List<TComponent> listOfElements = new ArrayList<>();
        List<GridCell> cells = getCells();
        for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
            GridCell cell = cells.get(columnIndex);
            TComponent element = null;
            if (cell.getCellControlComponentType() == null) {
                element = cell.as(componentType);
            } else {
                ComponentCreateService repo = new ComponentCreateService();
                element = repo.create(cell.getCellControlBy().getClass(), componentType);
                element.setParentWrappedElement(cell.getWrappedElement());
                // TODO: set caching to false
            }
            listOfElements.add(element);
        }
        return listOfElements;
    }

    public <TComponent extends WebComponent> List<TComponent> getCells(Class<TComponent> componentType, Predicate<TComponent> selector) throws InstantiationException, IllegalAccessException {
        return getCells(componentType).stream().filter(selector).toList();
    }

    public <TComponent extends WebComponent> TComponent getFirstOrDefaultCell(Class<TComponent> componentType, Predicate<TComponent> selector) throws InstantiationException, IllegalAccessException {
        return getCells(componentType, selector).stream().findFirst().orElse(null);
    }

    public <T> T getItem(Class<T> clazz) throws IllegalAccessException, InstantiationException, IntrospectionException {
        return parentGrid.castRow(getIndex());
    }

    public <T> void assertRow(T expectedItem, String... propertiesNotToCompare) throws IllegalAccessException, InstantiationException, IntrospectionException {
        T actualItem = getItem((Class<T>) expectedItem.getClass());

        EntitiesAsserter.assertAreEqual(expectedItem, actualItem, propertiesNotToCompare);
    }

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }
}
