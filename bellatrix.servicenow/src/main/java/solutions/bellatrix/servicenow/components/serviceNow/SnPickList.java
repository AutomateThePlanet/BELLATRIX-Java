package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;

public class SnPickList extends SnString {
    protected Class<?> getSetTextParamClass() {
        return String.class;
    }

    @Override
    public SnComponentType componentType() {
        return SnComponentType.PICK_LIST;
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        invokeValueSetEvent(text);
    }
}