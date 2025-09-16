package solutions.bellatrix.servicenow.components.uiBuilder;

import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;

public class UibToolbarButton extends WebComponent implements ComponentDisabled {
    protected UibToolbarButton uibToolbarButton () { return this.createByCss(UibToolbarButton.class, ".sn-canvas-toolbar-button-container button");};

    @Override
    public boolean isDisabled() {
        return getAttribute("readonly") != null || uibToolbarButton().isDisabled() || getAttribute("disabled") != null;
    }

    public String getLabel () {
        return uibToolbarButton().getAttribute("value");
    }

    public UibToolbarButton uibToolbarButtonByLabel (String label) {
        return uibToolbarButton().createByCss(UibToolbarButton.class, "[value='%s']".formatted(label));
    }
}
