package solutions.bellatrix.servicenow.components.serviceNow;

import lombok.SneakyThrows;
import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class SnDefaultComponent extends SnComponent implements ComponentText {
    public final static EventListener<ComponentActionEventArgs> SETTING_VALUE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALUE_SET = new EventListener<>();

    protected String formControlXpathLocator() {
        return ".//*[contains(concat(' ',normalize-space(@class),' '),' form-control ')]";
    }

    protected void invokeValueSetEvent(String text) {
        VALUE_SET.broadcast(new ComponentActionEventArgs(this, text));
    }

    // This method is used to get the type of the parameter of the setText method.
    // This is needed when we want to dynamically invoke setTextMethod();
    //If you use it with formModel. FormModel field type should equals this type in order dynamic logic to work properly.

    protected Class<?> getSetTextParamClass() {
        return String.class;
    }

    public abstract void setText(String text);

    public Boolean isRequired() {
        var xpathLocator = ".//label//span[contains(concat(' ',normalize-space(@class),' '),' required-marker ')]";
        var requiredMarkers = this.createAllByXPath(Span.class, xpathLocator);

        return requiredMarkers.size() == 1;
    }

    @SneakyThrows
    public void validateIsRequired(boolean isRequired) {
        try {
            Method method;
            if (isRequired) {
                method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", WebComponent.class, BooleanSupplier.class, String.class);
            } else {
                method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeFalse", WebComponent.class, BooleanSupplier.class, String.class);
            }

            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (BooleanSupplier) this::isRequired, "required");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public String getLabel() {
        var xpathLocator = ".//label//span[contains(concat(' ',normalize-space(@class),' '),' label-text ')]";
        return this.createByXPath(Span.class, xpathLocator).getText();
    }

    @SneakyThrows
    public void validateLabel(String label) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeIs", WebComponent.class, Supplier.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (Supplier<String>) this::getLabel, label, String.format("label \u001B[35m%s\u001B[0m", label));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public Boolean isDisabled() {
        boolean isDisabled = checkFormControlContainsClassDisabled();

        if (checkFormControlContainsAttributeReadonly()) {
            isDisabled = true;
        }

        return isDisabled;
    }

    public Boolean checkFormControlContainsClassDisabled() {
        return formControl().getHtmlClass().contains("disabled");
    }

    public Boolean checkFormControlContainsAttributeReadonly() {
        var readonlyAttribute = formControl().getAttribute("readonly");

        return readonlyAttribute != null;
    }

    @SneakyThrows
    public void validateIsDisabled(boolean isDisabled) {
        try {
            Method method;
            if (isDisabled) {
                method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", WebComponent.class, BooleanSupplier.class, String.class);
            } else {
                method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeFalse", WebComponent.class, BooleanSupplier.class, String.class);
            }

            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (BooleanSupplier) this::isDisabled, "disabled");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public String getType() {
        var xpathLocator = ".//div[@data-type='label']";
        return this.createByXPath(Span.class, xpathLocator).getWrappedElement().getAttribute("type");
    }

    public abstract SnComponentType componentType();

    @SneakyThrows
    public void validateType() {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeIs", WebComponent.class, Supplier.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (Supplier<String>) this::getType, componentType().toString(), String.format("type \u001B[35m%s\u001B[0m", componentType()));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public TextInput formControl() {
        return this.createByXPath(TextInput.class, formControlXpathLocator());
    }
}