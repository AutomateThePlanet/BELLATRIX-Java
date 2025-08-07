package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;

public class SnFloat extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.FLOAT;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        super.invokeValueSetEvent(text);
    }
}