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

package solutions.bellatrix.playwright.components.advanced.table;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.advanced.services.HeaderNamesService;
import solutions.bellatrix.playwright.components.contracts.ComponentHtml;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TableRow extends WebComponent implements ComponentHtml {
    private Table parentTable;
    private HeaderNamesService headerNamesService;

    @Getter @Setter private int index;

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    public void setParentTable(Table table) {
        parentTable = table;
        headerNamesService = new HeaderNamesService(parentTable.getTableService().getHeaderRows());
    }

    public TableCell getCell(int column) {
        if (getTableCells().size() <= column) return null;

        var tableCell = getTableCells().get(column);
        tableCell.setColumn(column);
        tableCell.setRow(index);

        return tableCell;
    }

    public TableCell getCell(String headerName) {
        int position = headerNamesService.getHeaderPosition(headerName, parentTable.getColumnHeaderNames());

        return getCell(position);
    }

    public <TDto> TableCell getCell(Class<TDto> dtoClass, Predicate<TDto> expression) {
        var headerName = headerNamesService.getHeaderNameByExpression(dtoClass, expression);
        return getCell(headerName);
    }

    public List<TableCell> getCells() {
        int columnNumber = 0;
        var tableCells = new ArrayList<TableCell>();
        for (var tableCell : getTableCells()) {
            tableCells.add(tableCell);
            tableCell.setRow(index);
            tableCell.setColumn(columnNumber++);
        }

        return tableCells;
    }

    public List<TableCell> getCells(Predicate<TableCell> selector) {
        return getCells().stream().filter(selector).toList();
    }

    public TableCell getFirstOrDefault(Predicate<TableCell> selector) {
        return getCells(selector).stream().findFirst().orElse(null);
    }

    public <T> T getItem(Class<T> clazz) {
        return parentTable.castRow(clazz, this);
    }

    @SuppressWarnings("unchecked")
    public <T> void assertRow(T expectedItem) {
        T actualItem = getItem((Class<T>)expectedItem.getClass());

        EntitiesAsserter.assertAreEqual(expectedItem, actualItem);
    }

    protected List<TableCell> getTableCells() {
        return this.create().allByXpath(TableCell.class, "/" + parentTable.getTableService().locators().getCellTag());
    }
}
