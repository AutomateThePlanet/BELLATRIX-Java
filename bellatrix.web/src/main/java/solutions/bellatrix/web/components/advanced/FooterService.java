package solutions.bellatrix.web.components.advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.opentelemetry.api.internal.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class FooterService {
    private String xpathToNameElement;
    private Element tableFooter;
    private List<List<String>> tableFooterRowsValuesWithIndex;

    public FooterService(Element tableFooter, String xpathToNameElement) {
        this.tableFooter = tableFooter;
        this.xpathToNameElement = xpathToNameElement;
    }

    public FooterService(Element tableFooter) {
        this.tableFooter = tableFooter;
        this.xpathToNameElement = "";
    }

    public List<Element> getRows() {
        List<Element> rows = new ArrayList<>();
        for (Node node : tableFooter.select("tr")) {
            Element row = (Element)node;
            String style = row.attr("style");
            if (!style.equals("display:none")) {
                rows.add(row);
            }
        }
        return rows;
    }

    public Element getFooterRowByName(String footerName) {
        for (Element row : getRows()) {
            if (row.selectFirst(".//td[.='" + footerName + "']") != null) {
                return row;
            }
        }
        return null;
    }

    public Element getFooterRowByPosition(int position) {
        return getRows().get(position);
    }

    public Element getFooterRowCellByName(String footerName, int cellIndex) {
        Element row = getRows().stream().filter(r -> r.text().equals(footerName)).findFirst().orElse(null);
        if (row != null) {
            return row.select(".//td").get(cellIndex);
        }
        return null;
    }

    public Element getFooterRowCellByPosition(int position, int cellIndex) {
        return getRows().get(position).select(".//td").get(cellIndex);
    }

    public List<List<String>> getFooterRowsData() {
        initializeFooterRows();
        return tableFooterRowsValuesWithIndex;
    }

    public List<String> getFooterRowDataByName(String footerName) {
        List<List<String>> footerRowsData = getFooterRowsData();
        for (List<String> row : footerRowsData) {
            if (row.get(0).equals(footerName)) {
                return row;
            }
        }
        for (List<String> row : footerRowsData) {
            if (row.get(0).startsWith(footerName)) {
                return row;
            }
        }
        return null;
    }

    public List<String> getFooterRowDataByPosition(int position) {
        return getFooterRowsData().get(position);
    }

    public String getFooterValueForIndex(int footerPosition, int index) {
        return getFooterRowDataByPosition(footerPosition).get(index);
    }

    public String getFooterValueForIndex(String footerName, int index) {
        return getFooterRowDataByName(footerName).get(index);
    }

    private void initializeFooterRows() {
        if (tableFooterRowsValuesWithIndex != null) {
            return;
        }

        tableFooterRowsValuesWithIndex = new ArrayList<List<String>>();
        int rowIndex = 0;

        for (Element tableFooterRow : getRows()) {
            int columnIndex = 0;
            int footerCellsCount = tableFooterRow.select(".//td").size();

            for (Element currentCell : tableFooterRow.select(".//td")) {
                String cellValue;

                if (StringUtils.isNullOrEmpty(xpathToNameElement)) {
                    cellValue = StringEscapeUtils.unescapeHtml4(currentCell.text());
                } else {
                    cellValue = StringEscapeUtils.unescapeHtml4(currentCell.select(xpathToNameElement).text());
                }

                int colSpan = getColSpan(currentCell);

                addColumnIndex(rowIndex, colSpan, new AtomicInteger(columnIndex), cellValue);
            }

            rowIndex++;
        }
    }

    private void addColumnIndex(int currentRow, int colSpan, AtomicInteger columnIndex, String cellValue) {
        addFooterCellIndex(currentRow, columnIndex.getAndIncrement(), cellValue);
        if (colSpan == 0) {
            columnIndex.incrementAndGet();
        } else {
            int initialIndex = columnIndex.get();
            for (int i = 1; i < colSpan; i++) {
                addFooterCellIndex(currentRow, initialIndex + i, "");
                columnIndex.incrementAndGet();
            }
            columnIndex.incrementAndGet();
        }
    }

    private void addFooterCellIndex(int currentRow, int operationalIndex, String cellValue) {
        if (tableFooterRowsValuesWithIndex.size() > currentRow) {
            tableFooterRowsValuesWithIndex.get(currentRow).add(cellValue);
        } else {
            List<String> rowValues = new ArrayList<>();
            rowValues.add(cellValue);
            tableFooterRowsValuesWithIndex.add(rowValues);
        }
    }


    private int getColSpan(Element headerCell) {
        String colSpanText = headerCell.attr("colspan");
        return colSpanText == null ? 0 : Integer.parseInt(colSpanText);
    }
}
