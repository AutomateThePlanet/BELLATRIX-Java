package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;
import solutions.bellatrix.servicenow.snSetupData.FxCurrency2Instance;

public class SnCurrency2 extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.CURRENCY;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return FxCurrency2Instance.class;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    public void setText(FxCurrency2Instance text) {
        this.setText(String.valueOf(text.getAmount()));
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        invokeValueSetEvent(text);
    }
}