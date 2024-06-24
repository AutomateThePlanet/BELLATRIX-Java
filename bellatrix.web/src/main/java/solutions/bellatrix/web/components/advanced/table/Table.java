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

package solutions.bellatrix.web.components.advanced.table;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.core.assertions.EntitiesAsserter;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.core.utilities.PropertyReference;
import solutions.bellatrix.core.utilities.parsing.TypeParser;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.*;
import solutions.bellatrix.web.components.advanced.services.FooterService;
import solutions.bellatrix.web.components.advanced.services.HeaderNamesService;
import solutions.bellatrix.web.components.advanced.services.TableService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Table extends WebComponent {
    private HeaderNamesService headerNamesService;
    private FooterService footerService;
    private List<TableRow> rows;
    private TableService tableService;

    public Table() {
    }

    public TableService getTableService() {
        if (this.tableService == null) {
            this.tableService = new TableService(defaultGetInnerHtmlAttribute());
        }
        return this.tableService;
    }

    protected HeaderNamesService getHeaderNamesService() {
        if (this.headerNamesService == null) {
            this.headerNamesService = new HeaderNamesService(getTableService().getHeaderRows());
        }
        return this.headerNamesService;
    }

    protected FooterService getFooterService() {
        if (this.footerService == null) {
            this.footerService = new FooterService(getTableService().getFooter());
        }
        return this.footerService;
    }

    public TableFooter getFooter() {
        return this.createByXPath(TableFooter.class, HtmlService.getAbsoluteXPath(getTableService().getFooter()));
    }

    public int getRowsCount() {
        return getRows().size();
    }

    @Getter @Setter private List<HeaderInfo> columnHeaderNames;

    public List<Label> getColumnHeaders() {
        return this.createAllByTag(Label.class, getTableService().locators().getHeaderTag());
    }

    public List<TableHeaderRow> getTableHeaderRows() {
        return this.createAllByXPath(TableHeaderRow.class, getTableService().locators().getHeadersXpath());
    }

    public List<TableRow> getRows() {
        initializeRows();
        return rows;
    }

    public TableRow getRow(int index) {
        initializeRows();
        return rows.get(index);
    }

    public void forEachRow(Consumer<TableCell> action) {
        for (var row : getRows()) {
            for (var rowCell : row.getCells()) {
                action.accept(rowCell);
            }
        }
    }

    public void forEachHeader(Consumer<Label> action) {
        for (var header : getColumnHeaders()) {
            action.accept(header);
        }
    }

    public List<TableCell> getColumn(int column) {
        var tableCells = getRows().stream().map(row -> row.getCell(column)).toList();

        int rowNumber = 0;
        for (var tableCell : tableCells) {
            tableCell.setColumn(column);
            tableCell.setRow(rowNumber++);
        }

        return tableCells;
    }

    public List<TableCell> getColumn(String header) {
        Integer position = getHeaderNamesService().getHeaderPosition(header, getColumnHeaderNames());
        if (position == null) {
            return new ArrayList<>();
        }

        return getColumn(position);
    }

    public TableCell getCell(String header, int row) {
        Integer position = getHeaderNamesService().getHeaderPosition(header, getColumnHeaderNames());
        if (position == null) return null;

        return getCell(position, row);
    }

    public <TDto> TableCell getCell(Class<TDto> dtoClass, PropertyReference<TDto> expression, int row) {
        String headerName = getHeaderNamesService().getHeaderNameByExpression(dtoClass, expression);
        var position = getHeaderNamesService().getHeaderPosition(headerName, columnHeaderNames);
        if (position == null) return null;

        return getCell(position, row);
    }

    public TableCell getCell(int column, int row) {
        var cell = getRow(row).getCell(column);
        cell.setRow(row);
        cell.setColumn(column);

        return cell;
    }

    public List<TableCell> getCells(Predicate<TableCell> selector) {
        var filteredCells = new ArrayList<TableCell>();
        for (var tableRow : getRows()) {
            var currentFilteredCells = tableRow.getCells(selector);
            filteredCells.addAll(currentFilteredCells);
        }

        return filteredCells;
    }

    public TableCell getFirstCell(Predicate<TableCell> selector) {
        return getCells(selector).get(0);
    }

    public List<TableRow> getRows(Predicate<TableCell> selector) {
        return getRows()
                .stream().filter(x -> !x.getCells(selector).isEmpty())
                .findAny().stream().toList();
    }

    public TableRow getFirstOrDefaultRow(Predicate<TableCell> selector) {
        return getRows(selector).stream().findFirst().orElse(null);
    }

    public <T> void assertTable(List<T> expectedEntities) {
        assertTable(getRows(), expectedEntities);
    }

    public List<String> getHeaderNames() {
        return getHeaderNamesService().getHeaderNames();
    }

    public <T> List<T> getItems(Class<T> clazz) {
        return getItems(clazz, getRows());
    }

    @SuppressWarnings("unchecked")
    public <T, TRow extends TableRow> void assertTable(List<TRow> rows, List<T> expectedEntities) {
        var actualEntities = getItems((Class<T>)expectedEntities.get(0).getClass(), rows);
        if (actualEntities.size() != expectedEntities.size()) {
            throw new IllegalArgumentException(String.format("The current table rows count %d is different than the specified expected values %d.", actualEntities.size(), expectedEntities.size()));
        }

        for (int i = 0; i < expectedEntities.size(); i++) {
            EntitiesAsserter.assertAreEqual(expectedEntities.get(i), actualEntities.get(i));
        }
    }

    protected <T, TRow extends TableRow> List<T> getItems(Class<T> clazz, List<TRow> rows) {
        return rows
                .stream().filter(row -> !row.getCells().isEmpty())
                .map(row -> castRow(clazz, row))
                .toList();
    }

    <TDto> TDto castRow(Class<TDto> dtoClass, TableRow row) {
        var fields = dtoClass.getDeclaredFields();
        var castRow = InstanceFactory.create(dtoClass);

        for (var field : fields) {
            Predicate<Method> isSetterToField = (x) -> x.getName().toLowerCase().endsWith(field.getName().toLowerCase()) && x.getParameterCount() == 1;

            if (Arrays.stream(dtoClass.getDeclaredMethods()).anyMatch(isSetterToField)) {
                var headerName = getHeaderNamesService().getHeaderNameByField(field);
                var headerPosition = getHeaderNamesService().getHeaderPosition(headerName, getColumnHeaderNames());

                if (headerPosition != null) {
                    var setter = Arrays.stream(dtoClass.getDeclaredMethods()).filter(isSetterToField).toList().get(0);
                    var elementValue = row.getCells().get(headerPosition).getText();
                    var parameterType = setter.getParameters()[0].getType();

                    try {
                        field.setAccessible(true);
                        setter.invoke(castRow, TypeParser.parse(elementValue, parameterType));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                var exception = "\nCannot cast Table Row data to Java object. Most probably your class is missing setters.\n" +
                        "Failed while going through field with name:" + field.getName() + "\n";
                throw new IllegalArgumentException(exception);
            }
        }

        return castRow;
    }

    // TODO: reuse TableService
    private void initializeRows() {
        if (rows == null || rows.isEmpty()) {
            rows = this.createAllByXPath(TableRow.class, getTableService().locators().getRowsXpath());
            int rowNumber = 0;
            for (var row : rows) {
                if (!this.createAllByXPath(TableRow.class, getTableService().locators().getHeadersXpath()).isEmpty()) {
                    row.setParentTable(this);
                }
                row.setIndex(rowNumber++);
            }
        }
    }

    public Table setColumn(String headerName) {
        if (this.getColumnHeaderNames() == null) {
            (this).setColumnHeaderNames(new ArrayList<>());
        }

        this.getColumnHeaderNames().add(new HeaderInfo(headerName));
        return this;
    }

    public Table setColumns(List<HeaderInfo> headerNames) {
        if (this.getColumnHeaderNames() == null) {
            (this).setColumnHeaderNames(new ArrayList<>());
        }

        this.getColumnHeaderNames().addAll(headerNames);
        return this;
    }
}
