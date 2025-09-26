package solutions.bellatrix.servicenow.pages.uib.pages.uibTableViewPage;

import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;
import solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage;
import solutions.bellatrix.web.components.advanced.grid.GridCell;

public class UibTableViewPage<MapT extends Map, AssertsT extends Asserts<MapT>> extends BaseUIBPage<MapT, AssertsT> {
    private String url;

    @Override
    protected String getUrl() {
        return url;
    }

    protected ServiceNowPage serviceNowPage() {
        return app().createPage(ServiceNowPage.class);
    }

    public void addAnyColumnByListAction(String columnName, String locator) {
        map().listActionsIcon().click();
        map().editColumnsButton().click();
        map().searchFieldOnEditList().setText(columnName);
        var columnCheckbox = map().anyColumnCheckboxOnEditColumnList(locator);
        var isChecked = columnCheckbox.getAttribute("checked");
        if (isChecked == null || "null".equals(isChecked)) {
            columnCheckbox.hover();
            columnCheckbox.check();
            map().okButtonOnEditList().click();
        } else {
            map().closeDialogueOnEditList().click();
        }
    }

    public void removeAnyColumnByListAction(String columnName, String locator) {
        map().listActionsIcon().click();
        map().editColumnsButton().click();
        map().searchFieldOnEditList().setText(columnName);
        var columnCheckbox = map().anyColumnCheckboxOnEditColumnList(locator);
        var isChecked = columnCheckbox.getAttribute("checked");
        if (isChecked.equals("true")) {
            columnCheckbox.hover();
            columnCheckbox.check();
            map().okButtonOnEditList().click();
        } else {
            map().closeDialogueOnEditList().click();
        }
    }

    public String getValueFromVerificationDataGrid(String columnName, String questionName) {
        var workOrderVerificationDataGrid = map().dataGrid();
        var col = workOrderVerificationDataGrid.getColumn("Question Text");
        String result = "";
        for (var c : col) {
            if (c.getText().trim().equals(questionName)) {
                result = workOrderVerificationDataGrid.getRow(c.getRow()).getCell(columnName).getText();
                break;
            }
        }
        return result;
    }

    public void openRecordFromDataGrid(String columnName, String recordValue) {
        var workGrid = map().dataGrid();
        var colValues = workGrid.getColumn(columnName);
        var result = new GridCell();
        for (var c : colValues) {
            if (c.getText().trim().equals(recordValue)) {
                result = c;
                break;
            }
        }
        result.click();
    }

    public void openRecordByColumnValue(String columnName, String recordValue) {
        filterByColumnValue(columnName, recordValue);
        openRecordFromDataGrid(columnName, recordValue);
    }

    public void openColumnFilter(String columnName) {
        var filterButton = map().getColumnFilter(columnName);
        filterButton.hover();
        filterButton.click();
    }

    public void filterByColumnValue(String columnName, String value) {
        var filterButton = map().getColumnFilter(columnName);
        filterButton.hover();
        filterButton.click();

        var inputField = map().inputPopoverFilter();
        var applyButton = map().popupButtonByText("Apply");

        inputField.setText(value);
        applyButton.click();
    }

    public void showAttachments() {
        map().getAttachmentsSidebarButton().click();
    }
}
