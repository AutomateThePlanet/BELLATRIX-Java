package solutions.bellatrix.web.components.advanced;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.List;
import java.util.stream.Collectors;

public class TableService {
    private static final String ROWS_XPATH_LOCATOR = "//tr[descendant::td]";
    private String _tableXPath;
    private Document _htmlDoc;

    public TableService(String html) {
        _htmlDoc = Jsoup.parse(html);
    }

    public TableService(String html, String tableXpath) {
        this(html);
        _tableXPath = tableXpath;
    }

    public Element getTable() {
        if ("".equals(_tableXPath) || _tableXPath == null) {
            _tableXPath = "//*";
        }
        return _htmlDoc.selectFirst(_tableXPath);
    }

    public List<Element> getHeaders() {
        return getTable().select("th").stream().collect(Collectors.toList());
    }

    public List<Element> getHeaderRows() {
        return getTable().select("tr:has(th)").stream()
                .filter(a -> !"display:none".equals(a.attr("style")))
                .collect(Collectors.toList());
    }

    public List<Element> getRows() {
        return getTable().select(ROWS_XPATH_LOCATOR).stream().collect(Collectors.toList());
    }

    public Element getFooter() {
        return getTable().selectFirst("tfoot");
    }

    public Element getRow(int row) {
        return getRows().get(row);
    }

    public Element getCell(int row, int column) {
        return getRowCells(row).get(column);
    }

    public List<Element> getCells() {
        List<Element> listOfNodes = getRows().stream()
                .flatMap(row -> row.select("td").stream())
                .collect(Collectors.toList());
        return listOfNodes;
    }

    public List<Element> getRowCells(int rowIndex) {
        return getTable().select(getRows().get(rowIndex).cssSelector() + " td").stream()
                .collect(Collectors.toList());
    }
}
