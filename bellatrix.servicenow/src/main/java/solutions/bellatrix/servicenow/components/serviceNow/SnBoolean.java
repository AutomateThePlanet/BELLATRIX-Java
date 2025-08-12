package solutions.bellatrix.servicenow.components.serviceNow;


import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.CheckBox;
import solutions.bellatrix.web.components.ComponentActionEventArgs;

public class SnBoolean extends SnDefaultComponent {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    @Override
    public SnComponentType componentType() {
        return SnComponentType.BOOLEAN;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return Boolean.class;
    }

    @Override
    protected String formControlXpathLocator() {
        return ".//input[@type='checkbox']";
    }

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }

    protected CheckBox inputWithActualCheckboxState() {
        var xpathLocator = ".//div[contains(@class,'input_controls')]/input[@type='hidden']";
        return this.createByXPath(CheckBox.class, xpathLocator);
    }

    protected Button labelToClick() {
        var xpathLocator = ".//label[contains(concat(' ',normalize-space(@class),' '),' checkbox-label ')]";
        return this.createByXPath(Button.class, xpathLocator);
    }

    @Override
    public String getText() {
        return inputWithActualCheckboxState().getAttribute("value");
    }

    public void setText(Boolean checkboxValue) {
        this.setText(checkboxValue.toString());
    }

    @Override
    public void setText(String text) {
        if (!getText().equals(text)) {
            labelToClick().click();
            invokeValueSetEvent(text);
        }
    }
}