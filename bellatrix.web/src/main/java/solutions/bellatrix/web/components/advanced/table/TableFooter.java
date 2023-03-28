package solutions.bellatrix.web.components.advanced.table;

import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.FooterService;
import solutions.bellatrix.web.components.advanced.HeaderNamesService;
import solutions.bellatrix.web.components.advanced.IHeaderInfo;
import solutions.bellatrix.web.components.contracts.ComponentHtml;

import java.util.Optional;
import java.util.stream.Collectors;

public class TableFooter extends WebComponent implements ComponentHtml {
    private Table _parentTable;
    private HeaderNamesService _headerNamesService;
    private FooterService _footerService;
    private int index;

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    public void setParentTable(Table table) {
        _parentTable = table;
        _headerNamesService = new HeaderNamesService(_parentTable.getTableService().getHeaderRows());
        _footerService = new FooterService(_parentTable.getTableService().getFooter());
    }

    public TableRow getRowByPosition(int position) {
        var footerRow = _footerService.getFooterRowByPosition(position);
        TableRow tableRow = this.createByCss(TableRow.class, footerRow.cssSelector());
        tableRow.setParentTable(_parentTable);
        tableRow.setIndex(position);
        return tableRow;
    }

    public TableRow getRowByName(String footerName) {
        var node = _footerService.getFooterRowByName(footerName);
        int position = _footerService.getRows().indexOf(node);
        TableRow footerRow = createByCss(TableRow.class, node.cssSelector());
        footerRow.setParentTable(_parentTable);
        footerRow.setIndex(position);
        return footerRow;
    }

    public TableCell getCell(String footerName, String headerName) {
        Optional<Integer> headerPosition = _headerNamesService.getHeaderPosition(headerName, _parentTable.getColumnHeaderNames().stream().map(x -> (IHeaderInfo) x).collect(Collectors.toList()), null, true);
        if (headerPosition == null) {
            return null;
        }

        var node = _footerService.getFooterRowCellByName(footerName, headerPosition.get());
        TableCell footerCell = createByCss(TableCell.class, node.cssSelector());
        footerCell.setRow(_footerService.getRows().indexOf(node));
        footerCell.setColumn(headerPosition.get());
        return footerCell;
    }

    public TableCell getCell(int position, String headerName) {
        Optional<Integer> headerPosition = _headerNamesService.getHeaderPosition(headerName, _parentTable.getColumnHeaderNames().stream().map(x -> (IHeaderInfo) x).collect(Collectors.toList()), null, true);
        if (headerPosition == null) {
            return null;
        }

        var node = _footerService.getFooterRowCellByPosition(position, headerPosition.get());
        TableCell footerCell = createByCss(TableCell.class, node.cssSelector());
        footerCell.setRow(position);
        footerCell.setColumn(headerPosition.get());
        return footerCell;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

