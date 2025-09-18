package solutions.bellatrix.servicenow.components.uiBuilder;

import org.openqa.selenium.ElementNotInteractableException;
import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.services.ComponentCreateService;

public class RecordReference extends UIBDefaultComponent implements ComponentDisabled, ComponentText {

    protected WebComponent recordTypehead() {
        return this.create().byCss(WebComponent.class, "now-record-typeahead").getShadowRoot();
    }

    protected Button referenceButton() {
        return this.createByCss(Button.class, "button");
    }

    protected TextInput referenceTextInput() {
        return this.createByCss(TextInput.class, "input");
    }

    protected WebComponent dropdownList() {
        return this.createByCss(WebComponent.class, "now-typeahead");
    }

    protected Button getOptionByIndex(int index) {
        return dropdownList().createByCss(Button.class, String.format("li[aria-posinset='%d']", index));
    }

    @Override
    public String getText() {
        return referenceTextInput().getValue();
    }

    @Override
    public UibComponentType componentType() {
        return UibComponentType.REFERENCE;
    }

    public void click() {
        referenceButton().click();
    }

    public void setText(String text) {
        referenceTextInput().setText(text);
    }

    public void pickOptionByIndex(int index) {
        try {
            browserService.waitForAjax();
            getOptionByIndex(index).click();
        } catch (ElementNotInteractableException exception) {
            getOptionByIndex(index).scrollToVisible();
            browserService.waitForAjax();
            getOptionByIndex(index).click();
        }
    }

    public void selectByText(String text) {
        click();
        setText(text);
        pickOptionByIndex(1);
    }

    public String getValue() {
        return getAttribute("value");
    }

    public String getName() {
        return getAttribute("name");
    }

    public void assertTextIs(String expectedText) {
        referenceTextInput().validateTextIs(expectedText);
    }

    protected ComponentCreateService create() {
        return new ComponentCreateService();
    }
}
