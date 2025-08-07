package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.Button;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SnGlideTime extends SnDefaultComponent{
    @Override
    public SnComponentType componentType() {
        return SnComponentType.GLIDE_TIME;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return LocalTime.class;
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
        var dateTimeFormat = calendarButton().getAttribute("models-date_time_format");
        var dateAsString = DateTimeFormatter.ofPattern(dateTimeFormat).format(localDateTime);

        setText(dateAsString);
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        super.invokeValueSetEvent(text);
    }
}
