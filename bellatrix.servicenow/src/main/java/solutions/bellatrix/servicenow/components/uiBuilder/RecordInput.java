package solutions.bellatrix.servicenow.components.uiBuilder;

import org.openqa.selenium.ElementNotInteractableException;
import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;

public class RecordInput extends UIBDefaultComponent implements ComponentDisabled, ComponentText {

    protected TextInput textInput() {
        return create().byCss(TextInput.class, "input");
    }

    @Override
    public String getText() {
        return textInput().getText();
    }

    @Override
    public boolean isDisabled() {
        return getAttribute("readonly") != null || textInput().isDisabled();
    }

    @Override
    public UibComponentType componentType() {
        return UibComponentType.STRING;
    }

    public void setText(String text) {
        if (!isDisabled()) {
            textInput().setText(text);
        } else {
            throw new ElementNotInteractableException("Cannot set text on readonly input");
        }
    }

    public void clear() {
        if (!isDisabled()) {
            textInput().setText("");
        } else {
            throw new ElementNotInteractableException("Cannot clear readonly input");
        }
    }

    public String getName() {
        return getAttribute("name");
    }

    public boolean isRequired() {
        return getAttribute("required") != null;
    }

    public void assertTextIs(String expectedText) {
        validateTextIs(expectedText);
    }
}
