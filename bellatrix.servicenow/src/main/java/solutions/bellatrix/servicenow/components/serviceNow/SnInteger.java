package solutions.bellatrix.servicenow.components.serviceNow;


import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;

public class SnInteger extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.INTEGER;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return Integer.class;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    public void setText(Integer text) {
        this.setText(text.toString());
        super.invokeValueSetEvent(text.toString());
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
    }
}