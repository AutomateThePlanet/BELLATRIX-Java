package solutions.bellatrix.servicenow.components.uiBuilder;

import java.util.List;
import org.openqa.selenium.ElementNotInteractableException;
import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;

public class RecordChoice extends UIBDefaultComponent implements ComponentDisabled, ComponentText {

    public WebComponent dropdown() {
        return this.createByXPath(WebComponent.class, ".//now-select");
    }

    public Button dropdownButton() {
        return dropdown().createByCss(Button.class, "button");
    }

    public List<Button> getDropdownOptions() {
        return getDropDownWrapper().createAllByCss(Button.class, "div[role='option']");
    }

    public Button getOptionByIndex(int index) {
        return getDropdownOptions().get(index);
    }

    public Button getOptionByText(String text) {
        var optionFound = getDropdownOptions().stream().filter(x -> x.getText().equals(text)).findFirst();
        if (optionFound.isEmpty()) {
            throw new RuntimeException("Option with text %s not found.".formatted(text));
        }
        return optionFound.get();
    }

    @Override
    public String getText() {
        return dropdownButton().getText();
    }

    @Override
    public boolean isDisabled() {
        return dropdownButton().isDisabled();
    }

    @Override
    public UibComponentType componentType() {
        return UibComponentType.CHOICE;
    }

    public void click() {
        dropdownButton().click();
    }

    public void selectByIndex(int index) {
        try {
            click();
            browserService.waitForAjax();
            getOptionByIndex(index).click();
        } catch (ElementNotInteractableException exception) {
            getOptionByIndex(index).scrollToVisible();
            getOptionByIndex(index).click();
        }
    }

    public void selectByText(String text) {
        try {
            click();
            browserService.waitForAjax();
            getOptionByText(text).click();
        } catch (ElementNotInteractableException exception) {
            getOptionByText(text).scrollToVisible();
            getOptionByText(text).click();
        }
    }

    public String getName() {
        return getAttribute("name");
    }

    @Override
    public void setText(String text) {
        selectByText(text);
    }

    public void assertSelectionIs(String expectedText) {
        validateTextIs(expectedText);
    }
}
