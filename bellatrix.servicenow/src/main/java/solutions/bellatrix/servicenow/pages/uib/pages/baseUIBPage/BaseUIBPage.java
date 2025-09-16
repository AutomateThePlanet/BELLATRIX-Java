package solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.remote.RemoteWebElement;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.servicenow.components.models.ServiceNowForm;
import solutions.bellatrix.servicenow.components.uiBuilder.RecordChoice;
import solutions.bellatrix.servicenow.components.uiBuilder.UIBDefaultComponent;
import solutions.bellatrix.servicenow.components.uiBuilder.UiBuilderRecordChoice;
import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.FieldLabel;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.FieldLocator;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.Id;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.UibComponent;
import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;
import solutions.bellatrix.servicenow.pages.uib.pages.uibRecordViewPage.UibRecordViewPage;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;
import solutions.bellatrix.servicenow.pages.uib.sections.mainContentSection.MainContentSection;
import solutions.bellatrix.servicenow.pages.uib.sections.menuHeader.MenuHeaderSection;
import solutions.bellatrix.servicenow.pages.uib.sections.tabsSection.TabsSection;
import solutions.bellatrix.servicenow.utilities.generators.BaseInstancesUrlGeneration;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.WebPage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                    var foundComponentWrappers = container.createAllByXPath(componentClass, String.format(".//*[contains(name(),'sn-record-') and descendant::*[text()='%s']]", elementLabel)).stream().findAny();

                    if (foundComponentWrappers.isPresent()) {
                        var component = foundComponentWrappers.get();
                        String actualValue;
                        if (componentClass.getName().contains("Reference")) {
                            actualValue = component.getValue();
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
                              component.getWrappedElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE, field.get(model).toString(), Keys.ENTER);
                         } else if (componentClass.getName().contains("Choice")){

                            var choice = ((UiBuilderRecordChoice)component);
                            choice.dropdownButton().click();
                            choice.getOptionByText(field.get(model).toString()).click();
//                           choice.getOptionByText(field.get(model).toString());
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

//            if (field.isAnnotationPresent(UibComponent.class) && field.isAnnotationPresent(FieldLabel.class)) {
//                var componentClass = field.getDeclaredAnnotation(UibComponent.class).value();
//                var elementLabel = field.getDeclaredAnnotation(FieldLabel.class).value();
//
//                try {
//                    var foundComponentWrappers = container.createAllByXPath(componentClass, String.format(".//*[contains(name(),'sn-record-') and descendant::*[text()='%s']]", elementLabel)).stream().findAny();
//                    if (foundComponentWrappers.isPresent()) {
//
//                        var component = foundComponentWrappers.get();
//                        Method setTextMethod = getSetTextMethod(componentClass);
//                        Object setTextValue = null;
//                        try {
//                            setTextValue = field.get(model);
//                            setTextMethod.invoke(component, setTextValue);
//                        } catch (Exception e) {
//                            throw new RuntimeException("Fill Form failed with Error.", e);
//                        }
//                    }
//
//                }
//                catch (IllegalArgumentException | NotFoundException iae) {
//                    Log.info("Form Field with label: '%s' Not found".formatted(elementLabel));
//                }
//                catch (Exception e) {
//                    throw new RuntimeException("Failed to read field value for label: %s".formatted(elementLabel), e);
//                }
//            }


//            // Refactor after read and Assert are working as expected
//            var componentClass = field.getDeclaredAnnotation(UibComponent.class).value();
//            var elementId = field.getDeclaredAnnotation(Id.class).value();
//            var component = formComponent.createByXPath(componentClass, String.format(".//*[@id='%s']", elementId));
//
//            Method setTextMethod = getSetTextMethod(componentClass);
//            Object setTextValue = null;
//            try {
//                setTextValue = field.get(model);
//                setTextMethod.invoke(component, setTextValue);
//            } catch (Exception e) {
//                throw new RuntimeException("Fill Form failed with Error.", e);
//            }
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