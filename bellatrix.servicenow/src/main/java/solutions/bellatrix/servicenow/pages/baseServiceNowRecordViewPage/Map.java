package solutions.bellatrix.servicenow.pages.baseServiceNowRecordViewPage;

import java.util.List;

import solutions.bellatrix.servicenow.contracts.FieldLabel;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.CheckBox;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.FileInput;
import solutions.bellatrix.web.components.Frame;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.Option;
import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.pages.PageMap;

public class Map extends PageMap {
    private static String repairString(String string) {
        return string.toLowerCase().replace(' ', '_');
    }

    public TextArea textAreaByLabel(String inputLabel) {
        var locator = String.format("(//div[contains(@class,'form-group') and .//span[text()='%s']]//textarea)[1]", inputLabel);
        return create().byXPath(TextArea.class, locator);
    }

    public CheckBox checkBoxByLabel(String inputLabel) {
        var xpathLocator = String.format("//div[contains(@class,'form-group') and .//span[text()='%s']]//input[@type='checkbox']", inputLabel);
        return create().byXPath(CheckBox.class, xpathLocator);
    }
    //To be deleted?
    public Button selectFormInput(String element, String fieldName) {
        var locator = String.format("//select[contains(@id,'%s.%s')]", repairString(element), repairString(fieldName));
        return create().byXPath(Button.class, locator);
    }

    public Select selectFormInput(String label) {
        var locator = String.format("//div[@models-type='label' and .//span[text()='%s']]/following-sibling::div/select", label);
        return create().byXPath(Select.class, locator);
    }

    public Anchor cellByTableAndText(String tableName, String cellText) {
        var locator = String.format("//caption[contains(@models-read-label,'%s')]//following-sibling::tbody[contains(@class,'list2_body')]//td/descendant-or-self::*[text()='%s']", tableName, cellText);
        return create().byXPath(Anchor.class, locator);
    }

    public Button buttonByTitle(String title) {
        var xpath = String.format("//button[@models-original-title='%s']", title);
        return create().byXPath(Button.class, xpath);
    }

    public Frame mainTableFrame() {
        return create().byId(Frame.class, "gsft_main");
    }

    public Button closeButton() {
        return create().byXPath(Button.class, "//button[@aria-label='Close']");
    }

    public Button reloadFormButton() {
        return create().byXPath(Button.class, "//div[text()='Reload form']");
    }

    public Div recordTitleValue() {
        return create().byXPath(Div.class, "//div[@class[contains(string(),'navbar-title-display-value')]]");
    }

    public Div recordHeading() {
        return create().byXPath(Div.class, "//div[contains(@class,'navbar-title-caption')]");
    }

    public Div recordNumberHeading() {
        return create().byXPath(Div.class, "//div[@class='navbar-title-display-value']");
    }

    public Button backButton() {
        return create().byXPath(Button.class, "//button[@aria-label='Back']");
    }

    public Button buttonInRightNavigationBarByText(String text) {
        var locator = String.format("//div[@class='navbar-right']//button[text()='%s']", text);
        return create().byXPath(Button.class, locator);
    }

    public Button buttonInRightNavigationBarById(String id) {
        var locator = String.format("//button[@id='%s']", id);
        return create().byXPath(Button.class, locator);
    }

    public <Label extends FieldLabel> TextArea takeInput(Label inputLabel) {
        return takeInput(inputLabel.getLabel());
    }

    public TextArea takeInput(String inputLabel) {
        var locator = String.format("(//div[contains(@class,'form-group') and .//span[text()='%s']]//descendant::input[not(@type='hidden')])[1]", inputLabel);
        return create().byXPath(TextArea.class, locator);
    }

    public TextArea takeInputByLabel(String inputLabel) {
        var locator = String.format("(//div[contains(@class,'form-group') and .//span[text()='%s']]//input[1])", inputLabel);
        return create().byXPath(TextArea.class, locator);
    }

    public Button searchButtonByLabel(String label) {
        var locator = String.format("//div[contains(@class,'form-group') and .//span[text()='%s']]/descendant::button[@id[contains(string(),'lookup')]]/span", label);
        return create().byXPath(Button.class, locator);
    }

    public <Label extends FieldLabel> Option selectedOptionByLabel(Label inputLabel) {
        return selectedOptionByLabel(inputLabel.getLabel());
    }

    public Option selectedOptionByLabel(String inputLabel) {
        var locator = String.format("//div[contains(@class,'form-group') and .//label[./descendant-or-self::*[text()='%s']]]//option[@selected='SELECTED']", inputLabel);
        return create().byXPath(Option.class, locator);
    }

    public List<Label> allLabels() {
        var xpathLocator = "//span[@class='label-text']/parent::label";
        return create().allByXPath(Label.class, xpathLocator);
    }

    public Button submitButton() {
        var xpathLocator = "//button[text()='Submit']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button updateButton() {
        var xpathLocator = "//button[text()='Update']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button updateLastButton() {
        var xpathLocator = "(//button[text()='Update'])[last()]";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button deleteButton() {
        var xpathLocator = "//button[text()='Delete']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public Div deleteConfirmationMessage() {
        var xpathLocator = "//h2[@id='delete_confirm_form_title']/ancestor::div[@class='modal-content']";
        return create().byXPath(Div.class, xpathLocator);
    }

    public Button popUpConfirmationCancelButton() {
        return create().byXPath(Button.class, "//div[@class='modal-dialog modal-alert']//button[@id='cancel_button']");
    }

    public Button popUpConfirmationDeleteButton() {
        return create().byXPath(Button.class, "//div[@class='modal-dialog modal-alert']//button[@id='ok_button']");
    }

    public Label deletePopUpTextContent() {
        return create().byXPath(Label.class, "//div[@class='modal-dialog modal-alert']//div[@class='modal-content']//div[text() and not(./button)]");
    }

    public List<Div> listButtons(String button) {
        var xpathLocator = String.format("//button[text()='%s']", button);
        return create().allByXPath(Div.class, xpathLocator);
    }

    public Button popUpDeleteButton() {
        return create().byXPath(Button.class, "//div[@class='modal-content' and .//header[@aria-label='Confirmation']]//button[@id='ok_button']");
    }

    public Button popUpCancelButton() {
        return create().byXPath(Button.class, "//div[@class='modal-content' and .//header[@aria-label='Confirmation']]//button[@id='cancel_button']");
    }

    public Label popUpMessage() {
        return create().byXPath(Label.class, "//div[@class='modal-content' and .//header[@aria-label='Confirmation']]//div[text() and not(@align)]");
    }

    public Button tabByLabel(String tabLabel) {
        var xpathLocator = String.format("//span[@class='tab_caption_text' and contains(text(),'%s')]", tabLabel.replace(" ","\u00A0"));
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button tabByLabel(FieldLabel fieldLabel) {
        var xpathLocator = String.format("//span[@class='tab_caption_text' and contains(text(),'%s')]", fieldLabel.getLabel());
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button tabByPartialLabel(String tabLabel) {
        var xpathLocator = String.format("//span[@class='tab_caption_text' and contains(text(),'%s')]", tabLabel);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Span labelRequiredMarker(String field) {
        var xpathLocator = String.format("//label[./span[@class='label-text' and text()='%s']]/span[contains(concat(' ',normalize-space(@class),' '),' required-marker')]", field);
        return create().byXPath(Span.class, xpathLocator);
    }

    public Frame innerFrame() {
        return create().byCss(Frame.class, "#gsft_main");
    }

    public Div formGroup(String label) {
        var xpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' form-group ') and .//span[@class='label-text' and text()='%s']]", label);
        return create().byXPath(Div.class, xpathLocator);
    }

    public Button manageAttachmentsButton() {
        var xpathLocator = "//button[@aria-label='Manage Attachments']";
        return create().byXPath(Button.class, xpathLocator);
    }

    public FileInput inputWithIdAttachFile() {
        return create().byXPath(FileInput.class, "//input[@id='attachFile']");
    }

    public Anchor viewAttachedFileAnchor() {
        return create().byXPath(Anchor.class, "//tbody[@id='attachment_table_body']//a[contains(@aria-label,'View')]");
    }

    public Anchor renameAttachedFileAnchor() {
        return create().byXPath(Anchor.class, "//tbody[@id='attachment_table_body']/descendant::a[contains(text(),'rename')]");
    }

    public Button addAttachmentHeaderButton() {
        return create().byId(Button.class, "header_add_attachment");
    }

    public List<Button> bottomTablesTabs() {
        var xpathLocator = "//div[@id='tabs2_list']//span[@class='tab_caption_text']";
        return create().allByXPath(Button.class, xpathLocator);
    }

    public Button bottomTablesLabelsButton(String label) {
        var locator = String.format("//div[@id='tabs2_list']//span[@class='tab_caption_text' and contains(text(),'%s')]", label.replace(" ", "\u00A0"));
        return create().byXPath(Button.class, locator);
    }

    public Button newButtonByHeading(String heading) {
        var xpathLocator = String.format("//div[@aria-label='%s, filtering toolbar']//button[text()='New']", heading);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button newButtonByPartialHeading(String heading) {
        var xpathLocator = String.format("//div[@role='region'][@aria-label[contains(string(),'%s')]]//button[text()='New']", heading);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button buttonByName(String buttonName) {
        var xpathLocator = String.format("//button[text()='%s']", buttonName);
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<TextInput> getSecondTableColumns(String tabCaption) {
        String xpath = String.format("//div[@aria-hidden='false' and @tab_caption='%s']//thead//th[@role='columnheader']", tabCaption);
        return create().allByXPath(TextInput.class, xpath);
    }

    public Div allertMessage() {
        var xpathLocator = "//div[@class='outputmsg_text']";
        return create().byXPath(Div.class, xpathLocator);
    }

    public List<Div> listAllertMessages() {
        var xpathLocator = "//div[@class='outputmsg_text']";
        return create().allByXPath(Div.class, xpathLocator);
    }

    public TextInput inputByLabel(String inputLabel) {
        var xpathLocator = String.format("//input[@aria-label='%s']", inputLabel);
        return create().byXPath(TextInput.class, xpathLocator);
    }

    private WebComponent polarisMainMacroponent() {
        return create().byXPath(WebComponent.class, "//*[contains(name(),'macroponent')]").toShadowRootToBeAttached();
    }

    public Frame polarisMainPageFrame() {
        return polarisMainMacroponent().shadowRootCreateByCss(Frame.class, "#gsft_main");
    }

    public List<Option> selectFormInputOptions(String element, String fieldName) {
        var locator = String.format("//select[contains(@id,'%s.%s')]/option", repairString(element), repairString(fieldName));
        return create().allByXPath(Option.class, locator);
    }

    public List<Div> columnDataByHeader(String columnHeader) {
        var locator = String.format("//tbody[contains(@class,'list2_body')']//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]", columnHeader);

        return create().allByXPath(Div.class, locator);
    }

    public List<Anchor> columnDataByTableAndColumnName(String table, String columnName) {
        var xpath = String.format("//tbody[contains(@class,'list2_body') and ./tr[@record_class='%s']]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]//a", table, columnName);
        return create().allByXPath(Anchor.class, xpath);
    }

    public Button lastSaveButton() {
        var xpathLocator = "(//button[text()='Save'])[last()]";
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<Div> columnDataByTabNumberAndHeader(int columnNumber, String header) {
        var xpathLocator = "(//tbody[contains(@class,'list2_body')])[%d]//td[count(//table[not(@id='table_clone')]//th[.//a[text()='%s']]/preceding-sibling::th)+1]".formatted(columnNumber, header);
        return create().allByXPath(Div.class, xpathLocator);
    }
}