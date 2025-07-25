package solutions.bellatrix.servicenow.components.uiBuilder;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;

public class RecordCheckbox extends WebComponent implements ComponentDisabled {

    protected CheckBox checkbox() {
        return this.createByCss(CheckBox.class, "input[type='checkbox']");
    }

    public boolean isChecked() {
        return checkbox().isChecked();
    }

    @Override
    public boolean isDisabled() {
        return getAttribute("readonly") != null || checkbox().isDisabled() || getAttribute("disabled") != null;
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
}