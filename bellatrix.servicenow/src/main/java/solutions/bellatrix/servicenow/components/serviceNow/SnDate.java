package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.Button;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SnDate extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.DATE;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return LocalDate.class;
    }

    public Button calendarButton() {
        var xpathLocator = ".//a";
        return this.createByXPath(Button.class, xpathLocator);
    }

    @Override
    public String getText() {
        return formControl().getValue();
    }

    public void setText(LocalDate localDate) {
        var dateTimeFormat = calendarButton().getAttribute("models-date_time_format");
        var dateAsString = DateTimeFormatter.ofPattern(dateTimeFormat).format(localDate);

        setText(dateAsString);
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        invokeValueSetEvent(text);
    }

    @Override
    public Boolean isDisabled() {
        var readonlyAttribute = formControl().getAttribute("readonly");
        Boolean isReadonly = false;

        if (readonlyAttribute != null) {
            isReadonly = Boolean.parseBoolean(readonlyAttribute);
        }

        return super.isDisabled() || isReadonly;
    }
}