/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.components.advanced;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.Objects;

public class TableService {
    private static final String ROWS_XPATH_LOCATOR = "//tr[descendant::td]";
    private static final String HEADERS_XPATH_LOCATOR = "//tr[descendant::th]";
    private String tableXPath;
    private Document htmlDoc;

    public TableService(String html) {
        htmlDoc = Jsoup.parse(html, Parser.xmlParser());
    }

    public TableService(String html, String tableXpath) {
        this(html);
        this.tableXPath = tableXpath;
    }

    public Element getTable() {
        if (tableXPath == null || tableXPath.isBlank()) {
            // By default, we use root element for table
            tableXPath = "//*";
        }

        return htmlDoc.selectXpath(tableXPath).first();
    }

    public List<Element> getHeaders() {
        return getTable().selectXpath("//th");
    }

    public List<Element> getHeaderRows() {
        return getTable().selectXpath(HEADERS_XPATH_LOCATOR).stream().filter((a) -> {
            if (a.attribute("style") != null) {
                return !Objects.equals(a.attribute("style").getValue(), "display:none");
            } else {
                return true;
            }
        }).toList();
    }

    public List<Element> getRows() {
        return getTable().selectXpath(ROWS_XPATH_LOCATOR);
    }

    public Element getFooter() {
        return getTable().selectXpath("//tfoot").first();
    }

    public Element getRow(int index) {
        return getRows().get(index);
    }

    public Element getCell(int row, int column) {
        return getRowCells(row).get(column);
    }

    public List<Element> getCells() {
        var listOfNodes = new Elements();
        for (int i = 0; i < getRows().size(); i++) {
            listOfNodes.addAll(getRow(i).selectXpath("//td"));
        }

        return listOfNodes;
    }

    public List<Element> getRowCells(int rowIndex) {
        return getRow(rowIndex).selectXpath("//td");
    }
}
