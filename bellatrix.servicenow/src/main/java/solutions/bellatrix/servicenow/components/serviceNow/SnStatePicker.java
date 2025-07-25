package solutions.bellatrix.servicenow.components.serviceNow;

import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SnStatePicker extends SnComponent {
    @Override
    public String getText() {
        return getSelectedButtons().getText();
    }

    public void setText(String text) {
        getButtonByText(text).click();
    }

    private List<String> getStates() {
        return getButtons().stream().map(Button::getText).toList();
    }

    private List<Button> getButtons() {
        var xpathLocator = ".//button[text()][contains(@class, 'ui-btn')]";
        return this.createAllByXPath(Button.class, xpathLocator);
    }

    private Button getButtonByText(String text) {
        var xpathLocator = String.format(".//button[text()='%s']", text);
        return this.createByXPath(Button.class, xpathLocator);
    }

    private Button getSelectedButtons() {
        var xpathLocator = ".//button[text() and contains(@class,'btn-info')]";
        return this.createByXPath(Button.class, xpathLocator);
    }

    @SneakyThrows
    public void validateButtonsText(String... buttonText) {
        validateButtonsText(Arrays.stream(buttonText).toList());
    }

    @SneakyThrows
    public void validateButtonsText(List<String> buttonText) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateCollectionIs", WebComponent.class, Supplier.class, List.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (Supplier<List<String>>) this::getStates, buttonText, String.format("buttons text  %s should be part", buttonText));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}