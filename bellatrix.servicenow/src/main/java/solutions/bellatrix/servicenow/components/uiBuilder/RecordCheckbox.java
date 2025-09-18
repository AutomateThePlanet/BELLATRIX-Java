package solutions.bellatrix.servicenow.components.uiBuilder;
import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;

public class RecordCheckbox extends UIBDefaultComponent implements ComponentDisabled {
    protected CheckBox checkbox() {
        return this.createByCss(CheckBox.class, "input[type='checkbox']");
    }

    public boolean isChecked() {
        return checkbox().isChecked();
    }

    @Override
    public void setText(String text) {
    }

    @Override
    public boolean isDisabled() {
        return getAttribute("readonly") != null || checkbox().isDisabled() || getAttribute("disabled") != null;
    }

    @Override
    public UibComponentType componentType() {
        return null;
    }

    public void check() {
        if (!isChecked() && !isDisabled()) {
            checkbox().check();
        }
    }

    public void uncheck() {
        if (isChecked() && !isDisabled()) {
            checkbox().uncheck();
        }
    }

    public String getName() {
        return getAttribute("name");
    }

    public void assertIsChecked() {
        checkbox().validateIsChecked();
    }

    public void assertIsUnchecked() {
        checkbox().validateIsUnchecked();
    }

    @Override
    public String getText() {
        return "";
    }
}