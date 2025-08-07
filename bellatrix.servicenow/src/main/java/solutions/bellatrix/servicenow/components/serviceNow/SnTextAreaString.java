package solutions.bellatrix.servicenow.components.serviceNow;

import lombok.SneakyThrows;
import solutions.bellatrix.servicenow.components.enums.SnComponentType;

public class SnTextAreaString extends SnDefaultComponent {
    @Override
    protected String formControlXpathLocator() {
        return ".//textarea[contains(concat(' ',normalize-space(@class),' '),' form-control ')]";
    }

    @Override
    public SnComponentType componentType() {
        return SnComponentType.STRING;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
    }

    @SneakyThrows
    public void validateType() {
        super.validateType();
        formControl().validateIsVisible();
    }
}