package solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.interactions.Actions;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.servicenow.components.models.ServiceNowForm;
import solutions.bellatrix.servicenow.components.uiBuilder.RecordCheckbox;
import solutions.bellatrix.servicenow.components.uiBuilder.UIBDefaultComponent;
import solutions.bellatrix.servicenow.components.uiBuilder.UiBuilderRecordChoice;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.FieldLabel;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.FieldLocator;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.UibComponent;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;
import solutions.bellatrix.servicenow.pages.uib.sections.mainContentSection.MainContentSection;
import solutions.bellatrix.servicenow.pages.uib.sections.menuHeader.MenuHeaderSection;
import solutions.bellatrix.servicenow.pages.uib.sections.tabsSection.TabsSection;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.WebPage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BaseUIBPage<MapT extends Map, AssertsT extends Asserts<MapT>> extends WebPage<MapT, AssertsT> {
    public LeftSidebarSection sidebar = new LeftSidebarSection(this);
    public MainContentSection mainContent = new MainContentSection(this);
    public MenuHeaderSection menuHeader = new MenuHeaderSection(this);
    public TabsSection tabs = new TabsSection(this);

    public ShadowRoot base() {
        return create().byXPath(ShadowRoot.class, "//*[contains(name(),'macroponent')]").getShadowRoot();
    }

    public void assertNavigated() {
        app().browser().waitForAjax();
        app().browser().waitForPartialUrl(getUrl());
        app().browser().waitForAjax();
    }

    private static <SnClass extends UIBDefaultComponent> Method getGetTextMethod(Class<SnClass> snClass) {
        List<Method> methodContainingClass = new ArrayList<>();
        Class<?> currentClass = snClass;
        while (methodContainingClass.isEmpty()) {
            methodContainingClass.addAll(Arrays.stream(currentClass.getDeclaredMethods()).filter(x -> {
                var methodName = x.getName();
                x.setAccessible(true);
                return x.getName().equals("getText");
            }).toList());
        }

        try {
            return snClass.getDeclaredMethod("getText");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Component does not support getText().", e);
        }
    }

    protected <TFormModel extends ServiceNowForm> TFormModel readForm(WebComponent container, Class<TFormModel> modelClass) {
        TFormModel model = InstanceFactory.create(modelClass);

        Field[] fields = modelClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(UibComponent.class) && field.isAnnotationPresent(FieldLocator.class)) {
                var componentClass = field.getDeclaredAnnotation(UibComponent.class).value();
                var elementLabel = field.getDeclaredAnnotation(FieldLabel.class).value();
                try {
                    var foundComponentWrappers = container.createAllByXPath(componentClass, String.format("./descendant::*[(contains(name(),'sn-record-') or contains(name(),'now-record-')) and @context='form' and descendant::*[text()='%s']]", elementLabel)).stream().findAny();

                    if (foundComponentWrappers.isPresent()) {
                        var component = foundComponentWrappers.get();
                        String actualValue;
                        if (componentClass.getName().contains("Reference")) {
                            actualValue = component.getValue();
                        } else if (componentClass.getName().contains("Checkbox")) {
                            actualValue = component.createByXPath(RecordCheckbox.class, "./descendant::now-checkbox").getValue();
                        } else {
                            actualValue = component.getShadowRoot().createByCss(TextInput.class,"input" ).getValue();
                        }
                        field.set(model, actualValue);
                    }
                    else {
                        Log.info("Form Field with label: '%s' Not found.".formatted(elementLabel));
                    }
                }
                catch (IllegalArgumentException | NotFoundException iae) {
                    Log.info("Form Field with label: '%s' Not found".formatted(elementLabel));
                }
                catch (Exception e) {
                    throw new RuntimeException("Failed to read field value for label: %s".formatted(elementLabel), e);
                }
            }
        }

        return model;
    }

    protected <TFormModel extends ServiceNowForm> void assertForm(TFormModel expected, WebComponent container, Class<TFormModel> formClass) {
        var actual = readForm(container, formClass);
        var fields = expected.getNotNullFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                var expectedValue = field.get(expected);
                var actualValue = field.get(actual);
                Assertions.assertEquals(expectedValue, actualValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to compare field values", e);
            }
        }
    }

    protected  <FormClass extends ServiceNowForm> void fillForm(FormClass model, WebComponent container) {
        var fields = model.getNotNullFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getAnnotations()[0].toString().contains("Disabled()")) {
                continue;
            }

            if (field.isAnnotationPresent(UibComponent.class) && field.isAnnotationPresent(FieldLabel.class) && field.isAnnotationPresent(FieldLocator.class)) {
                var componentClass = field.getDeclaredAnnotation(UibComponent.class).value();
                var elementLabel = field.getDeclaredAnnotation(FieldLabel.class).value();
                var elementLocator = field.getDeclaredAnnotation(FieldLocator.class).value();
                try {
                    var foundComponentWrappers = container.createAllByXPath(componentClass, String.format(".//*[contains(name(),'sn-record-') and descendant::*[@name='%s']]", elementLocator)).stream().findAny();

                    if (foundComponentWrappers.isPresent()) {
                        var component = foundComponentWrappers.get();
                        if (componentClass.getName().contains("Reference")) {
                            var value = field.get(model).toString();

                            Actions actions = new Actions(browser().getWrappedDriver());

                            actions.sendKeys(component.getWrappedElement(), value)
                                    .pause(Duration.ofMillis(500))
                                    .sendKeys(Keys.ARROW_DOWN)
                                    .pause(Duration.ofMillis(500))
                                    .sendKeys(Keys.ENTER)
                                    .perform();

                        } else if (componentClass.getName().contains("Choice")){

                            var choice = ((UiBuilderRecordChoice)component);
                            choice.dropdownButton().click();
                            choice.getOptionByText(field.get(model).toString()).click();
                        } else {
                            component.getShadowRoot().createByCss(TextInput.class,"input" ).getWrappedElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE, field.get(model).toString(), Keys.ENTER);
                        }
                    }
                    else {
                        Log.info("Form Field with label: '%s' Not found.".formatted(elementLabel));
                    }
                }
                catch (IllegalArgumentException | NotFoundException iae) {
                    Log.info("Form Field with label: '%s' Not found".formatted(elementLabel));
                }
                catch (Exception e) {
                    throw new RuntimeException("Failed to read field value for label: %s".formatted(elementLabel), e);
                }
            }
        }
    }

    private static <SnClass extends UIBDefaultComponent> Method getSetTextMethod(Class<SnClass> snClass) {
        List<Method> methodContainingClass = new ArrayList<>();
        Class<?> currentClass = snClass;
        while (methodContainingClass.isEmpty()) {
            methodContainingClass.addAll(Arrays.stream(currentClass.getDeclaredMethods()).filter(x -> {
                x.setAccessible(true);
                return x.getName().equals("getSetTextParamClass");
            }).toList());

            currentClass = currentClass.getSuperclass();
        }

        Object setTextParamType = null;
        try {
            setTextParamType = methodContainingClass.get(0).invoke(snClass.getConstructor().newInstance());
            return snClass.getDeclaredMethod("setText", (Class<?>) setTextParamType);
        } catch (Exception e) {
            throw new RuntimeException("Fill form failed with Error:", e);
        }
    }
}