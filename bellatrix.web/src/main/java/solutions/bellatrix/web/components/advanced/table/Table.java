package solutions.bellatrix.web.components.advanced.table;

import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.*;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Table extends WebComponent {
    private HeaderNamesService headerNamesService;
    private FooterService footerService;
    private List<TableRow> _rows;
    private TableService tableService;
    private List<HeaderInfo> columnHeaderNames;

    public Table() {
    }

    public TableService getTableService() {
        if (tableService == null) {
            String innerHtml = getAttribute("innerHTML");
            tableService = new TableService(innerHtml);
        }
        return tableService;
    }

    protected HeaderNamesService getHeaderNamesService() {
        if (headerNamesService == null) {
            headerNamesService = new HeaderNamesService(getTableService().getHeaderRows());
        }
        return headerNamesService;
    }

    protected FooterService getFooterService() {
        if (footerService == null) {
            footerService = new FooterService(getTableService().getFooter());
        }
        return footerService;
    }

    public TableFooter getFooter() {
        return this.createByCss(TableFooter.class, getTableService().getFooter().cssSelector());
    }

    public int getRowsCount() {
        return _rows.size();
    }

    public List<HeaderInfo> getColumnHeaderNames() {
        return columnHeaderNames;
    }

    public void setColumnHeaderNames(List<HeaderInfo> columnHeaderNames) {
        this.columnHeaderNames = columnHeaderNames;
    }

//    @Override
//    public Class<? extends WebComponent> getComponentType() {
//        return getClass();
//    }

    public List<Label> getColumnHeaders() {
//        return this.createAllByTag(Label.class, "th", true).toList();
        return this.createAllByTag(Label.class, "th");
    }

    public List<TableHeaderRow> getTableHeaderRows() {
        return this.createAllByXPath(TableHeaderRow.class, ".//tr[descendant::th]");
    }

    public List<TableRow> getRows() {
        initializeRows();
        return _rows;
    }

    public TableRow getRow(int row) {
        initializeRows();
        return _rows.get(row);
    }

    public void forEachRow(Consumer<TableRow> action) {
        for (TableRow tableRow : getRows()) {
            action.accept(tableRow);
        }
    }

    public void forEachCell(Consumer<TableCell> action) {
        for (TableRow tableRow : getRows()) {
            for (TableCell rowCell : tableRow.getCells()) {
                action.accept(rowCell);
            }
        }
    }

    public void forEachHeader(Consumer<Label> action) {
        for (Label header : getColumnHeaders()) {
            action.accept(header);
        }
    }

    public List<TableCell> getColumn(int column) {
        List<TableCell> tableCells = getRows().stream().map(row -> row.getCell(column)).collect(Collectors.toList());
        int rowNumber = 0;
        for (TableCell tableCell : tableCells) {
            tableCell.setColumn(column);
            tableCell.setRow(rowNumber++);
        }
        return tableCells;
    }

    public List<TableCell> getColumn(String header) {
        Optional<Integer> position = getHeaderNamesService().getHeaderPosition(header, columnHeaderNames.stream().map(i -> (IHeaderInfo)i).collect(Collectors.toList()), null, true);
        if (position == null) {
            return new ArrayList<>();
        }
        return getColumn(position.get());
    }

    public TableCell getCell(String header, int row) {
        Optional<Integer> position = getHeaderNamesService().getHeaderPosition(header, columnHeaderNames.stream().map(i -> (IHeaderInfo)i).collect(Collectors.toList()), null, true);
        if (position == null) {
            return null;
        }
        return getCell(position.get(), row);
    }

//    public <TDto> TableCell getCell(Expression<Func<TDto, Object>> expression, int row) {
//        String headerName
//        Integer position = getHeaderNamesService().getHeaderPosition(headerNames, columnHeaderNames.stream().map(i -> (IHeaderInfo)i).collect(Collectors.toList()));
//        if (position == null) {
//            return null;
//        }
//        return getCell(position, row);
//    }

    public TableCell getCell(int column, int row) {
        TableCell cell = getRow(row).getCell(column);
        cell.setRow(row);
        cell.setColumn(column);
        return cell;
    }

    public List<TableCell> getCells(Predicate<TableCell> selector) {
        List<TableCell> filteredCells = new ArrayList<>();
        for (TableRow tableRow : getRows()) {
            List<TableCell> currentFilteredCells = tableRow.getCells(selector);
            filteredCells.addAll(currentFilteredCells);
        }
        return filteredCells;
    }

    public TableCell getFirstOrDefaultCell(Predicate<TableCell> selector) {
        return getCells(selector).stream().findFirst().orElse(null);
    }

    public List<TableRow> getRows(Predicate<TableCell> selector) {
        return getRows().stream().filter(r -> r.getCells(selector).size() > 0).collect(Collectors.toList());
    }

    public TableRow getFirstOrDefaultRow(Predicate<TableCell> selector) {
        return getRows(selector).stream().findFirst().orElse(null);
    }

    public <T> void assertTable(List<T> expectedEntities, Class<T> itemType) throws Exception {
        assertTable(expectedEntities, TableRow.class, itemType);
    }

    public <T, TRow extends TableRow> void assertTable(List<T> expectedEntities, Class<TRow> rowType, Class<T> itemType) throws Exception {
        List<TRow> rows = getRows().stream().filter(rowType::isInstance).map(rowType::cast).collect(Collectors.toList());
        List<T> actualEntities = getItems(rows, itemType);
        if (actualEntities.size() != expectedEntities.size()) {
            throw new IllegalArgumentException(String.format("The current table rows count %d is different than the specified expected values %d.", actualEntities.size(), expectedEntities.size()));
        }
        for (int i = 0; i < expectedEntities.size(); i++) {
            EntitiesAsserter.assertAreEqual(expectedEntities.get(i), actualEntities.get(i));
        }
    }

    public List<String> getHeaderNames() {
        return getHeaderNamesService().getHeaderNames();
    }

    public <T> List<T> getItems(Class<T> itemType) throws Exception {
        return getItems(TableRow.class, itemType);
    }

    public <T, TRow extends TableRow> List<T> getItems(Class<TRow> rowType, Class<T> itemType) throws Exception {
        List<TRow> rows = getRows().stream().filter(rowType::isInstance).map(rowType::cast).collect(Collectors.toList());
        return getItems(rows, itemType);
    }


    protected <T, TRow extends TableRow> List<T> getItems(List<TRow> rows, Class<T> type) throws Exception {
        List<T> items = new ArrayList<>();
        for (TRow row : rows) {
            items.add(castRow(row, type));
        }
        return items;
    }

    protected  <T> T castRow(TableRow row, Class<T> type) throws Exception {
        T castRow = type.newInstance();
        PropertyDescriptor[] props = Introspector.getBeanInfo(type).getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            if (row.getCells().size() > 0) {
                if (prop.getWriteMethod() != null) {
                    String headerName = getHeaderNamesService().getHeaderNameByProperty(prop.getReadMethod());
                    Optional<Integer> headerPosition = getHeaderNamesService().getHeaderPosition(headerName, getColumnHeaderNames().stream().map(x -> (IHeaderInfo) x).collect(Collectors.toList()), null, true);
                    if (headerPosition != null) {
                        String elementValue = row.getCells().get(headerPosition.get()).getText();
                        Object value = convertStringToType(elementValue, prop.getPropertyType());
                        prop.getWriteMethod().invoke(castRow, value);
                    }
                } else {
                    throw new RuntimeException("Cannot cast grid data to Java object. Most probably your Java class properties don't have setters.");
                }
            }
        }
        return castRow;
    }

    private Object convertStringToType(String elementValue, Class<?> propertyType) throws ParseException {
        if (propertyType == String.class) {
            return elementValue;
        } else if (propertyType == int.class || propertyType == Integer.class) {
            return Integer.parseInt(elementValue);
        } else if (propertyType == double.class || propertyType == Double.class) {
            return Double.parseDouble(elementValue);
        } else if (propertyType == boolean.class || propertyType == Boolean.class) {
            return Boolean.parseBoolean(elementValue);
        } else if (propertyType == LocalDate.class) {
            return LocalDate.parse(elementValue);
        } else if (propertyType == LocalDateTime.class) {
            return LocalDateTime.parse(elementValue);
        } else {
            throw new RuntimeException("Unsupported property type: " + propertyType.getName());
        }
    }


    private void initializeRows() {
        if (getRows() == null || getRows().size() > 0) {
//            _rows = createAllByXpath("./tr[descendant::td]|./tbody/tr[descendant::td]", true, TableRow.class);
            _rows = createAllByXPath(TableRow.class, "./tr[descendant::td]|./tbody/tr[descendant::td]");

            int rowNumber = 0;
            for (TableRow gridRow : _rows) {
//                if (!createAllByXpath("./tr[descendant::th]", true, Element.class).isEmpty()) {
//                    gridRow.setParentTable(this);
//                }

                if (!createAllByXPath(WebComponent.class, "./tr[descendant::th]").isEmpty()) {
                    gridRow.setParentTable(this);
                }

                gridRow.setIndex(rowNumber++);
            }
        }
    }


}