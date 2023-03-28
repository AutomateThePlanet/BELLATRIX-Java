package solutions.bellatrix.web.components.advanced.grid;

import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.core.utilities.Wait;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.*;
import solutions.bellatrix.web.components.advanced.table.TableCell;
import solutions.bellatrix.web.findstrategies.FindStrategy;
import solutions.bellatrix.web.services.ComponentCreateService;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Grid extends WebComponent {

    private static final String RowsXPathLocator = "//tr[descendant::td]";
    private TableService tableService;
    private List<HeaderInfo> controlColumnDataCollection;

    public Grid() {
        super();
    }

    public TableService getTableService() {
        if (tableService == null) {
            waitUntilPopulated();
            String innerHtml = getAttribute("innerHTML");
            tableService = new TableService(innerHtml);
        }
        return tableService;
    }

    public HeaderNamesService getHeaderNamesService() {
        return new HeaderNamesService(getTableService().getHeaderRows());
    }

    public FooterService getFooterService() {
        return new FooterService(getTableService().getFooter());
    }

    public List<HeaderInfo> getControlColumnDataCollection() {
        return controlColumnDataCollection;
    }

    public void setControlColumnDataCollection(List<HeaderInfo> controlColumnDataCollection) {
        this.controlColumnDataCollection = controlColumnDataCollection;
    }

    public List<Button> getColumnHeaders() {
        List<Button> list = new LinkedList<>();
        for (var node : getTableService().getHeaders()) {
            Button element = createByCss(Button.class, node.cssSelector());
            list.add(element);
        }
        return list;
    }

    public List<TableHeaderRow> getTableHeaderRows() {
        List<TableHeaderRow> list = new LinkedList<>();
        for (var header : getTableService().getHeaderRows()) {
            var element = createByCss(TableHeaderRow.class, header.cssSelector());
            list.add(element);
        }
        return list;
    }

    public void waitUntilPopulated() {
        Wait.forConditionUntilTimeout(() -> {
                    List<Label> rows = this.createAllByXPath(Label.class, RowsXPathLocator);
                    return rows != null && !rows.isEmpty();
                },
                3000, null, 3000);
    }

    public List<String> getHeaderNames() {
        return getHeaderNamesService().getHeaderNames();
    }

    public void forEachHeader(java.util.function.Consumer<Button> action) {
        getColumnHeaders().forEach(action);
    }

    public int getRowsCount() {
        return getTableService().getRows().size();
    }

    public GridRow getRow(int rowIndex) {
        String xpath = "." + getTableService().getRow(rowIndex).cssSelector();
        GridRow row = createByCss(GridRow.class, xpath);
        row.setParentGrid(this);
        row.setIndex(rowIndex);
        return row;
    }

    public List<GridRow> getRows() {
        List<GridRow> rows = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < tableService.getRows().size(); rowIndex++) {
            GridRow row = getRow(rowIndex);
            rows.add(row);
        }
        return rows;
    }


//    public List<GridRow> getRows(Predicate<WebComponent> selector) {
//        return getRows().stream()
//                .filter(r -> r.getCells(selector).size() > 0)
//                .collect(Collectors.toList());
//    }

//    public GridRow getFirstOrDefaultRow(Predicate<WebComponent> selector) {
//        return getRows(selector).stream().findFirst().orElse(null);
//    }

    public void forEachRow(Consumer<GridRow> action) {
        for (var gridRow : getTableService().getRows()) {
            String xpath = gridRow.cssSelector();
            GridRow row = createByCss(GridRow.class, xpath);
            action.accept(row);
        }
    }

    public GridCell getCell(int row, int column) {
        String innerCss = tableService.getCell(row, column).cssSelector();
        String outerCss = getCurrentElementCssSelector();
        String fullCss = outerCss + " " + innerCss;
        GridCell cell = createByCss(GridCell.class, fullCss);
        setCellMetaData(cell, row, column);
        return cell;
    }

    public GridCell getCell(String header, int row) {
        Optional<Integer> position = getHeaderNamesService().getHeaderPosition(header, getControlColumnDataCollection(), null, true);
        if (position == null) {
            return null;
        }

        return getCell(row, position.get());
    }

//    public <TDto> GridCell getCell(Function<TDto, Object> expression, int row) {
//        String headerName = headerNamesService.getHeaderNameByExpression(expression);
//        Integer position = headerNamesService.getHeaderPosition(headerName, ControlColumnDataCollection);
//        if (position == null) {
//            return null;
//        }
//
//        return getCell(row, position);
//    }

    public void forEachCell(Consumer<GridCell> action) {
        String outerXPath = getCurrentElementXPath();
        for (var gridCell : tableService.getCells()) {
            String css = outerXPath + gridCell.cssSelector();
            action.accept(createByCss(GridCell.class, css));
        }
    }

    public <TComponent extends WebComponent> List<TComponent> getCells(Predicate<? super WebComponent> selector) throws InstantiationException, IllegalAccessException {
        List<TComponent> filteredCells = new ArrayList<>();
        for (GridRow gridRow : getRows()) {
            List<TComponent> currentFilteredCells = gridRow.getCells(selector);
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
            if (footerRow.selectFirst("td").text().equals(footerName)) {
                return getFooterByPosition(currentRowIndex);
            }
            currentRowIndex++;
        }

        throw new IllegalArgumentException("Footer row " + footerName + " was not found.");
    }

    public GridRow getFooterByPosition(int position) {
        String css = "." + getFooterService().getRows().get(position).cssSelector();
        GridRow row = createByCss(GridRow.class, css);
        row.setParentGrid(this);
        row.setIndex(position);
        return row;
    }

//    public List<GridRow> getFooterRows(Predicate<WebComponent> selector) {
//        return getFooterRows().stream()
//                .filter(r -> r.getCells(selector).size() > 0)
//                .collect(Collectors.toList());
//    }

//    public GridRow getFirstOrDefaultFooterRow(Predicate<Component> selector) {
//        return getFooterRows(selector).stream().findFirst().orElse(null);
//    }

    public void forEachFooterRow(Consumer<GridRow> action) {
        for (var gridRow : getFooterService().getRows()) {
            String css = gridRow.cssSelector();
            action.accept(createByCss(GridRow.class, css));
        }
    }

//    public <TComponent extends WebComponent> TComponent getFirstOrDefaultCell(Predicate<TComponent> selector) {
//        return getCells(selector).stream().findFirst().orElse(null);
//    }

    public String getGridColumnNameByIndex(int index) {
        return getHeaderNamesService().getHeaderNames().get(index);
    }

    public int getGridColumnIndexByName(String columnName) {
        List<String> coll = getHeaderNamesService().getHeaderNames();
        return coll.indexOf(columnName);
    }

    public List<GridCell> getColumn(int column) {
        List<GridCell> list = new ArrayList<>();

        for (int row = 0; row < getTableService().getRows().size(); row++) {
            list.add(getCell(row, column));
        }

        return list;
    }

    public List<GridCell> getColumn(String header, Integer order) {
        Optional<Integer> position = getHeaderNamesService().getHeaderPosition(header, controlColumnDataCollection, order, true);
        if (position == null) {
            return new ArrayList<GridCell>();
        }

        return getColumn(position.get());
    }

    public void assertTable(List<?> expectedEntities, String... propertiesNotToCompare) throws IntrospectionException, IllegalAccessException, InstantiationException {
        scrollToVisible();
        Assertions.assertEquals(expectedEntities.size(), getRowsCount(), String.format("Expected rows count %d but rows was %d", expectedEntities.size(), getRowsCount()));

        for (int i = 0; i < getRowsCount(); i++) {
            Object entity = castRow(i, propertiesNotToCompare);
            EntitiesAsserter.assertAreEqual(expectedEntities.get(i), entity, propertiesNotToCompare);
        }
    }

    public void assertIsEmpty() {
        scrollToVisible();
        Assertions.assertEquals(0, getRowsCount(), String.format("Grid should be empty, but has %d rows", getRowsCount()));
    }

    public void assertIsNotEmpty() {
        scrollToVisible();
        Assertions.assertTrue(tableService.getRows().size() > 0, "Grid has no rows");
    }

    public void assertRowsCount(int expectedRowsCount) {
        scrollToVisible();
        Assertions.assertEquals(expectedRowsCount, getRowsCount(), String.format("Grid should have %d rows, but has %d rows", expectedRowsCount, getRowsCount()));
    }

    public <TRowObject> List<TRowObject> getItems() throws IntrospectionException, IllegalAccessException, InstantiationException {
        scrollToVisible();
        return getItems(getRows().stream().map(x -> (GridRow)x).collect(Collectors.toList()));
    }

    protected <TRowObject extends Object, TRow extends GridRow> List<TRowObject> getItems(List<TRow> rows) throws IntrospectionException, IllegalAccessException, InstantiationException {
        List<TRowObject> list = new ArrayList<TRowObject>();

        for (TRow row : rows) {
            TRowObject obj = castRow(row.getIndex());
            list.add(obj);
        }

        return list;
    }

    private <TValue> TValue castCell(ControlColumnData controlData, TableCell tableCell, Class<TValue> valueType) {
        var repo = new ComponentCreateService();
        Object value = null;
        if (controlData != null && controlData.getComponentType() != null) {
            try {
                Class<? extends WebComponent> componentType = controlData.getComponentType().asSubclass(WebComponent.class);
                FindStrategy findStrategy = controlData.getFindStrategy();
                if (findStrategy == null) {
                    value = tableCell.getText();
                } else {
                    var component = repo.create(findStrategy.getClass(), componentType);
                    component.setParentWrappedElement(tableCell.getWrappedElement());
                    if (valueType.isInstance(component)) {
                        value = valueType.cast(component);
                    } else {
                        // Resolve the appropriate Readonly Control Data Handler
                        IReadonlyControlDataHandler<?> controlDataHandler = ControlDataHandlerResolver.resolveReadonlyHandler(component.getClass());
                        if (controlDataHandler == null) {
                            throw new RuntimeException(
                                    "Cannot find proper IReadonlyControlDataHandler for type: " + component.getClass().getName()
                                            + ". Make sure it is registered in the ServiceContainer");
                        }
                        Object data = controlDataHandler.getData(controlData.getComponentType(), component);
                        if (valueType.isInstance(data)) {
                            value = valueType.cast(data);
                        } else {
                            throw new RuntimeException("Cannot cast control data to the target type");
                        }
                    }
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            value = tableCell.getText();
        }
        return valueType.cast(value);
    }

//    private ControlColumnData getControlDataByProperty(Property property) {
//        List<Annotation> annotations = Arrays.asList(property.getAnnotations());
//        HeaderNameAnnotation headerNameAnnotation = annotations.stream()
//                .filter(x -> x.annotationType() == HeaderNameAnnotation.class)
//                .findFirst()
//                .map(HeaderNameAnnotation.class::cast)
//                .orElse(null);
//
//        IHeaderInfo headerInfo;
//        if (headerNameAnnotation != null) {
//            headerInfo = controlColumnDataCollection.stream()
//                    .filter(x -> x.getHeaderName().equals(headerNameAnnotation.value()))
//                    .findFirst()
//                    .orElse(null);
//        } else {
//            headerInfo = controlColumnDataCollection.stream()
//                    .filter(x -> x.getHeaderName().equals(property.getName()))
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        return (ControlColumnData) headerInfo;
//    }
//
//
//    public <TRowObject> TRowObject castRow(int rowIndex, String... propertiesToSkip) throws IllegalAccessException, InstantiationException, IntrospectionException {
//        List<Element> cells = tableService.getRowCells(rowIndex);
//
//        if (cells.size() != controlColumnDataCollection.size()) {
//            // Compare headers to determine why the cells count is different
//            List<String> actual = getHeaderNamesService().getHeaderNames();
//            List<String> expected = controlColumnDataCollection.stream().map(ControlColumnData::getHeaderName).collect(Collectors.toList());
//            Assertions.assertEquals(expected, actual, String.format("Expected: %s\r\nActual: %s", expected.stream().collect(Collectors.joining(", ")), actual.stream().collect(Collectors.joining(", "))));
//        }
//
//        TRowObject dto = (TRowObject) new Object();
//        List<PropertyDescriptor> properties = Arrays.asList(Introspector.getBeanInfo(dto.getClass()).getPropertyDescriptors());
//        for (PropertyDescriptor property : properties) {
//            IHeaderInfo headerInfo = getHeaderNamesService().getHeaderInfoByProperty(property);
//
//            if (Arrays.asList(propertiesToSkip).contains(headerInfo.getHeaderName())) {
//                continue;
//            }
//
//            Optional<Integer> headerPosition = getHeaderNamesService().getHeaderPosition(headerInfo.getHeaderName(), controlColumnDataCollection.stream().map(c -> (IHeaderInfo) c).collect(Collectors.toList()), headerInfo.getOrder(), true);
//            if (headerPosition == null) {
//                continue;
//            }
//
//            ControlColumnData controlData = getControlDataByProperty(property.getPropertyType().getRawType());
//            if (controlData != null && controlData.getComponentType() != null && WebComponent.class.isAssignableFrom(controlData.getComponentType())) {
//                var repo = new ComponentCreateService();
//                String cssSelector = "." + cells.get(headerPosition).getCssSelector();
//                TableCell tableCell = this.createByCss(TableCell.class, cssSelector);
//                Object elementValue;
//                if (controlData.getBy() == null) {
//                    controlData.setBy(By.cssSelector(cssSelector));
//                    elementValue = castCell(repo, controlData, tableCell);
//                    controlData.setBy(null);
//                } else {
//                    elementValue = castCell(repo, controlData, tableCell);
//                }
//
//                Class<?> elementType = property.getPropertyType().getRawType();
//                Object newValue = elementValue == null ? null : Convert.ChangeType(elementValue, elementType);
//
//                elementValue = Convert.ChangeType(newValue, property.getPropertyType().get());
//                property.getWriteMethod().invoke(dto, elementValue);
//            } else {
//                String htmlNodeValue = HtmlUtils.htmlDecode(tableService.getRowCells(rowIndex).get(headerPosition).getText()).trim();
//                Class<?> type = property.getPropertyType().getRawType();
//                Object elementValue;
//                if (type.equals(DateTime.class) || type.equals(DateTime.class)) {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                    LocalDateTime dateTime = LocalDateTime.parse(htmlNodeValue, formatter);
//                    elementValue = (DateTime) dateTime;
//                } else {
//                    elementValue = StringUtils.isEmpty(htmlNodeValue) ? null : Convert.ChangeType(htmlNodeValue, type);
//                }
//
//                property.getWriteMethod().invoke(dto, elementValue);
//            }
//        }
//
//        return dto;
//    }

    protected void setCellMetaData(GridCell cell, int row, int column) {
        cell.setRow(row);
        cell.setColumn(column);
        String headerName = controlColumnDataCollection.get(column).getHeaderName();
        ControlColumnData controlData = getControlColumnDataByHeaderName(headerName);
        cell.setCellControlBy(controlData.getFindStrategy());
        cell.setCellControlComponentType(controlData.getComponentType());
    }

    public ControlColumnData getControlColumnDataByHeaderName(String headerName) {
        var headerInfo  = controlColumnDataCollection.stream()
                .filter(x -> x.getHeaderName().equals(headerName))
                .findFirst()
                .orElse(null);

        return new ControlColumnData(headerInfo.getHeaderName(), headerInfo.getOrder());
    }

    public boolean anyControlColumnDataByHeaderName(String headerName) {
        return controlColumnDataCollection.stream()
                .anyMatch(x -> x.getHeaderName().equals(headerName));
    }

    protected String getCurrentElementXPath() {
        String jsScriptText =
                "function createXPathFromElement(elm) {" +
                        "    var allNodes = document.getElementsByTagName('*');" +
                        "    for (var segs = []; elm && elm.nodeType === 1; elm = elm.parentNode) {" +
                        "        if (elm.hasAttribute('id')) {" +
                        "            var uniqueIdCount = 0;" +
                        "            for (var n = 0; n < allNodes.length; n++) {" +
                        "                if (allNodes[n].hasAttribute('id') && allNodes[n].id === elm.id) uniqueIdCount++;" +
                        "                if (uniqueIdCount > 1) break;" +
                        "            };" +
                        "            if (uniqueIdCount === 1) {" +
                        "                segs.unshift('//*[@id=\'' + elm.getAttribute('id') + '\']');" +
                        "                return segs.join('/');" +
                        "            }" +
                        "            else {" +
                        "                segs.unshift('//' + elm.localName.toLowerCase() + '[@id=\'' + elm.getAttribute('id') + '\']');" +
                        "            }" +
                        "        }" +
                        "        else {" +
                        "            for (i = 1, sib = elm.previousSibling; sib; sib = sib.previousSibling) {" +
                        "                if (sib.localName === elm.localName) i++;" +
                        "            };" +
                        "            segs.unshift(elm.localName.toLowerCase() + '[' + i + ']');" +
                        "        };" +
                        "    };" +
                        "    return segs.length ? '/' + segs.join('/') : null;" +
                        "};" +
                        "return createXPathFromElement(arguments[0]);";

        return javaScriptService.execute(jsScriptText, this);
    }

    protected String getCurrentElementCssSelector() {
        String jsScriptText =
                "function getCssSelectorFromElement(el) {" +
                        "   var names = [];" +
                        "   while (el.parentNode) {" +
                        "       if (el.id) {" +
                        "           names.unshift('#' + el.id);" +
                        "           break;" +
                        "       } else {" +
                        "           if (el == el.ownerDocument.documentElement)" +
                        "               names.unshift(el.tagName);" +
                        "           else {" +
                        "               for (var c = 1, e = el; e.previousElementSibling; e = e.previousElementSibling, c++) {}" +
                        "               names.unshift(el.tagName + ':nth-child(' + c + ')');" +
                        "           }" +
                        "           el = el.parentNode;" +
                        "       }" +
                        "   }" +
                        "   return names.join(' > ');" +
                        "};" +
                        "return getCssSelectorFromElement(arguments[0]);";
        return javaScriptService.execute(jsScriptText, this);
    }

}


