package solutions.bellatrix.servicenow.pages.serviceNowTableViewPage;

import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.pages.PageMap;
import java.util.ArrayList;
import java.util.List;

public class Map extends PageMap {
    public Anchor mainHeading() {
        return create().byXPath(Anchor.class, "//h1[contains(@class,'navbar-title')]/a");
    }

    public Anchor contextManu() {
        return create().byXPath(Anchor.class, "//a[@models-original-title='Properties Context Menu']");
    }

    public Anchor recordNumberByRowCount(int rowCount) {
        String xpathLocator = String.format("//tbody[contains(@class,'list2_body')]/tr[%s]/td[3]/a", rowCount);

        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Anchor recordNumberByRowCountAndColumnCount(int rowCount, int columnCount) {
        String xpathLocator = String.format("//tbody[contains(@class,'list2_body')]/tr[%s]/td[%s]/a", rowCount, columnCount);

        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Anchor recordNumberByRowCountAndColumnContent(int rowCount, String columnContent) {
        String xpathLocator = String.format("//tbody[contains(@class,'list2_body')]/tr[%s]/td/a[contains(text(),'%s')]", rowCount, columnContent);

        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Button newRecordButton() {
        return create().byXPath(Button.class, "//button[@id='sysverb_new' and @type='submit']");
    }

    public Button buttonByName(String buttonName) {
        var xpathxpathLocator = String.format("//button[text()='%s']", buttonName);
        return create().byXPath(Button.class, xpathxpathLocator);
    }

    public TextInput searchInputByAriaLabel(String areaLabel) {
        var xpathLocator = String.format("//table[not(@id='table_clone')]//tr[contains(@class,'list_header_search_row')]//input[@aria-label='Search column: %s']", areaLabel.toLowerCase());

        return create().byXPath(TextInput.class, xpathLocator);
    }

    public List<Div> tableColumnsLabel() {
        var xpathLocator = "//div[@role='main']//th[contains(@class,' list_header_cell ')]//a[contains(concat(' ', normalize-space(@class), ' '), ' column_head ')]";

        return create().allByXPath(Div.class, xpathLocator);
    }

    public Button editButtonByTableName(String tableName) {
        var xpathLocator = String.format("//div[contains(@aria-label,'%s')]//button[text()='Edit']", tableName);

        return create().byXPath(Button.class, xpathLocator);
    }

    public List<Div> columnDataByHeader(String columnHeader) {
        var xpathLocator = String.format("//tbody[contains(@class,'list2_body')]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]", columnHeader);

        return create().allByXPath(Div.class, xpathLocator);
    }

    public Button tabHeaderByPartialText(String tabText) {
        var xpathLocator = String.format("//span[@class='tab_header']//span[contains(text(), '%s')]", tabText);

        return create().byXPath(Button.class, xpathLocator);
    }

    public Button tableHeader(String containingText) {
        var xpathLocator = String.format("//table[contains(@class, 'list_table')]//a[contains(@class,'column_head') and text()='%s']", containingText);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Div sortColumnSign() {
        return create().byXPath(Div.class, "//i[contains(@class,'list-column-icon') and contains(@style,'visible')]");
    }

    public Anchor rowData(int rowNumber, String columnName) {
        var xpathLocator = String.format("(//tbody[contains(@class,'list2_body')]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]/descendant-or-self::*[text()])[%d]", columnName, rowNumber);

        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Button rowData(String columnName) {
        var xpathLocator = String.format("//tbody[contains(@class,'list2_body')]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]", columnName);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Anchor rowDataByTable(String tableName, int rowNumber, String columnName) {
        var xpathLocator = String.format("(.//tbody[contains(@class,'list2_body')]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]/descendant-or-self::*[text()])[%d]", columnName, rowNumber);

        return detailedTableDataName(tableName).createByXPath(Anchor.class, xpathLocator);
    }

    public TextInput rowDataOfRecord(int rowNumber, String columnName) {
        var xpathLocator = String.format("(//tbody[contains(@class,'list2_body')]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]/descendant-or-self::*[text()])[%d]", columnName, rowNumber);

        return create().byXPath(TextInput.class, xpathLocator);
    }

    public Anchor cellByText(String cellText) {
        var xpathLocator = String.format("//tbody[contains(@class,'list2_body')]//td/descendant-or-self::*[text()='%s']", cellText);

        return create().byXPath(Anchor.class, xpathLocator);
    }

    public List<Anchor> allRowDataByColumnLabel(String columnLabel) {
        var xpathLocator = String.format("//tbody[contains(@class,'list2_body')]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]//descendant-or-self::*[text()]", columnLabel);

        return create().allByXPath(Anchor.class, xpathLocator);
    }

    public List<Div> emptyGridTableRows() {
        return create().allByXPath(Div.class, "//thead[./tr[@id]]//following-sibling::tbody//tr[@class='list2_no_records']");
    }

    public List<Div> rows() {
        return create().allByXPath(Div.class, "//tbody[contains(@class,'list2_body')]/tr");
    }

    public TextInput searchBoxByMainHeader(String mainHeader) {
        var xpathLocator = String.format("//div[@class='navbar-header' and .//span[text()='%s']]//following-sibling::span//input[@placeholder='Search']", mainHeader);
        return create().byXPath(TextInput.class, xpathLocator);
    }

    public Frame mainTableFrame() {
        return create().byId(Frame.class, "gsft_main");
    }

    public List<Anchor> appliedFilters() {
        return appliedFilters(false);
    }

    public List<Anchor> appliedFilters(boolean isPolarisOn) {
        String xpathLocator;
        if (isPolarisOn) {
            xpathLocator = "//a[@role='listitem']";
        } else {
            xpathLocator = "//*[contains(concat(' ',normalize-space(@class),' '),' icon-filter ')]/following-sibling::*//*[@role='listitem']";
        }
        return create().allByXPath(Anchor.class, xpathLocator);
    }

    public Div detailedTableDataName(String tableDataName) {
        var xpathLocator = String.format("//table[@aria-label='%s'][not(@id='table_clone')]", tableDataName);
        return create().byXPath(Div.class, xpathLocator);
    }

    public List<Div> allRowDataElementsByColumnLabelOfTable(String tableName, String columnLabel) {
        var xpathLocator = String.format(".//tbody[contains(@class,'list2_body')]//td[count(//table[@aria-label='%s'][not(@id='table_clone')]/descendant::th[.//a[text()='%s']]/preceding-sibling::th)+1]//descendant-or-self::*[text()]", tableName, columnLabel);

        return detailedTableDataName(tableName).createAllByXPath(Div.class, xpathLocator);
    }

    public List<String> allRowDataTextByColumnLabelOfTable(String tableName, String columnLabel) {
        var listElements = allRowDataElementsByColumnLabelOfTable(tableName, columnLabel);
        List<String> listText = new ArrayList<>();
        listElements.forEach(e -> listText.add(e.getText()));

        return listText;
    }

    public Anchor infoRecordSign(int row) {
        var xpathLocator = String.format("//tr[%s]//a[contains(concat(' ',normalize-space(@class),' '),' btn-icon')]", row);
        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Button openRecordButton() {
        return create().byXPath(Button.class, "//*[text()='Open Record']");
    }

    public Button searchToggleIcon() {
        return create().byXPath(Button.class, "//button[contains(@class, 'list_header_search_toggle')]");
    }

    public Button allLink() {
        return create().byXPath(Button.class, "//a//b[text()='All']");
    }

    public Span securityMessage() {
        var xpath = "//h3";
        return create().byXPath(Span.class, xpath);
    }

    public Button contextMenuButtonByText(String contextText) {
        var xpathLocator = String.format("//div[contains(text(), '%s')]", contextText);
        return contextMenu().createByXPath(Button.class, xpathLocator);
    }

    public Button contextMenuTextField(String contextText) {
        var xpathLocator = String.format("//div[contains(@style, 'visibility: visible;')]//div[contains(text(), '%s')]", contextText);
        return create().byXPath(Button.class, xpathLocator);
    }

    private Div contextMenu() {
        var xpathLocator = "//div[contains(@style, 'visibility: visible;')]";
        return create().byXPath(Div.class, xpathLocator);
    }

    public List<Div> contextMenuOptions() {
        var xpathLocator = ".//div[@class='context_item']";
        return contextMenu().createAllByXPath(Div.class, xpathLocator);
    }

    public Button tableHeaderDropdownButton(String tableHeader) {
        var xpathLocator = ".//preceding-sibling::i";
        return tableHeader(tableHeader).createByXPath(Button.class, xpathLocator);
    }

    public Button siteButton(String label) {
        var xpathLocator = String.format("//table[contains(@class, 'list_table')]//a[contains(@class,'column_head') and text()='%s']//following-sibling::i", label);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button siteButtonLabel(String label) {
        var xpathLocator = String.format("//table[contains(@class, 'list_table')]//a[contains(@class,'column_head') and text()='%s']", label);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Div listGroup() {
        var xpathLocator = "//span[contains(@class,'list_group')]";
        return create().byXPath(Div.class, xpathLocator);
    }

    public TextInput searchByMessage() {
        var xpathLocator = "//table[@id='syslog_table']//tr[@class='list_header list_header_search_row']/td[@name='message']//input[@type='search']";
        return create().byXPath(TextInput.class, xpathLocator);
    }

    public TextInput messageField() {
        var xpathLocator = "//div[@id='syslog']//td[5]/div/div/div/input";
        return create().byXPath(TextInput.class, xpathLocator);
    }

    public Button infoIconButton(String row) {
        var xpathLocator = String.format("(//a[contains(@class,'table-btn-lg') and contains(@class,'icon-info')])['%s']", row);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button openRecordButton_() {
        return create().byCss(Button.class, ".btn.btn-default.btn-sm.pull-right");
    }

    public Button searchOptionsDropdown() {
        return create().byXPath(Button.class, "//select[contains(@class,'form-control') and @role='listbox']");
    }

    public Button searchOption(String option) {
        var xpathLocator = String.format("//option[contains(text(),'%s')]", option);
        return create().byXPath(Button.class, xpathLocator);
    }

    public TextArea searchField() {
        return create().byCss(TextArea.class, "[role='search'] input");
    }

    public Button infoIconRow() {
        return create().byCss(Button.class, ".list_odd.list_row  a[role='button']");
    }

    public Frame gsftMainFrame() {
        return create().byXPath(Frame.class, "//*[@id='gsft_main']");
    }

    public Button searchedRecord(String number) {
        var xpathLocator = String.format("//a[@class='linked formlink' and text()='%s']", number);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button sortUpButtonByColumnName(String columnName) {
        var xpathLocator = String.format("//span[@class='list_header_cell_container']/a[text()='%s']/following-sibling::span/descendant::i[contains(@class,'sort-icon-padding list-column-icon icon-vcr-up')]", columnName);

        return create().byXPath(Button.class, xpathLocator);
    }

    public Button sortDownButtonByColumnName(String columnName) {
        var xpathLocator = String.format("//span[@class='list_header_cell_container']/a[text()='%s']/following-sibling::span/descendant::i[contains(@class,'sort-icon-padding list-column-icon icon-vcr-down')]", columnName);

        return create().byXPath(Button.class, xpathLocator);
    }

    public Button sortButtonByColumnName(String columnName) {
        var xpathLocator = String.format("//span[@class='list_header_cell_container']/a[text()='%s']/following-sibling::span/descendant::i[contains(@class,'sort-icon-padding list-column-icon')]", columnName);

        return create().byXPath(Button.class, xpathLocator);
    }

    public Button searchDropdown() {
        return create().byXPath(Button.class, "//span[@class='input-group-addon input-group-select']/span");
    }

    public Button searchDropdownOption(String option) {
        var xpathLocator = String.format("//span[@class='input-group-addon input-group-select']/span/select/option[contains(text(),'%s')]", option);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button menuView() {
        var xpathLocator = "//h1[@class[contains(string(),'list_title')]]";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button viewOption() {
        var xpathLocator = "//div[@models-context-menu-label='View']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button subMenuOption(String optionName) {
        var xpathLocator = String.format("//div[@class='context_item'][text()='%s']", optionName);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button tableColumnName(String columnName) {
        var xpathLocator = String.format("//table[not(contains(@id,'table_clone'))]/descendant::a[text()='%s']", columnName);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button contextMenuOptionByName(String optionName) {
        var xpathLocator = String.format("//div[text()='%s']", optionName);
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<Span> listGroupRowsByGroupName(String groupName) {
        var xpathLocator = String.format("//span[@class='list_group'][contains(text(),'%s')]", groupName);
        return create().allByXPath(Span.class, xpathLocator);
    }

    public Button collapseAllButton() {
        var xpathLocator = "//table[not(contains(@id,'table_clone'))]/descendant::button[@aria-label='Collapse all groups']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button expandAllButton() {
        var xpathLocator = "//table[not(contains(@id,'table_clone'))]/descendant::button[@aria-label='Expand all groups']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button viewAllAttachments() {
        var xpathLocator = "//a[@models-original-title='View All Attachments']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<Div> listAttachmentsElements() {
        var xpathLocator = "//div[@id='attachment_dialog_list']/descendant::a[@aria-label[contains(string(),'Download')]]";
        return create().allByXPath(Div.class, xpathLocator);
    }

    public Button downloadAllAttachments() {
        var xpathLocator = "//input[@id='download_all_button']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<CheckBox> listSelectAttachments() {
        var xpathLocator = "//input[@aria-label[contains(string(),'Select attachment')]]";
        return create().allByXPath(CheckBox.class, xpathLocator);
    }
    public List<Div> tableHeadersByTabLabel(String tabLabel) {
        var xpathLocator = String.format("//table[contains(@id,'REL') and ./caption[contains(@models-edit-label,'%s')]]//th[@name and not(@name='search')]", tabLabel);
        return create().allByXPath(Div.class, xpathLocator);
    }

    public Button menuItemByContainingText(String containingText) {
        var xpathLocator = String.format("//div[@role='menuitem' and contains(text(),'%s')]", containingText);
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<Button> allMenuItems() {
        var xpathLocator = "//div[@role='menuitem' and text()]";
        return create().allByXPath(Button.class, xpathLocator);
    }

    public Div totalRowsInfo() {
        var xpathLocator = "//tr[contains(@class, 'list_nav')]/descendant::span[@class=' list_row_number_input ']/span[contains(@id, 'total_rows')]";
        return create().byXPath(Div.class, xpathLocator);
    }
}