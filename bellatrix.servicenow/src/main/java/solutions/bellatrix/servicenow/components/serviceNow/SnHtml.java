package solutions.bellatrix.servicenow.components.serviceNow;


import solutions.bellatrix.servicenow.components.enums.SnComponentType;

public class SnHtml extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.HTML;
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