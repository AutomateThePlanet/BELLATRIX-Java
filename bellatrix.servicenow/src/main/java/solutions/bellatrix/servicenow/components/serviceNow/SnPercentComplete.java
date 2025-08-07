package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;

public class SnPercentComplete extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.PERCENT_COMPLETE;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        invokeValueSetEvent(text);
    }
}