package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.*;

import java.time.Duration;

public class SnDuration extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.DURATION;
    }

    @Override
    protected Class<?> getSetTextParamClass() {
        return Duration.class;
    }

    @Override
    public String getText() {
        long days = Long.parseLong(formControl().getText());
        var duration = Duration.ofDays(days);

        return duration.toString();
    }

    public void setText(Duration expected) {
        this.setText(String.valueOf(expected.toDays()));
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        super.invokeValueSetEvent(text);
    }

    public TextInput daysInput() {
        return formControl();
    }

    public TextInput hoursInput() {
        var xpathLocator = ".//input[@title='Hours']";
        return this.createByXPath(TextInput.class, xpathLocator);
    }

    public TextInput minutesInput() {
        var xpathLocator = ".//input[@title='Minutes']";
        return this.createByXPath(TextInput.class, xpathLocator);
    }

    public TextInput secondsInput() {
        var xpathLocator = ".//input[@title='Seconds']";
        return this.createByXPath(TextInput.class, xpathLocator);
    }

    public Boolean isDisabled() {
        if (getFormControlsCount() == 4) {
            return daysInput().isDisabled() && hoursInput().isDisabled() && minutesInput().isDisabled() && secondsInput().isDisabled();
        }

        return formControl().getHtmlClass().contains("disabled");
    }

    private int getFormControlsCount() {
        var cssLocator = "input.form-control";
        return this.createAllByCss(TextInput.class, cssLocator).size();
    }
}