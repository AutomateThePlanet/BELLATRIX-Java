package solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage;

import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.PageMap;

import java.util.List;

public class Map extends PageMap {
    public ShadowRoot customUiMainMacroponent() {
        return create().byXPath(ShadowRoot.class, "//*[contains(name(),'macroponent')]").getShadowRoot();
    }

    public Button closePopUp()
    {
        return customUiMainMacroponent().createByCss(Button.class,"div > div.controls.info > now-button-iconic");
    }

    public Button tabWithShadowDom(String tabName) {
        var xpathLocator = String.format("//div[contains(@class, 'tabset-wrapper')]/descendant::span[@class='now-tab-label'][text()='%s']/ancestor::button[@role='tab']".formatted(tabName));
        return customUiMainMacroponent().createByXPath(Button.class, xpathLocator);
    }

    protected ShadowRoot recordInputWithShadowDom(String label) {
        var xpathLocator = String.format("//now-record-typeahead[@name='%s']", label);
        return customUiMainMacroponent().createByXPath(ShadowRoot.class, xpathLocator);
    }

    public ShadowRoot recordMultiInputWithShadowDom(String label) {
        var xpathLocator = String.format("//now-record-typeahead-multiple[@name='%s']", label);
        return customUiMainMacroponent().createByXPath(ShadowRoot.class, xpathLocator);
    }

    protected ShadowRoot inputWithShadowDom(String label) {
        var xpathLocator = String.format("//now-input[@name='%s']", label);
        return customUiMainMacroponent().createByXPath(ShadowRoot.class, xpathLocator);
    }

    public ShadowRoot selectWithShadowDom(String label) {
        var xpathLocator = String.format("//now-select[@name='%s']", label);
        return customUiMainMacroponent().createByXPath(ShadowRoot.class, xpathLocator);
    }

    protected ShadowRoot textAreaWithShadowDom(String label) {
        var xpathLocator = String.format("//now-textarea[@name='%s']", label);
        return customUiMainMacroponent().createByXPath(ShadowRoot.class, xpathLocator);
    }

    protected ShadowRoot customUiDropDownSeismicHoist() {
        return create().byCss(ShadowRoot.class, "seismic-hoist").getShadowRoot();
    }

    public Button pickOptionByNumber(int optionNumber) {
        var xpathLocator = String.format("//div[@class='now-dropdown-list']//div[@id='%d' and @role='option']", optionNumber);
        return customUiDropDownSeismicHoist().createByXPath(Button.class, xpathLocator);
    }

    public Button pickMenuOptionById(String optionId) {
        var xpathLocator = String.format("//div[@role='menuitem' and @id='%s']", optionId);
        return customUiDropDownSeismicHoist().createByXPath(Button.class, xpathLocator);
    }

    public List<Button> menuOptionsText() {
        var xpathLocator = "//div[@role='menuitem' and @id]//div[text()]";
        return customUiDropDownSeismicHoist().createAllByXPath(Button.class, xpathLocator);
    }

    public List<Button> menuOptions() {
        var xpathLocator = "//div[@role='menuitem' and @id]";
        return customUiDropDownSeismicHoist().createAllByXPath(Button.class, xpathLocator);
    }

    public Button selectButton(String label) {
        var xpathLocator = "//button";
        return selectWithShadowDom(label).createByXPath(Button.class, xpathLocator);
    }

    public Button recordSaveButton() {
        var xpathLocator = "(//div[./now-record-common-record-presence]//button[@data-ariadescribedby='Save'])[1]";
        return customUiMainMacroponent().createByXPath(Button.class, xpathLocator);
    }

    public TextInput recordInputField(String inputName) {
        return recordInputWithShadowDom(inputName).createByXPath(TextInput.class, "//input");
    }

    public TextInput multiRecordInputField(String inputName) {
        return recordMultiInputWithShadowDom(inputName).createByXPath(TextInput.class, "//input");
    }

    public TextInput inputField(String inputName) {
        return inputWithShadowDom(inputName).createByXPath(TextInput.class, "//input");
    }

    public TextArea textAreaField(String textAreaName) {
        return textAreaWithShadowDom(textAreaName).createByXPath(TextArea.class, "//textarea");
    }
}