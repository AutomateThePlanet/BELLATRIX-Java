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

package solutions.bellatrix.playwright.components.advanced.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.core.utilities.SingletonFactory;

import java.util.List;
import java.util.Objects;

public class TableService {
    protected String tableXpath;
    private final Document htmlDoc;

    public TableService(String html) {
        htmlDoc = Jsoup.parse(html, Parser.xmlParser());
    }

    public TableService(String html, String tableXpath) {
        this(html);
        this.tableXpath = tableXpath;
    }

    public TableLocators locators() {
        return SingletonFactory.getInstance(TableLocators.class);
    }

    public Element getTable() {
        if (tableXpath == null || tableXpath.isBlank()) {
            return HtmlService.addRootElementIfNeeded(htmlDoc);
        }

        return htmlDoc.selectXpath(tableXpath).first();
    }

    public List<Element> getHeaders() {
        return getTable().selectXpath("." + locators().getHeadersXpath());
    }

    public List<Element> getHeaderRows() {
        return getTable().selectXpath("." + locators().getHeadersXpath()).stream()
                .filter((a) -> a.attribute("style") == null || !Objects.equals(a.attribute("style").getValue(), "display:none"))
                .toList();
    }

    public Element getRow(int index) {
        return getRows().get(index);
    }

    public List<Element> getRows() {
        return getTable().selectXpath("." + locators().getRowsXpath());
    }

    public Element getFooter() {
        return getTable().selectXpath("." + locators().getFooterXpath()).first();
    }

    public Element getCell(int row, int column) {
        return getRowCells(row).get(column);
    }

    public List<Element> getCells() {
        var listOfNodes = new Elements();
        for (int i = 0; i < getRows().size(); i++) {
            listOfNodes.addAll(getRow(i).selectXpath("." + locators().getCellXpath()));
        }

        return listOfNodes;
    }

    public List<Element> getRowCells(int rowIndex) {
        return getRow(rowIndex).selectXpath("." + locators().getCellXpath());
    }
}
