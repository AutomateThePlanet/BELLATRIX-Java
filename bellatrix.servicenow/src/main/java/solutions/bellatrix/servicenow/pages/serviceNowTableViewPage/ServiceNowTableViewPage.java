package solutions.bellatrix.servicenow.pages.serviceNowTableViewPage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.contracts.SnFormField;
import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;
import solutions.bellatrix.servicenow.utilities.generators.BaseInstancesUrlGeneration;
import solutions.bellatrix.servicenow.utilities.UserInteraction;
import solutions.bellatrix.web.pages.WebPage;
import solutions.bellatrix.web.components.Div;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceNowTableViewPage extends WebPage<Map, Asserts> {
    private String url;

    @Override
    protected String getUrl() {
        return url;
    }

    protected ServiceNowPage serviceNowPage() {
        return app().createPage(ServiceNowPage.class);
    }

    public <Table extends ServiceNowTable> void open(Table table) {
        url = BaseInstancesUrlGeneration.getSnTableViewBaseUrl(table);
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    public <Table extends ServiceNowTable> void open(Table table, boolean isPolaris) {
        url = BaseInstancesUrlGeneration.getSnTableViewBaseUrl(table);
        super.open();
        if (!isPolaris) {
            browser().switchToFrame(map().mainTableFrame());
        }
    }

    @Override
    public void waitForPageLoad() {
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void openRecordFromMainTable(int recordNumber) {
        map().recordNumberByRowCount(recordNumber).click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void selectRecordFromMainTable(int recordNumber) {
        map().recordNumberByRowCount(recordNumber).click();
    }

    public void openRecordInfoFromMainTable(int recordNumber) {
        map().infoRecordSign(recordNumber).hover();
        map().infoRecordSign(recordNumber).click();
        map().openRecordButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void openRecordFromMainTable(int recordNumber, int columnNumber) {
        map().recordNumberByRowCountAndColumnCount(recordNumber, columnNumber).toBeClickable().waitToBe();
        map().recordNumberByRowCountAndColumnCount(recordNumber, columnNumber).click();
        browser().waitForAjax();
    }

    public void openRecord(int recordNumber, int columnNumber) {
        map().recordNumberByRowCountAndColumnCount(recordNumber, columnNumber).hover();
        map().recordNumberByRowCountAndColumnCount(recordNumber, columnNumber).toBeClickable().waitToBe();
        map().recordNumberByRowCountAndColumnCount(recordNumber, columnNumber).click();
        map().openRecordButton().click();
    }

    public void openRecordFromMainTable(int recordNumber, String columnContent) {
        map().recordNumberByRowCountAndColumnContent(recordNumber, columnContent).toBeClickable().waitToBe();
        map().recordNumberByRowCountAndColumnContent(recordNumber, columnContent).click();
        browser().waitForAjax();
    }

    public void openRecordFromMainTable(String columnName, String columnContent) {
        searchBy(columnName, columnContent);
        openRecordFromMainTable(1);
        browser().waitForAjax();
    }

    public void selectRecordFromPopUpWindowMainTable(String columnName, String columnContent) {
        searchBy(columnName, columnContent);
        selectRecordFromMainTable(1);
    }

    public void clickOnCellData(String cellContainingText) {
        map().cellByText(cellContainingText).click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void createNewRecordForCurrentTable() {
        map().newRecordButton().toBeVisible().waitToBe();
        map().newRecordButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void searchByName(String name) {
        searchBy("Name", name);
    }

    public void searchByNumber(String number) {
        searchBy("Number", number);
    }

    public void searchBy(SnFormField searchLabelColumn, String searchedText) {
        searchBy(searchLabelColumn.getLabel(), searchedText);
    }

    public void searchBy(String searchLabelColumn, String searchedText) {
        searchBy(searchLabelColumn, searchedText, true);
    }

    public void searchBy(String searchLabelColumn, String searchedText, Boolean toCheckSearchRowVisible) {
        if (toCheckSearchRowVisible) {
            if (!map().searchInputByAriaLabel(searchLabelColumn).isVisible()) {
                UserInteraction.wait2Seconds();
                map().searchToggleIcon().toBeClickable();
                map().searchToggleIcon().click();
                UserInteraction.wait2Seconds();
            }
        }

        map().searchInputByAriaLabel(searchLabelColumn).setText(searchedText + Keys.ENTER);
        browser().waitForAjax();
    }

    public void searchRemovingArchivedFilterBy(String searchLabelColumn, String searchedText) {
        clickOnAllLink();
        if (map().searchInputByAriaLabel(searchLabelColumn).isVisible()) {
            UserInteraction.wait2Seconds();
            searchBy(searchLabelColumn, searchedText, false);
        } else {
            map().searchToggleIcon().toBeClickable();
            map().searchToggleIcon().click();
            UserInteraction.wait2Seconds();
            searchBy(searchLabelColumn, searchedText, false);
        }
    }

    public void searchBy(String searchLabelColumn, String searchedText, String partialUrl) {
        map().searchInputByAriaLabel(searchLabelColumn).setText(searchedText + Keys.ENTER);
        browser().waitForAjax();
        browser().refresh();
        browser().waitForPartialUrl(partialUrl);
        browser().waitForAjax();
    }

    public void clickOnAllLink() {
        map().allLink().click();
        browser().waitForAjax();
    }

    public void clickButtonByLabel(String label) {
        map().siteButtonLabel(label).click();
        app().browser().waitForAjax();
        map().siteButton(label).click();
        app().browser().waitForAjax();
    }

    public String getTableRecordData(int rowNumber, String columnName) {
        return map().rowData(rowNumber, columnName).getText();
    }

    public void changeTableTabByPartialLabel(String tabPartialLabel) {
        map().tabHeaderByPartialText(tabPartialLabel).click();
        browser().waitForAjax();
    }

    public void clickOnEditByTableName(String tableName) {
        map().editButtonByTableName(tableName).scrollToVisible();
        map().editButtonByTableName(tableName).click();
        browser().waitForAjax();

        browser().switchToLastTab();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void clickOnNewButton() {
        map().newRecordButton().click();
        waitForPageLoad();
    }

    public void clickTwiceOnTableHeader(String label) {
        map().tableHeader(label).click();
        map().tableHeader(label).click();
        browser().waitForAjax();
    }

    public void sortAscOrder(String label) {
        map().tableHeader(label).click();
        if (!map().sortColumnSign().getAttribute("class").contains("icon-vcr-up")) {
            map().tableHeader(label).click();
        }
    }

    public List<String> columnDataByHeaderText(String columnHeader) {
        return map().columnDataByHeader(columnHeader).stream().map(Div::getText).toList();
    }

    public List<String> formatColumnData(String columnHeader) {
        return columnDataByHeaderText(columnHeader).stream().map(d -> d.split(":")[0]).toList();
    }

    public void assertTableColumnContains(String columnHeader, String... expectedValues) {
        var columnElementsText = columnDataByHeaderText(columnHeader);
        for (String expectedValue : expectedValues) {
            var errorMessage = String.format("%s field is not present in %s.", expectedValue, columnHeader);

            assertTrue(columnElementsText.contains(expectedValue), errorMessage);
        }
    }

    public void clickCellColumnElementBySearchCellValue(String tableName, String columnSearch, String searchValue, String elementToClickColumn) {
        var rowsData = map().allRowDataTextByColumnLabelOfTable(tableName, columnSearch);
        var searchRow = rowsData.indexOf(searchValue);
        map().rowDataByTable(tableName, searchRow, elementToClickColumn).click();
    }

    public void clickButtonByName(String name) {
        map().buttonByName(name).click();
        browser().waitForAjax();
    }

    public void assertTableColumnContainsText(String columnHeader, String expectedValue) {
        searchBy(columnHeader, expectedValue);

        assertTableColumnContains(columnHeader, expectedValue);
    }

    public void assertTableColumnDoesNotContain(String columnHeader, String... expectedValues) {
        var columnElementsText = columnDataByHeaderText(columnHeader);
        for (String expectedValue : expectedValues) {
            var errorMessage = String.format("%s field is not present in %s.", expectedValue, columnHeader);

            assertFalse(columnElementsText.contains(expectedValue), errorMessage);
        }
    }

    public void openRowRecord(String row) {
        map().infoIconButton(row).hover();
        map().infoIconButton(row).click();
        browser().waitForAjax();
        map().openRecordButton_().click();
        browser().waitUntilPageLoadsCompletely();
    }

    public void searchingBy(String searchedOption, String searchedText) {
        selectSearchOptionFromDropdown(searchedOption);
        enterTextInSearchField(searchedText);
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void selectSearchOptionFromDropdown(String searchedOption) {
        map().searchOptionsDropdown().click();
        browser().waitForAjax();
        map().searchOption(searchedOption).click();
        browser().waitForAjax();
    }

    public void enterTextInSearchField(String searchedText) {
        map().searchField().toBeVisible().waitToBe();
        map().searchField().setText(searchedText + Keys.ENTER);
        browser().waitForAjax();
    }

    public void waitThePageToLoad() {
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void searchByOption(String searchedOption, String searchedText) {
        map().searchDropdown().click();
        map().searchDropdownOption(searchedOption).click();
        browser().waitForAjax();
        map().searchField().setText(searchedText + Keys.ENTER);
    }

    public void clickInfoRecordRow(int row) {
        map().infoRecordSign(row).hover();
        map().infoRecordSign(row).click();
    }

    public void clickInfoRecord() {
        map().infoRecordSign(1).hover();
        map().infoRecordSign(1).click();
    }

    public void sortColumnByNameDescending(String columnName) {
        if (map().sortButtonByColumnName(columnName).getWrappedElement().getAttribute("class").contains("icon-vcr-up")) {
            map().sortButtonByColumnName(columnName).click();
        }
    }

    public void openContextMenuByLabel(String columnName) {
        Actions actions = new Actions(browser().getWrappedDriver());
        actions.contextClick(map().tableColumnName(columnName).getWrappedElement()).perform();
    }

    public List<String> listAttachments() {
        var list = new ArrayList<String>();
        map().listAttachmentsElements().forEach(x -> list.add(x.getText()));
        return list;
    }

    public void assertListOfAttachments() {
        var list = listAttachments();
    }
}