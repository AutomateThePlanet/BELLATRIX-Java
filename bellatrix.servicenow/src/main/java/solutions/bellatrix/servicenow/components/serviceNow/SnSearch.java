package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;

public class SnSearch extends  SnDefaultComponent{
    @Override
    public SnComponentType componentType() {
        return SnComponentType.SEARCH;
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
