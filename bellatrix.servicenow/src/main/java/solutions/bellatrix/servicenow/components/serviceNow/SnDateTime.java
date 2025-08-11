package solutions.bellatrix.servicenow.components.serviceNow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.Button;

public class SnDateTime extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.DATE_TIME;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return LocalDateTime.class;
    }

    public Button calendarButton() {
        var xpathLocator = ".//following-sibling::span//button";
        return this.createByXPath(Button.class, xpathLocator);
    }

    @Override
    public String getText() {
        return formControl().getValue();
    }

    public void setText(LocalDateTime localDateTime) {
        var dateTimeFormat = calendarButton().getAttribute("data-date_time_format");
        var dateAsString = DateTimeFormatter.ofPattern(dateTimeFormat).format(localDateTime);

        setText(dateAsString);
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        invokeValueSetEvent(text);
    }
}