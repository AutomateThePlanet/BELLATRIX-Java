package solutions.bellatrix.web.components.advanced.grid;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.core.utilities.*;
import solutions.bellatrix.core.utilities.parsing.TypeParser;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.*;
import solutions.bellatrix.web.components.advanced.services.FooterService;
import solutions.bellatrix.web.components.advanced.services.HeaderNamesService;
import solutions.bellatrix.web.components.advanced.services.TableLocators;
import solutions.bellatrix.web.components.advanced.services.TableService;
import solutions.bellatrix.web.components.advanced.table.TableCell;
import solutions.bellatrix.web.components.advanced.table.TableHeaderRow;
import solutions.bellatrix.web.components.datahandlers.ControlDataHandler;
import solutions.bellatrix.web.findstrategies.FindStrategy;
import solutions.bellatrix.web.findstrategies.XPathFindStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Grid extends WebComponent {
    private TableService tableService;
    @Getter @Setter private List<ControlColumnData> controlColumnDataCollection;

    public TableService getTableService() {
        if (tableService == null) {
            waitUntilPopulated();
            var innerHtml = defaultGetInnerHtmlAttribute();
            tableService = new TableService(innerHtml);
        }

        return tableService;
    }

    private TableLocators locators() {
        return SingletonFactory.getInstance(TableLocators.class);
    }

    public HeaderNamesService getHeaderNamesService() {
        return new HeaderNamesService(getTableService().getHeaderRows());
    }

    public FooterService getFooterService() {
        return new FooterService(getTableService().getFooter());
    }

    public List<Button> getColumnHeaders() {
        var list = new ArrayList<Button>();
        for (var header : getTableService().getHeaders()) {
            var element = this.createByXPath(Button.class, HtmlService.getAbsoluteXpath(header));
            list.add(element);
        }

        return list;
    }

    public List<TableHeaderRow> getTableHeaderRows() {
        var list = new ArrayList<TableHeaderRow>();
        for (var headerRow : getTableService().getHeaderRows()) {
            var element = this.createByXPath(TableHeaderRow.class, HtmlService.getAbsoluteXpath(headerRow));
            list.add(element);
        }

        return list;
    }

    public void waitUntilPopulated() {
        Wait.forConditionUntilTimeout(() -> {
            var rows = this.createAllByXPath(Label.class, "." + locators().getRowsXpath());
            return rows != null && !rows.isEmpty();
        }, 3000, 500);
    }

    public List<String> getHeaderNames() {
        return getHeaderNamesService().getHeaderNames();
    }

    public void forEachHeader(Consumer<Button> action) {
        for (var header : getColumnHeaders()) {
            action.accept(header);
        }
    }

    public int rowsCount() {
        return getTableService().getRows().size();
    }

    public GridRow getRow(int rowIndex) {
        String xpath = HtmlService.getAbsoluteXpath(getTableService().getRow(rowIndex));
        GridRow row = this.createByXPath(GridRow.class, "." + xpath);
        row.setParentGrid(this);
        row.setIndex(rowIndex);

        return row;
    }
    
    public List<GridRow> getRows() {
        var rowsCount = getTableService().getRows().size();

        var list = new ArrayList<GridRow>(rowsCount);

        for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
            list.add(getRow(rowIndex));
        }

        return list;
    }

    public <TComponent extends WebComponent> List<GridRow> getRows(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getRows().stream().filter(r -> !r.getCells(clazz, selector).isEmpty()).toList();
    }

    public <TComponent extends WebComponent> GridRow getFirstOrDefaultRow(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getRows(clazz, selector).stream().findFirst().orElse(null);
    }

    public void forEachRow(Consumer<GridRow> action) {
        for (var gridRow : getTableService().getRows()) {
            String xpath = HtmlService.getAbsoluteXpath(gridRow);
            action.accept(this.createByXPath(GridRow.class, "." + xpath));
        }
    }

    public GridCell getCell(int row, int column) {
        String innerXpath = HtmlService.getAbsoluteXpath(getTableService().getCell(row, column));
        if (innerXpath.startsWith(".")) innerXpath = innerXpath.substring(1);
        String outerXpath = getCurrentElementXPath();
        String fullXpath = outerXpath + innerXpath;
        if (fullXpath.startsWith("///")) fullXpath = fullXpath.substring(1);
        GridCell cell = this.createByXPath(GridCell.class, fullXpath);
        setCellMetaData(cell, row, column);

        return cell;
    }

    public GridCell getCell(String header, int row) {
        Integer position = getHeaderNamesService().getHeaderPosition(header, controlColumnDataCollection);
        if (position == null) return null;

        return getCell(row, position);
    }

    public <TDto> GridCell getCell(Class<TDto> clazz, PropertyReference<TDto> expression, int row) {
        String headerName = getHeaderNamesService().getHeaderNameByExpression(clazz, expression);
        Integer position = getHeaderNamesService().getHeaderPosition(headerName, controlColumnDataCollection);
        if (position == null) return null;

        return getCell(row, position);
    }

    public void forEachCell(Consumer<GridCell> action) {
        var outerXpath = getCurrentElementXPath();
        for (var gridCell : getTableService().getCells()) {
            var fullXpath = outerXpath + HtmlService.getAbsoluteXpath(gridCell);
            action.accept(this.createByXPath(GridCell.class, "." + fullXpath));
        }
    }

    public <TComponent extends WebComponent> List<TComponent> getCells(Class<TComponent> clazz, Predicate<TComponent> selector) {
        var rows = getRows();

        var filteredCells = new ArrayList<TComponent>(rows.size());

        for (var gridRow : rows) {
            var currentFilteredCells = gridRow.getCells(clazz, selector);
            filteredCells.addAll(currentFilteredCells);
        }

        return filteredCells;
    }

    public int getFooterRowsCount() {
        return getFooterService().getRows().size();
    }

    public GridRow getFooterByName(String footerName) {
        int currentRowIndex = 0;
        for (var footerRow : getFooterService().getRows()) {
            if (Objects.equals((footerRow).selectXpath(".//").text(), footerName)) {
                return getFooterByPosition(currentRowIndex);
            }

            currentRowIndex++;
        }

        throw new IllegalArgumentException(String.format("Footer row %s was not found.", footerName));
    }

    public GridRow getFooterByPosition(int position) {
        String xpath = HtmlService.getAbsoluteXpath(getFooterService().getRows().get(position));
        GridRow row = this.createByXPath(GridRow.class, "." + xpath);
        row.setParentGrid(this);
        row.setIndex(position);

        return row;
    }

    public List<GridRow> getFooterRows() {
        var list = new ArrayList<GridRow>(getFooterRowsCount());

        for (int rowIndex = 0; rowIndex < getFooterRowsCount(); rowIndex++) {
            GridRow row = getFooterByPosition(rowIndex);
            list.add(row);
        }

        return list;
    }

    public <TComponent extends WebComponent> List<GridRow> getFooterRows(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getFooterRows().stream().filter(r -> !r.getCells(clazz, selector).isEmpty()).toList();
    }

    public <TComponent extends WebComponent> GridRow getFirstOrDefaultFooterRow(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getFooterRows(clazz, selector).stream().findFirst().orElse(null);
    }

    public void forEachFooterRow(Consumer<GridRow> action) {
        for (var gridRow : getFooterService().getRows()) {
            String xpath = HtmlService.getAbsoluteXpath(gridRow);
            action.accept(this.createByXPath(GridRow.class, "." + xpath));
        }
    }

    public <TComponent extends WebComponent> TComponent getFirstOrDefaultCell(Class<TComponent> clazz, Predicate<TComponent> selector) {
        return getCells(clazz, selector).stream().findFirst().orElse(null);
    }

    public <TComponent extends WebComponent> TComponent getLastOrDefaultCell(Class<TComponent> clazz, Predicate<TComponent> selector) {
        var cells = getCells(clazz, selector);
        if (cells == null || cells.isEmpty()) return null;

        return cells.get(cells.size() - 1);
    }

    public String getGridColumnNameByIndex(int index) {
        return getHeaderNamesService().getHeaderNames().get(index);
    }

    public int getGridColumnIndexByName(String columnName) {
        var coll = getHeaderNamesService().getHeaderNames();

        return coll.indexOf(columnName);
    }

    public List<GridCell> getColumn(int column) {
        var list = new ArrayList<GridCell>(rowsCount());

        for (int row = 0; row < rowsCount(); row++) {
            list.add(getCell(row, column));
        }

        return list;
    }

    public List<GridCell> getColumn(String header) {
        return getColumn(header, null);
    }

    public List<GridCell> getColumn(String header, Integer order) {
        Integer position = getHeaderNamesService().getHeaderPosition(header, controlColumnDataCollection, order);
        if (position == null) return new ArrayList<>();

        return getColumn(position);
    }

    public <TRowObject> void assertTable(Class<TRowObject> clazz, List<TRowObject> expectedEntities, String... fieldsNotToCompare) {
        scrollToVisible();
        Assertions.assertEquals(expectedEntities.size(), rowsCount(), String.format("Expected rows count %d but rows was %s", expectedEntities.size(), rowsCount()));

        for (int i = 0; i < rowsCount(); i++) {
            var entity = castRow(clazz, i, fieldsNotToCompare);
            EntitiesAsserter.areEqual(expectedEntities.get(i), entity, fieldsNotToCompare);
        }
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked"})
    public <TRowObject> void assertTable(Class<TRowObject> clazz, List<TRowObject> expectedEntities) {
        scrollToVisible();
        Assertions.assertEquals(expectedEntities.size(), rowsCount(), String.format("Expected rows count %d but rows was %s", expectedEntities.size(), rowsCount()));

        for (int i = 0; i < rowsCount(); i++) {
            int finalI = i;
            var propsNotToCompare = Arrays.stream(expectedEntities.get(i).getClass().getDeclaredFields())
                    .filter(f -> {
                        try {
                            f.setAccessible(true);
                            return f.get(expectedEntities.get(finalI)) == null;
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(f -> getHeaderNamesService().getHeaderNameByField(f))
                    .toArray(String[]::new);

            TRowObject entity;
            if (!clazz.equals(Object.class)) {
                entity = castRow(clazz, i, propsNotToCompare);
            } else {
                Method method = this.getClass().getMethod("castRow", int.class, List.class);
                entity = (TRowObject)method.invoke(this, i, Arrays.stream(propsNotToCompare).toList());
            }
            EntitiesAsserter.areEqual(expectedEntities.get(i), entity, propsNotToCompare);
        }
    }

    public void assertIsEmpty() {
        scrollToVisible();
        Assertions.assertTrue(getTableService().getRows().isEmpty(), String.format("Grid should be empty, but has %d rows.", rowsCount()));
    }

    public void assertIsNotEmpty() {
        scrollToVisible();
        Assertions.assertFalse(getTableService().getRows().isEmpty(), "Grid has no rows.");
    }

    public void assertRowsCount(int expectedRowsCount) {
        scrollToVisible();
        Assertions.assertEquals(expectedRowsCount, rowsCount(), String.format("Grid should have %d rows, but has %d rows.", expectedRowsCount, rowsCount()));
    }

    public <TRowObject> List<TRowObject> getItems(Class<TRowObject> clazz) {
        scrollToVisible();
        return getItems(clazz, getRows());
    }

    @SneakyThrows
    public <TRowObject> TRowObject castRow(Class<TRowObject> clazz, int rowIndex, String... fieldsToSkip) {
        var cells = getTableService().getRowCells(rowIndex);

        if(controlColumnDataCollection == null || controlColumnDataCollection.isEmpty()) {
            throw new IllegalArgumentException("The grid does not have a column model set!");
        }

        if (cells.size() != controlColumnDataCollection.size()) {

            // Compare headers to determine why the cells count is different
            var actual = getHeaderNamesService().getHeaderNames();
            var expected = controlColumnDataCollection.stream().map(HeaderInfo::getHeaderName).toList();

            Assertions.assertIterableEquals(expected, actual, String.format("Expected: %s%sActual: %s", expected, System.lineSeparator(), actual));
        }

        var dto = InstanceFactory.create(clazz);
        var fields = clazz.getFields();
        for (var field : fields) {
            var fieldType = field.getType();

            var headerInfo = getHeaderNamesService().getHeaderInfoByField(field);

            if (Arrays.stream(fieldsToSkip).anyMatch(f -> f.equals(headerInfo.getHeaderName()))) continue;

            Integer headerPosition = getHeaderNamesService().getHeaderPosition(headerInfo.getHeaderName(), controlColumnDataCollection, headerInfo.getOrder());
            if (headerPosition == null) continue;

            var controlData = getControlDataByField(field);
            if (controlData != null && controlData.getComponentClass() != null && WebComponent.class.isAssignableFrom(controlData.getComponentClass())) {
                var xpath = HtmlService.getAbsoluteXpath(cells.get(headerPosition));
                var tableCell = this.createByXPath(TableCell.class, "." + xpath);
                Object elementValue;
                if (controlData.getFindStrategy() == null) {
                    controlData.setFindStrategy(new XPathFindStrategy(xpath));
                    elementValue = getCellData(controlData, tableCell);
                    controlData.setFindStrategy(null);
                }
                else {
                    elementValue = getCellData(controlData, tableCell);
                }

                field.set(dto, TypeParser.parse(elementValue, fieldType));
            } else {
                String htmlNodeValue = StringEscapeUtils.unescapeHtml4((getTableService().getRowCells(rowIndex).get(headerPosition)).text().trim());
                field.set(dto, TypeParser.parse(htmlNodeValue, fieldType));
            }
        }

        return dto;
    }

    public void setCellMetaData(GridCell cell, int row, int column) {
        cell.setRow(row);
        cell.setColumn(column);

        if (controlColumnDataCollection == null || controlColumnDataCollection.isEmpty()) {
            throw new IllegalArgumentException("The grid does not have a column model set!");
        }

        var columnData = controlColumnDataCollection.get(column);
        var headerName = columnData.getHeaderName();
        var controlData = getControlColumnDataByHeaderName(controlColumnDataCollection, headerName);
        cell.setCellControlFindStrategy(controlData.getFindStrategy());
        cell.setCellControlComponentType(controlData.getComponentClass());
    }

    public <TRowObject, TRow extends GridRow> List<TRowObject> getItems(Class<TRowObject> clazz, List<TRow> rows) {
        var list = new ArrayList<TRowObject>();
        for (var row : rows) {
            var obj = castRow(clazz, row.getIndex());
            list.add(obj);
        }

        return list;
    }

    private ControlColumnData getControlDataByField(Field field) {
        ControlColumnData headerInfo;

        if (field.isAnnotationPresent(TableHeader.class)) {
            var headerNameAttribute = field.getAnnotation(TableHeader.class);
            headerInfo = controlColumnDataCollection.stream().filter(h -> Objects.equals(h.getHeaderName(), headerNameAttribute.name())).findFirst().orElse(null);
        } else {
            headerInfo = controlColumnDataCollection.stream().filter(h -> Objects.equals(h.getHeaderName(), field.getName())).findFirst().orElse(null);
        }

        return headerInfo;
    }

    @SneakyThrows
    private Object getCellData(ControlColumnData controlData, TableCell tableCell) {
        var createMethod = WebComponent.class.getDeclaredMethod("create", Class.class, FindStrategy.class);
        createMethod.setAccessible(true);
        var element = createMethod.invoke(tableCell, controlData.getComponentClass(), controlData.getFindStrategy());

        return ControlDataHandler.getData((WebComponent)element);
    }

    protected String getCurrentElementXPath() {
        String jsScriptText ="""
                    function createXPathFromElement(elm) {
                        var allNodes = document.getElementsByTagName('*');
                        for (var segs = []; elm && elm.nodeType === 1; elm = elm.parentNode) {
                            if (elm.hasAttribute('id')) {
                                var uniqueIdCount = 0;
                                for (var n = 0; n < allNodes.length; n++) {
                                    if (allNodes[n].hasAttribute('id') && allNodes[n].id === elm.id) uniqueIdCount++;
                                    if (uniqueIdCount > 1) break;
                                };
                                if (uniqueIdCount === 1) {
                                    segs.unshift('//*[@id=\\'' + elm.getAttribute('id') + '\\']');
                                    return segs.join('/');
                                }
                                else {
                                    segs.unshift('//' + elm.localName.toLowerCase() + '[@id=\\'' + elm.getAttribute('id') + '\\']');
                                }
                            }
                            else {
                                for (i = 1, sib = elm.previousSibling; sib; sib = sib.previousSibling) {
                                    if (sib.localName === elm.localName) i++;
                                };
                                segs.unshift(elm.localName.toLowerCase() + '[' + i + ']');
                            };
                        };
                        return segs.length ? '/' + segs.join('/') : null;
                    };
                    return createXPathFromElement(arguments[0]);
                    """;

        return getJavaScriptService().execute(jsScriptText, this);
    }

    private ControlColumnData getControlColumnDataByHeaderName(List<? extends HeaderInfo> controlColumnDataCollection, String headerName) {
        return (ControlColumnData)controlColumnDataCollection.stream().filter(x -> x.getHeaderName().equals(headerName)).findFirst().orElse(null);
    }

    private boolean anyControlColumnDataByHeaderName(List<? extends HeaderInfo> controlColumnDataCollection, String headerName) {
        return controlColumnDataCollection.stream().anyMatch(x -> x.getHeaderName().equals(headerName));
    }

    public Grid setColumn(String headerName) {
        if (controlColumnDataCollection == null) {
            controlColumnDataCollection = new ArrayList<>();
        }

        controlColumnDataCollection.add(new ControlColumnData(headerName));

        return this;
    }

    public Grid setColumns(List<String> headerNames) {
        if (headerNames == null || headerNames.isEmpty()) {
            return this;
        }

        if (controlColumnDataCollection == null) {
            controlColumnDataCollection = new ArrayList<>();
        }

        for (var headerName : headerNames) {
            controlColumnDataCollection.add(new ControlColumnData(headerName));
        }

        return this;
    }

    public <TComponent extends WebComponent> Grid setColumn(String headerName, Class<TComponent> type) {
        if (controlColumnDataCollection == null) {
            controlColumnDataCollection = new ArrayList<>();
        }

        controlColumnDataCollection.add(new ControlColumnData(headerName, null, type));

        return this;
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> Grid setColumn(String headerName, Class<TComponent> type, TFindStrategy controlInnerLocator) {
        if (controlColumnDataCollection == null) {
            controlColumnDataCollection = new ArrayList<ControlColumnData>();
        }

        var column = new ControlColumnData(headerName, controlInnerLocator, type);
        controlColumnDataCollection.add(column);

        return this;
    }

    public <TGridModel> Grid setModelColumns(Class<TGridModel> clazz) {
        controlColumnDataCollection = new ArrayList<>();
        for (var field : clazz.getFields()) {
            var headerName = field.isAnnotationPresent(TableHeader.class) ? field.getAnnotation(TableHeader.class).name() : field.getName();
            controlColumnDataCollection.add(new ControlColumnData(headerName));
        }

        return this;
    }
}
