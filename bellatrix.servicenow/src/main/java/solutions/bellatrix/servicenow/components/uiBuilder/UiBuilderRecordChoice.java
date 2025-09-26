package solutions.bellatrix.servicenow.components.uiBuilder;

import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

import java.util.List;

public class UiBuilderRecordChoice extends UIBDefaultComponent implements ComponentDisabled, ComponentText {
    protected WebComponent customUiDropdown() {
        return this.shadowRootCreateByCss(WebComponent.class, "sn-record-choice").getShadowRoot();
    }

    public ShadowRoot dropdown() {
        return this.createByXPath(ShadowRoot.class, ".//now-select").getShadowRoot();
    }

    public Button dropdownButton() {
        return dropdown().createByCss(Button.class, "button");
    }

    public List<Button> getDropdownOptions() {
        return getDropDownWrapper().createAllByXPath(Button.class, "./descendant::div[@role='option']");
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
    protected ShadowRoot getDropDownWrapper() {
        return create().byXPath(ShadowRoot.class, "//now-popover-panel/descendant::seismic-hoist").getShadowRoot();
    }

    @Override
    public void setText(String text) {

    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public UibComponentType componentType() {
        return null;
    }

    @Override
    public String getText() {
        return this.getWrappedElement().getAttribute("value");
    }
}
