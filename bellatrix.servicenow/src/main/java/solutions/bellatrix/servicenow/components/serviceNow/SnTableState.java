package solutions.bellatrix.servicenow.components.serviceNow;

import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SnTableState extends SnComponent {
    @Override
    public String getText() {
        return getActive().getText();
    }

    public void validateActiveTabStateTextIs(String state) {
        getActive().validateTextIs(state);
    }

    public void setText(String text) {
        getAnchorByText(text).click();
    }

    private List<String> getStates() {
        return getAnchors().stream().map(Anchor::getText).toList();
    }

    private List<Anchor> getAnchors() {
        var xpathLocator = ".//a[text()]";
        return this.createAllByXPath(Anchor.class, xpathLocator);
    }

    private Anchor getAnchorByText(String text) {
        var xpathLocator = String.format(".//a[text()='%s']", text);
        return this.createByXPath(Anchor.class, xpathLocator);
    }

    private Anchor getActive() {
        var xpathLocator = ".//li[@class='active']//a[text()]";
        return this.createByXPath(Anchor.class, xpathLocator);
    }

    @SneakyThrows
    public void validateStates(String... expectedStates) {
        validateStates(Arrays.stream(expectedStates).toList());
    }

    @SneakyThrows
    public void validateStates(List<String> expectedStates) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateCollectionIs", WebComponent.class, Supplier.class, List.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (Supplier<List<String>>) this::getStates, expectedStates, String.format("buttons text  %s should be part", expectedStates));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    protected String getActiveAnchorText() {
        return getActive().getText();
    }

    @SneakyThrows
    void validateActiveState(String tabLabel) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeIs", WebComponent.class, Supplier.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (Supplier<String>) this::getActiveAnchorText, tabLabel, String.format("element %s should be active but it is NOT", tabLabel));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}