package solutions.bellatrix.servicenow.components.uiBuilder;

import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;

public class UiBuilderMultiRecordInput extends UiBuilderRecordInput {
    protected WebComponent customUiRecordTypeheadMultiple() {
        return this.shadowRootCreateByCss(WebComponent.class, "now-record-typeahead-multiple").getShadowRoot();
    }

    protected WebComponent customUiTypeheadMulti() {
        return customUiRecordTypeheadMultiple().shadowRootCreateByCss(WebComponent.class, "now-typeahead-multi").getShadowRoot();
    }

    @Override
    protected Button recordInputDropDownByLabel() {
        return customUiTypeheadMulti().shadowRootCreateByCss(Button.class, "input");
    }

    @Override
    protected TextInput recordTextInputDopDownByLabel() {
        return customUiTypeheadMulti().shadowRootCreateByCss(TextInput.class, "input");
    }

    protected Button chosenOptionCloseButtonByNumber(int optionNumber) {
        var cssLocator = String.format(".now-typeahead-pill-dismiss:nth-of-type(%d)", optionNumber);
        return customUiTypeheadMulti().shadowRootCreateByCss(Button.class, cssLocator);
    }

    public void closeChosenOptionByNumber(int optionNumber) {
        chosenOptionCloseButtonByNumber(optionNumber).click();
    }
}