package solutions.bellatrix.servicenow.components.uiBuilder;

import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.servicenow.components.serviceNow.SnComponent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.services.ComponentCreateService;
import solutions.bellatrix.web.validations.ComponentValidator;

@SuppressWarnings("unused")
public abstract class UIBDefaultComponent extends SnComponent implements ComponentText {
    public final static EventListener<ComponentActionEventArgs> SETTING_VALUE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALUE_SET = new EventListener<>();

    protected String formControlXpathLocator() {
        return ".//*[contains(concat(' ',normalize-space(@class),' '),' form-control ')]";
    }

    protected ShadowRoot getDropDownWrapper() {
        return create().byCss(ShadowRoot.class, "seismic-hoist");
    }

    protected boolean isDropDownOpen() {
        return create().allByCss(ShadowRoot.class, "seismic-hoist").stream().findFirst().isPresent();
    }

    protected void invokeValueSetEvent(String text) {
        VALUE_SET.broadcast(new ComponentActionEventArgs(this, text));
    }

    protected Class<?> getSetTextParamClass() {
        return String.class;
    }

    public abstract void setText(String text);

    public boolean isRequired() {
        return getAttribute("required") != null;
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
        // TODO: adjust locator
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

    public boolean isDisabled() {
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
        var xpathLocator = ".//div[@models-type='label']";
        return this.createByXPath(Span.class, xpathLocator).getWrappedElement().getAttribute("type");
    }

    public abstract UibComponentType componentType();

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

    protected ComponentCreateService create() {
        return new ComponentCreateService();
    }
}