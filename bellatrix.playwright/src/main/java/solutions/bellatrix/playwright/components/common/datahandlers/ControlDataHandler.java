package solutions.bellatrix.playwright.components.common.datahandlers;

import lombok.experimental.UtilityClass;
import solutions.bellatrix.core.utilities.parsing.TypeParser;
import solutions.bellatrix.playwright.components.*;
import solutions.bellatrix.playwright.components.WebComponent;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@UtilityClass
@SuppressWarnings({"rawtypes", "unchecked"})
public class ControlDataHandler {
    public static <T extends WebComponent> void addReadOnlyControlDataHandler(Class<T> clazz, Function<T, ?> handler) {
        READONLY_CONTROL_DATA_HANDLERS.put(clazz, handler);
    }

    public static <T extends WebComponent> void addEditableControlDataHandler(Class<T> clazz, BiConsumer<T, ?> handler) {
        EDITABLE_CONTROL_DATA_HANDLERS.put(clazz, handler);
    }

    public static <T extends WebComponent> Object getData(T component) {
        var dataHandler = READONLY_CONTROL_DATA_HANDLERS.get(component.getClass());
        if (dataHandler == null) {
            throw new IllegalArgumentException(String.format("Cannot find proper readonly control data handler for type: %s. Make sure it is registered!", component.getClass()));
        }

        return dataHandler.apply(component);
    }

    public static <T extends WebComponent> void setData(T component, Object data) {
        var dataHandler = EDITABLE_CONTROL_DATA_HANDLERS.get(component.getClass());
        if (dataHandler == null) {
            throw new IllegalArgumentException(String.format("Cannot find proper edit control data handler for type: %s. Make sure it is registered!", component.getClass()));
        }

        dataHandler.accept(component, data);
    }

    private static Map<Class<? extends WebComponent>, Function> READONLY_CONTROL_DATA_HANDLERS = Map.ofEntries(
            Map.entry(Anchor.class, (Function<Anchor, String>)(anchor) -> anchor.getText().trim()),
            Map.entry(Button.class, (Function<Button, String>)(button) -> (!button.getText().trim().isBlank()) ? button.getText().trim() : button.getValue()),
            Map.entry(CheckBox.class, (Function<CheckBox, Boolean>)CheckBox::isChecked),
            Map.entry(ColorInput.class, (Function<ColorInput, String>)ColorInput::getColor),
            Map.entry(DateInput.class, (Function<DateInput, String>)DateInput::getDate),
            Map.entry(DateTimeInput.class, (Function<DateTimeInput, String>)DateTimeInput::getTime),
            Map.entry(Div.class, (Function<Div, String>)(div) -> div.getText().trim()),
            Map.entry(EmailInput.class, (Function<EmailInput, String>)EmailInput::getEmail),
            Map.entry(Heading.class, (Function<Heading, String>)Heading::getText),
            Map.entry(Image.class, (Function<Image, String>)Image::getSrc),
            Map.entry(Label.class, (Function<Label, String>)(label) -> label.getText().trim()),
            Map.entry(MonthInput.class, (Function<MonthInput, String>)MonthInput::getMonth),
            Map.entry(NumberInput.class, (Function<NumberInput, Double>)NumberInput::getNumber),
            Map.entry(Output.class, (Function<Output, String>)(output) -> output.getText().trim()),
            Map.entry(PasswordInput.class, (Function<PasswordInput, String>)PasswordInput::getPassword),
            Map.entry(PhoneInput.class, (Function<PhoneInput, String>)PhoneInput::getPhone),
            Map.entry(RadioButton.class, (Function<RadioButton, Boolean>)RadioButton::isChecked),
            Map.entry(Range.class, (Function<Range, Double>)Range::getRange),
            Map.entry(Reset.class, (Function<Reset, String>)(reset) -> reset.getText().trim()),
            Map.entry(SearchField.class, (Function<SearchField, String>)SearchField::getSearch),
            Map.entry(Select.class, (Function<Select, String>)(select) -> select.getSelected().getText().trim()),
            Map.entry(Span.class, (Function<Span, String>)(span) -> span.getText().trim()),
            Map.entry(TextArea.class, (Function<TextArea, String>)(textArea) -> textArea.getText().trim()),
            Map.entry(TextInput.class, (Function<TextInput, String>)(textInput) -> textInput.getValue().trim()),
            Map.entry(TimeInput.class, (Function<TimeInput, String>)TimeInput::getTime),
            Map.entry(UrlField.class, (Function<UrlField, String>)UrlField::getUrl),
            Map.entry(WeekInput.class, (Function<WeekInput, String>)WeekInput::getWeek)
    );

    private static Map<Class<? extends WebComponent>, BiConsumer> EDITABLE_CONTROL_DATA_HANDLERS = Map.ofEntries(
            Map.entry(CheckBox.class, (BiConsumer<CheckBox, Object>)(checkBox, data) -> {
                try {
                    boolean valueToSet = TypeParser.parse(data, Boolean.class);
                    if (valueToSet) {
                        if (!checkBox.isChecked()) {
                            checkBox.check();
                        }
                    } else {
                        if (checkBox.isChecked()) {
                            checkBox.uncheck();
                        }
                    }
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException(String.format("The input string '%s' was not recognized as valid boolean.", data));
                }
            }),
            Map.entry(DateInput.class, (BiConsumer<DateInput, String>)(dateInput, data) -> {
                var valueToSet = TypeParser.parse(data, LocalDateTime.class);
                dateInput.setDate(valueToSet.getYear(), valueToSet.getMonthValue(), valueToSet.getDayOfMonth());
            }),
            Map.entry(DateTimeInput.class, (BiConsumer<DateTimeInput, String>)(dateTimeInput, data) -> {
                var valueToSet = TypeParser.parse(data, LocalDateTime.class);
                dateTimeInput.setTime(valueToSet);
            }),
            Map.entry(EmailInput.class, (BiConsumer<EmailInput, String>)EmailInput::setEmail),
            Map.entry(MonthInput.class, (BiConsumer<MonthInput, String>)(monthInput, data) -> {
                var valueToSet = TypeParser.parse(data, LocalDateTime.class);
                monthInput.setMonth(valueToSet.getYear(), valueToSet.getMonthValue());
            }),
            Map.entry(PasswordInput.class, (BiConsumer<PasswordInput, String>)PasswordInput::setPassword),
            Map.entry(PhoneInput.class, (BiConsumer<PhoneInput, String>)PhoneInput::setPhone),
            Map.entry(RadioButton.class, (BiConsumer<RadioButton, Object>)(radioButton, data) -> {
                var valueToSet = TypeParser.parse(data, Boolean.class);
                if (valueToSet) {
                    if (!radioButton.isChecked()) radioButton.click();
                } else {
                    if (radioButton.isChecked()) radioButton.click();
                }
            }),
            Map.entry(Range.class, (BiConsumer<Range, Object>)(range, data) -> {
                var valueToSet = TypeParser.parse(data, Integer.class);
                range.setRange(valueToSet);
            }),
            Map.entry(SearchField.class, (BiConsumer<SearchField, String>)SearchField::setSearch),
            Map.entry(Select.class, (BiConsumer<Select, Object>)(select, data) -> {
                if (data.getClass().equals(String.class)) select.selectByText((String)data);
                else if (data.getClass().equals(Integer.class)) select.selectByIndex((Integer)data);
            }),
            Map.entry(TextArea.class, (BiConsumer<TextArea, String>)TextArea::setText),
            Map.entry(TextInput.class, (BiConsumer<TextInput, String>)TextInput::setText),
            Map.entry(TimeInput.class, (BiConsumer<TimeInput, String>)(timeInput, data) -> {
                var valueToSet = TypeParser.parse(data, LocalDateTime.class);
                timeInput.setTime(valueToSet.getHour(), valueToSet.getMinute());
            }),
            Map.entry(UrlField.class, (BiConsumer<UrlField, String>)UrlField::setUrl),
            Map.entry(WeekInput.class, (BiConsumer<WeekInput, String>)(weekInput, data) -> {
                var valueToSet = TypeParser.parse(data, LocalDateTime.class);
                weekInput.setWeek(valueToSet.getYear(), valueToSet.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
            })
    );
}
