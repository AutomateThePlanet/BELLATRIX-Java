package solutions.bellatrix.servicenow.components.serviceNow;

import lombok.SneakyThrows;
import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.Option;
import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SnChoice extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.CHOICE;
    }

    public Select selectElement() {
        var xpathLocator = ".//select";
        return this.createByXPath(Select.class, xpathLocator);
    }

    public Option getSelectedOption() {
        var xpathLocator = "./option";
        var options = selectElement().createAllByXPath(Option.class, xpathLocator);

        return options.stream().filter(x -> x.getWrappedElement().getDomProperty("selected").equals("true")).findAny().get();
    }

    @Override
    public String getText() {
        return getSelectedOption().getText();
    }

    @Override
    public void setText(String text) {
        selectElement().selectByText(text);
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

    private List<String> getAllOptions() {
        return selectElement().getAllOptions().stream().map(Option::getText).toList();
    }

    public List<Option> getOptions(){
        return selectElement().getAllOptions();
    }

    @SneakyThrows
    public void validateOptions(String... optionsText) {
        validateOptions(Arrays.stream(optionsText).toList());
    }

    @SneakyThrows
    public void validateOptions(List<String> optionsText) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateCollectionIs", WebComponent.class, Supplier.class, List.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), this, (Supplier<List<String>>) this::getAllOptions, optionsText, String.format("options %s should be part", optionsText));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}