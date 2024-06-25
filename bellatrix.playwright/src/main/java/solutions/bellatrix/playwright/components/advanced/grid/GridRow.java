package solutions.bellatrix.playwright.components.advanced.grid;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.core.utilities.PropertyReference;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.components.contracts.ComponentHtml;
import solutions.bellatrix.playwright.findstrategies.FindStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Getter @Setter
public class GridRow extends WebComponent implements ComponentHtml {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    private Grid parentGrid;
    private int index;

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }

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

    public <TDto> GridCell getCell(Class<TDto> dtoClass, PropertyReference<TDto> expression) {
        return parentGrid.getCell(dtoClass, expression, index);
    }

    public List<GridCell> getCells() {
        var listOfCells = new ArrayList<GridCell>();
        var rowCells = parentGrid.getTableService().getRowCells(index);

        for (int rowCellsIndex = 0; rowCellsIndex < rowCells.size(); rowCellsIndex++) {
            var rowCellXpath = HtmlService.getAbsoluteXPath(rowCells.get(index));
            var cell = this.create().byXpath(GridCell.class, rowCellXpath);
            parentGrid.setCellMetaData(cell, index, rowCellsIndex);
            listOfCells.add(cell);
        }

        return listOfCells;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <TComponent extends WebComponent> List<TComponent> getCells(Class<TComponent> clazz) {
        var listOfElements = new ArrayList<TComponent>();
        var cells = getCells();
        for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
            var cell = cells.get(columnIndex);
            if (cell.getCellControlComponentType() == null) {
                listOfElements.add(cell.as(clazz));
            } else {
                var createMethod = WebComponent.class.getDeclaredMethod("create", Class.class, FindStrategy.class);
                createMethod.setAccessible(true);
                var element = (TComponent)createMethod.invoke(cell, cell.getCellControlComponentType(), cell.getCellControlFindStrategy());
                listOfElements.add(element);
            }
        }

        return listOfElements;
    }

    public <TComponent extends WebComponent> List<TComponent> getCells(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getCells(clazz).stream().filter((component) -> component.getClass().equals(clazz)).filter(selector).toList();
    }

    public <TComponent extends WebComponent> TComponent getFirstOrDefault(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getCells(clazz, selector).stream().findFirst().orElse(null);
    }

    public <T> T getItem(Class<T> clazz, String... fieldsToSkip) {
        return parentGrid.castRow(clazz, index, fieldsToSkip);
    }

    public <T> void assertRow(Class<T> clazz, T expectedItem, String... fieldsNotToCompare) {
        var actualItem = getItem(clazz, fieldsNotToCompare);

        EntitiesAsserter.assertAreEqual(expectedItem, actualItem, fieldsNotToCompare);
    }

    public <T> void assertRow(Class<T> clazz, T expectedItem) {
        List<String> propsNotToCompare = Arrays.stream(expectedItem.getClass().getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(expectedItem) == null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(field -> parentGrid.getHeaderNamesService().getHeaderNameByField(field))
                .toList();

        assertRow(clazz, expectedItem, propsNotToCompare.toArray(new String[0]));
    }
}
