package solutions.bellatrix.servicenow.components.uiBuilder;

import org.openqa.selenium.ElementNotInteractableException;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.services.ComponentCreateService;

public class UiBuilderRecordInput extends WebComponent implements ComponentDisabled, ComponentText {
    protected WebComponent customUiRecordTypehead() {
        return this.shadowRootCreateByCss(WebComponent.class, "now-record-typeahead").getShadowRoot();
    }

    protected WebComponent customUiTypehead() {
        return customUiRecordTypehead().shadowRootCreateByCss(WebComponent.class, "now-typeahead").getShadowRoot();
    }

    protected Button recordInputDropDownByLabel() {
        return customUiTypehead().shadowRootCreateByCss(Button.class, "input");
    }

    protected TextInput recordTextInputDopDownByLabel() {
        return customUiTypehead().shadowRootCreateByCss(TextInput.class, "input");
    }

    protected WebComponent customUiDropDownSeismicHoist() {
        return create().byCss(WebComponent.class, "seismic-hoist").getShadowRoot();
    }

    protected Button recordDropDownOptionByLabelAndOptionNumber(int optionNumber) {
        return customUiDropDownSeismicHoist().shadowRootCreateByCss(Button.class, String.format("li[aria-posinset='%d']", optionNumber));
    }

    @Override
    public String getText() {
        return recordInputDropDownByLabel().getWrappedElement().getAttribute("value");
    }

    public void click() {
        recordInputDropDownByLabel().click();
    }

    public void setText(String text) {
        recordTextInputDopDownByLabel().setText(text);
    }

    public void pickOptionByNumber(int optionNumber) {
        try {
            browserService.waitForAjax();
            recordDropDownOptionByLabelAndOptionNumber(optionNumber).click();
        } catch (ElementNotInteractableException exception) {
            recordDropDownOptionByLabelAndOptionNumber(optionNumber).scrollToVisible();
            browserService.waitForAjax();
            recordDropDownOptionByLabelAndOptionNumber(optionNumber).click();
        }
    }

    public void selectOptionByText(String optionText) {
        recordInputDropDownByLabel().click();
        setText(optionText);
        pickOptionByNumber(1);
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    protected ComponentCreateService create() {
        return new ComponentCreateService();
    }

    public void assertTextIs(String text) {
        recordTextInputDopDownByLabel().validateTextIs(text);
    }
}