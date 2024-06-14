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

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Element;
import solutions.bellatrix.core.utilities.Ref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FooterService {
    private String xpathToNameElement;
    private Element tableFooter;
    private List<List<String>> tableFooterRowsValuesWithIndex;

    public FooterService(Element tableFooter) {
        this.tableFooter = tableFooter;
    }

    public FooterService(Element tableFooter, String xpathToNameElement) {
        this(tableFooter);
        this.xpathToNameElement = xpathToNameElement;
    }

    public List<Element> getRows() {
        return tableFooter.selectXpath("//tr[ancestor::tfoot]")
                .stream().filter(a -> !Objects.equals(a.attribute("style").getValue(), "display:none"))
                .toList();
    }

    public List<List<String>> getFooterRowsData() {
        initializeFooterRows();
        return tableFooterRowsValuesWithIndex;
    }

    public Element getFooterRowByName(String footerName) {
        return getRows()
                .stream().filter(row -> row.selectXpath(String.format(".//td[.='%s']", footerName)) != null)
                .findFirst().orElse(null);
    }

    public Element getFooterRowByPosition(int position) {
        return getRows().get(position);
    }

    public Element getFooterRowCellByName(String footerName, int cellIndex) {
        var row = getRows().stream().filter(a -> Objects.equals(a.text(), footerName)).findFirst().orElse(null);
        return row.selectXpath(".//td").get(cellIndex);
    }

    public Element getFooterRowCellByPosition(int position, int cellIndex) {
        return getFooterRowByPosition(position).selectXpath(".//td").get(cellIndex);
    }

    public List<String> getFooterRowDataByName(String footerName) {
        return getFooterRowsData()
                .stream().filter(x -> Objects.equals(x.get(0), footerName))
                .findFirst()
                .orElseGet(() -> getFooterRowsData().stream().filter(x -> x.get(0).startsWith(footerName)).findFirst().orElse(null));
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

        tableFooterRowsValuesWithIndex = new ArrayList<>();
        int rowIndex = 0;
        for (var tableFooterRow : getRows()) {
            var columnIndex = new Ref<Integer>(0);
            var footerCellsCount = tableFooterRow.selectXpath(".//td").size();
            for (var currentCell : tableFooterRow.selectXpath(".//td")) {
                String cellValue;
                if (xpathToNameElement == null || xpathToNameElement.isBlank()) {
                    cellValue = StringEscapeUtils.unescapeHtml4(currentCell.text());
                } else {
                    cellValue = StringEscapeUtils.unescapeHtml4(currentCell.selectXpath(xpathToNameElement).text());
                }

                int colSpan = getColSpan(currentCell);
                addColumnIndex(rowIndex, colSpan, (Ref<Integer>)columnIndex, cellValue);
            }

            rowIndex++;
        }
    }

    private void addColumnIndex(int currentRow, int colSpan, Ref<Integer> columnIndex, String cellValue) {
        addFooterCellIndex(currentRow, cellValue);
        if (colSpan == 0) columnIndex.value++;
        else {
            int initialIndex = columnIndex.value;
            for (int i = 1; i < colSpan; i++) {
                addFooterCellIndex(currentRow, "");
                columnIndex.value++;
            }

            columnIndex.value++;
        }
    }

    private void addFooterCellIndex(int currentRow, String cellValue) {
        if (tableFooterRowsValuesWithIndex.size() > currentRow) {
            tableFooterRowsValuesWithIndex.get(currentRow).add(cellValue);
        } else {
            var list = new ArrayList<String>();
            list.add(cellValue);
            tableFooterRowsValuesWithIndex.add(list);
        }
    }

    private int getColSpan(Element headerCell) {
        String colSpanText = headerCell.attribute("colspan").getValue();
        return (colSpanText == null || colSpanText.isBlank()) ? 0 : Integer.parseInt(colSpanText);
    }
}