package solutions.bellatrix.web.components.advanced.table;

import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.HeaderNamesService;
import solutions.bellatrix.web.components.contracts.ComponentHtml;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Optional;

public class TableRow extends WebComponent implements ComponentHtml {
    private Table _parentTable;
    private HeaderNamesService _headerNamesService;

    protected List<TableCell> getTableCells() {
        return this.createAllByXPath(TableCell.class, "./td");
    }

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TableCell get(int i) {
        return getCells().get(i);
    }

    public void setParentTable(Table table) {
        _parentTable = table;
        _headerNamesService = new HeaderNamesService(_parentTable.getTableService().getHeaderRows());
    }

    public TableCell getCell(int column) {
        if (getTableCells().size() <= column) {
            return null;
        }

        TableCell tableCell = getTableCells().get(column);
        tableCell.setColumn(column);
        tableCell.setRow(index);

        return tableCell;
    }

    public TableCell getCell(String headerName) {
        Optional<Integer> position = _headerNamesService.getHeaderPosition(headerName, _parentTable.getColumnHeaderNames(), null, true);
        if (!position.isPresent()) {
            return null;
        }

        return getCell(position.get());
    }

//    public <TDto> TableCell getCell(Function<TDto, ?> expression) {
//        String headerName = _headerNamesService.getHeaderNameByExpression(expression);
//        return getCell(headerName);
//    }

    public List<TableCell> getCells() {
        var columnNumber = new AtomicInteger();
        return getTableCells().stream()
                .peek(tableCell -> {
                    tableCell.setRow(index);
                    tableCell.setColumn(columnNumber.getAndIncrement());
                }).collect(Collectors.toList());
    }

    public List<TableCell> getCells(Predicate<TableCell> selector) {
        return getCells().stream().filter(selector).collect(Collectors.toList());
    }

    public TableCell getFirstOrDefaultCell(Predicate<TableCell> selector) {
        return getCells(selector).stream().findFirst().orElse(null);
    }

    public <T> T getItem(Class<T> clazz) throws Exception {
        return (T)_parentTable.castRow(this, this.getClass());
    }

    public <T> void assertRow(T expectedItem, Class<T> itemType) throws Exception {
        T actualItem = getItem(itemType);

        EntitiesAsserter.assertAreEqual(expectedItem, actualItem);
    }
}

