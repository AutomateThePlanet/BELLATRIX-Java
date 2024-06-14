package solutions.bellatrix.web.components.datahandlers;

import lombok.experimental.UtilityClass;
import solutions.bellatrix.core.utilities.parsing.TypeParser;
import solutions.bellatrix.web.components.*;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@UtilityClass
@SuppressWarnings({"rawtypes", "unchecked"})
public class ControlDataHandler {
    // TODO: addReadOnlyControlDataHandler(parameters)
    // TODO: addEditableControlDataHandler(parameters)

    public <T> Object getData(T component) {
        var dataHandler = READONLY_CONTROL_DATA_HANDLERS.get(component.getClass());
        if (dataHandler == null) {
            throw new IllegalArgumentException(String.format("Cannot find proper readonly control data handler for type: %s. Make sure it is registered!", component.getClass()));
        }

        return dataHandler.apply(component);
    }

    public <T> void setData(T component, Object data) {
        var dataHandler = EDITABLE_CONTROL_DATA_HANDLERS.get(component.getClass());
        if (dataHandler == null) {
            throw new IllegalArgumentException(String.format("Cannot find proper edit control data handler for type: %s. Make sure it is registered!", component.getClass()));
        }

        dataHandler.accept(component, data);
    }

    private static final Map<Class<? extends WebComponent>, Function> READONLY_CONTROL_DATA_HANDLERS = Map.ofEntries(
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

    private static final Map<Class<? extends WebComponent>, BiConsumer> EDITABLE_CONTROL_DATA_HANDLERS = Map.ofEntries(
            Map.entry(CheckBox.class, (BiConsumer<CheckBox, String>)(checkBox, data) -> {
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
            })
            // TODO
    );
}
