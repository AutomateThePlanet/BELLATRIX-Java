package solutions.bellatrix.servicenow.pages.serviceNowTableViewPage;

import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.contracts.SnFormField;
import solutions.bellatrix.servicenow.utilities.AssertionsHelper;
import solutions.bellatrix.servicenow.utilities.generators.BaseInstancesUrlGeneration;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.pages.PageAsserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import solutions.bellatrix.web.components.Anchor;


public class Asserts extends PageAsserts<Map> {
    public void assertPartialUrl(String expectedPartialUrl) {
        app().browser().assertLandedOnPage(expectedPartialUrl);
    }

    public <Table extends ServiceNowTable> void assertPartialUrl(Table table) {
        app().browser().assertLandedOnPage(BaseInstancesUrlGeneration.getSnTableViewBaseUrl(table));
    }

    public void assertColumnData(List<String> columnData, String expectedValue) {
        columnData.forEach(d -> assertEquals(d, expectedValue));
    }

    public void assertColumnNotEmpty(List<String> data) {
        data.forEach(d -> assertNotEquals("", d));
    }

    public void assertTableContainsLabels(String... columnsName) {
        var columnsLabel = map().tableColumnsLabel().stream().map(x -> x.getText().split("\n")[0]).toList();

        assertTrue(new HashSet<>(columnsLabel).containsAll(Arrays.stream(columnsName).toList()), "Column did not appear in table");
    }

    public void assertTableContainsLabels(List<String> columnsNames) {
        var columnsLabel = map().tableColumnsLabel().stream().map(x -> x.getText().split("\n")[0]).toList();

        assertTrue(new HashSet<>(columnsLabel).containsAll(columnsNames), "Column did not appear in table");
    }

    public void assertTableLabelsSnFormFields(List<SnFormField> snFormFields) {
        assertTableLabels(snFormFields.stream().map(SnFormField::getLabel).toList());
    }

    public void assertTableLabels(List<String> expectedHeaders) {
        var actualHeaders = map().tableColumnsLabel().stream().map(x -> x.getText().split("\n")[0]).toList();
        AssertionsHelper.assertTableHeaders(expectedHeaders, actualHeaders);
    }

    public void assertTableNotContainsHeader(String expectedHeader) {
        var actualHeaders = map().tableColumnsLabel().stream().map(x -> x.getText().split("\n")[0]).toList();
        assertFalse(actualHeaders.contains(expectedHeader), "%s should not be part of the table headers".formatted(expectedHeader));
    }

    public void assertCellText(int tableRow, String columnName, String expectedValue) {
        var actualValue = map().rowData(tableRow, columnName).getText();
        var errorMessage = String.format("%s does NOT equal to %s", columnName, expectedValue);

        assertEquals(expectedValue, actualValue, errorMessage);
    }

    public void assertTableEmpty() {
        var recordsCount = map().emptyGridTableRows().size();

        assertEquals(0, recordsCount, "Table is not empty");
    }

    public void assertTableRowsAre(int expectedRowCount) {
        var recordsCount = map().rows().size();

        assertEquals(expectedRowCount, recordsCount, "Critical date is displayed");
    }

    public void assertMainHeader(String heading) {
        map().mainHeading().validateTextIs(heading);
    }

    public void assertContextMenu(String heading) {
        map().contextManu().validateTextIs(heading);
    }

    public void assertMainHeaderContains(String heading) {
        map().mainHeading().validateTextContains(heading);
    }

    public void assertTableColumnNotContains(String columnHeader, String... expectedValues) {
        var elementFields = map().columnDataByHeader(columnHeader);

        for (Div element : elementFields) {
            for (String expectedValue : expectedValues) {
                var errorMessage = String.format("%s field is present in %s but it shouldn't be", expectedValue, columnHeader);

                assertFalse(element.getText().contains(expectedValue), errorMessage);
            }
        }
    }

    public void assertNewButtonIsVisible() {
        map().newRecordButton().validateIsVisible();
    }

    public void assertButtonsAreVisible(String... buttonNames) {
        assertButtonsAreVisible(Arrays.stream(buttonNames).toList());
    }

    public void assertButtonsAreVisible(List<String> buttonNames) {
        buttonNames.forEach(x -> map().buttonByName(x).validateIsVisible());
    }

    public void assertFiltersApplied(String... filters) {
        assertFiltersApplied(false, filters);
//        var expectedFilters = new ArrayList<>();
//        expectedFilters.add("All");
//
//        if (filters.length != 0) {
//            expectedFilters.addAll(Arrays.asList(filters));
//        }
//
//        var actualFilters = map().appliedFilters().stream().map(Anchor::getText).toList();
//
//        assertEquals(expectedFilters, actualFilters, "Filters are not as expected");
    }

    public void assertFiltersApplied(boolean isPolarisOn, String... filters) {
        List<String> actualFilters;
        var expectedFilters = new ArrayList<>();
        expectedFilters.add("All");

        if (filters.length != 0) {
            expectedFilters.addAll(Arrays.asList(filters));
        }

        if (isPolarisOn) {
            actualFilters = map().appliedFilters(true).stream().map(Anchor::getText).toList();
        } else {
            actualFilters = map().appliedFilters().stream().map(Anchor::getText).toList();
        }
        assertEquals(expectedFilters, actualFilters, "Filters are not as expected");
    }

    public void assertFilterApplied(String filters) {
        var actualFilters = map().appliedFilters().stream().map(Anchor::getText).toList();

        assertTrue(actualFilters.contains(filters), "Filter is not as apply");
    }

    public void assertTextInColumnFromTableExists(String tableName, String columnLabel, String columnValue) {
        var listValuesInColumn = map().allRowDataTextByColumnLabelOfTable(tableName, columnLabel);
        assertTrue(listValuesInColumn.contains(columnValue), String.format("%s does not exists", columnValue));
    }

    public void assertSecurityMessage(String expectedSecurityMessage) {
        map().securityMessage().validateTextIs(expectedSecurityMessage);
    }

    public void assertTableRowsByColumnCount(int expectedNumber, String tableName, String columnName) {
        var recordsCount = map().allRowDataElementsByColumnLabelOfTable(tableName, columnName).size();

        assertEquals(expectedNumber, recordsCount);
    }

    public void assertNumberRowsByColumn(int expectedCount, String tableName, String columnName) {
        var recordsCount = map().allRowDataElementsByColumnLabelOfTable(tableName, columnName).size();

        assertEquals(expectedCount, recordsCount);
    }

    public void assertContextMenuOptions(List<String> expectedResult) {
        var visibleOptions = map().contextMenuOptions();
        var actualResult = visibleOptions.stream().map(x -> x.getText()).collect(Collectors.toList());
        actualResult.remove(0);

        assertEquals(expectedResult, actualResult);
    }

    public void assertAppliedGrouping(String group) {
        map().listGroup().validateTextContains(group);
    }

    public void assertNewButtonIsNotVisible() {
        map().newRecordButton().validateNotVisible();
    }

    public void assertSearchActionStateAdd() {
        assertEquals("=Add", map().searchInputByAriaLabel("Action").getText(), "Action selected is not Add");
    }

    public void assertSearchActionStateModify() {
        assertEquals("=Modify", map().searchInputByAriaLabel("Action").getText(), "Action selected is not Add");
    }

    public void assertSearchActionStateDelete() {
        assertEquals("=Delete", map().searchInputByAriaLabel("Action").getText(), "Action selected is not Add");
    }

    public void assertGroupListByGroupNameIsNotEmpty(String groupName) {
        var listSize = map().listGroupRowsByGroupName(groupName).size();
        assertTrue(listSize > 0, "No grouping applied");
    }

    public void assertGroupListByGroupNameIsEmpty(String groupName) {
        var listSize = map().listGroupRowsByGroupName(groupName).size();
        assertTrue(listSize == 0, "No grouping applied");
    }
}