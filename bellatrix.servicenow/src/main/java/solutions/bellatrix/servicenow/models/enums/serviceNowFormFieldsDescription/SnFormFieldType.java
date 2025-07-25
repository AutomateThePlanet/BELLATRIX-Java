package solutions.bellatrix.servicenow.models.enums.serviceNowFormFieldsDescription;

import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.contracts.ComponentReadonly;

public enum SnFormFieldType {
    INPUT("input", TextInput.class, "(//div[contains(@class,'form-group') and .//span[text()='%s']]//input[not(@type='hidden')])[1]"),
    TEXTAREA("textarea", TextArea.class, "//div[@data-type='label' and .//span[text()='%s']]/following-sibling::div/textarea"),
    SELECT("select", Select.class, "//div[@data-type='label' and .//span[text()='%s']]/following-sibling::div/select");

    private final String value;
    private final Class componentClass;
    private final String locator;

    SnFormFieldType(String label, Class componentClass, String locator) {
        this.value = label;
        this.componentClass = componentClass;
        this.locator = locator;
    }

    public String getValue() {
        return this.value;
    }

    public <C extends ComponentReadonly> Class<C> getComponent() {
        return this.componentClass;
    }

    public String getLocator(String label) {
        return String.format(this.locator, label);
    }
}