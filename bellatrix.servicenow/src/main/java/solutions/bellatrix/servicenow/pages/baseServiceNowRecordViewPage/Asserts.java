package solutions.bellatrix.servicenow.pages.baseServiceNowRecordViewPage;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import solutions.bellatrix.servicenow.components.data.ServiceNowForm;
import solutions.bellatrix.servicenow.components.serviceNow.SnChoice;
import solutions.bellatrix.servicenow.components.serviceNow.SnDefaultComponent;
import solutions.bellatrix.servicenow.contracts.FieldReadonly;
import solutions.bellatrix.servicenow.contracts.SnFormField;
import solutions.bellatrix.servicenow.snSetupData.annotations.snFieldAnnotations.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import solutions.bellatrix.servicenow.models.enums.serviceNowFormFieldsDescription.InputType;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentReadonly;
import solutions.bellatrix.web.pages.PageAsserts;

public class Asserts<MapT extends Map> extends PageAsserts<MapT> {
    private static <Form extends ServiceNowForm> List<Field> getNotNullFields(Form formModel) {
        return Arrays.stream(formModel.getClass().getDeclaredFields()).filter(f -> {
            try {
                f.setAccessible(true);
                return f.get(formModel) != null;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public void assertPageHeadingContains(String textToContain) {
        map().recordHeading().validateTextIs(textToContain);
    }

    public void assertInputIsNotEmpty(String fieldName) {
        var elementValue = map().takeInput(fieldName).getValue();
        var errorMessage = String.format("%s field is empty", fieldName);

        assertFalse(elementValue.isEmpty(), errorMessage);
    }

    public void assertFormSelectField(String fieldName) {
        var actualFormElement = map().selectFormInput(fieldName);
        actualFormElement.validateIsVisible();
    }

    public void assertRecordNumberHeadingContains(String textToContain) {
        map().recordNumberHeading().validateTextContains(textToContain);
    }

    public void assertRecordHeader(String recordNumber) {
        map().recordTitleValue().validateTextIs(recordNumber);
    }

    public void assertRecordMainHeader(String heading) {
        map().recordHeading().validateTextIs(heading);
    }

    public void assertListInputValuesReadOnly(List<String> listInputLabels) {
        listInputLabels.forEach(l -> map().takeInput(l).validateIsReadonly());
    }

    public void assertListSelectValuesReadOnly(List<String> listInputLabels) {
        listInputLabels.forEach(l -> map().selectFormInput(l).validateIsReadonly());
    }

    public void assertListTextAreaValuesReadOnly(List<String> listInputLabels) {
        listInputLabels.forEach(l -> map().textAreaByLabel(l).validateIsReadonly());
    }

    public void assertListCheckBoxValuesReadOnly(List<String> listInputLabels) {
        listInputLabels.forEach(l -> assertTrue(BaseServiceNowRecordViewPage.hasAriaDisabledAncestorOrSelf(map().checkBoxByLabel(l)), "Not disabled"));
    }

    public void assertButtonsByTextVisible(List<String> buttonLabels) {
        buttonLabels.forEach(l -> map().buttonInRightNavigationBarByText(l).validateIsVisible());
    }

    public void assertLabelsExist(List<String> expectedLabels) {
        var actualLabels = map().allLabels().stream().map(Label::getText).toList();
        var errorMessage = String.format("Actual labels were\n%s\nbut they were\n%s", String.join("\n", actualLabels), String.join("\n", expectedLabels));

        assertTrue(new HashSet<>(actualLabels).containsAll(expectedLabels), errorMessage);
    }

    @SuppressWarnings("resource")
    public void assertUrlStartsWith(String expectedUrl) {
        app().browser().assertLandedOnPage(expectedUrl);
    }

    public <SnField extends SnFormField> void assertFields(List<SnField> fields) {
        fields.forEach(f -> {
            if (f.isRequired()) {
                assertLabelRequiredMarker(f);
            }

            switch (f.getFieldType()) {
                case INPUT -> assertInput(f);
                case SELECT -> assertSelect(f);
                case TEXTAREA -> assertTextArea(f);
            }
        });
    }

    private <SnField extends SnFormField> void assertLabelRequiredMarker(SnField field) {
        var label = field.getLabel();
        var marker = map().labelRequiredMarker(field.getLabel());
        marker.validateIsVisible();

        var classes = Arrays.stream(marker.getHtmlClass().split(" ")).toList();
        var mandatory = marker.getAttribute("mandatory");

        assertAll(() -> assertTrue(classes.contains("required-marker"), String.format("%s required marker does not contains class \"mandatory\"", label)), () -> assertEquals("true", mandatory, String.format("%s required marker attribute should be true but it was false", label)));
    }

    private <SnField extends SnFormField> void assertInput(SnField field) {
        var label = field.getLabel();
        var input = map().takeInput(field.getLabel());
        input.validateIsVisible();

        var type = input.getAttribute("type");
        var formGroup = map().formGroup(field.getLabel());
        var formGroupClasses = Arrays.stream(formGroup.getHtmlClass().split(" ")).toList();
        var inputClasses = Arrays.stream(input.getHtmlClass().split(" ")).toList();

        assertAll(() -> assertEquals(field.isRequired(), formGroupClasses.contains("is-required"), String.format("%s input required state is wrong", label)));

        if (field.getInputType() == InputType.DECIMAL) {
            assertTrue(inputClasses.contains(InputType.DECIMAL.getValue()), String.format("%s input does not contains class decimal", label));
            assertEquals(InputType.TEXT.getValue(), type, String.format("%s input does not contains class decimal", label));
        } else {
            assertEquals(field.getInputType().getValue(), type, String.format("%s type is not correct", label));
        }
    }

    private <SnField extends SnFormField> void assertSelect(SnField field) {
        var label = field.getLabel();
        var select = map().selectFormInput(field.getLabel());
        select.validateIsVisible();

        var isRequired = Boolean.parseBoolean(select.getAttribute("aria-required"));

        assertAll(() -> assertEquals(field.isRequired(), isRequired, String.format("%s input required state is wrong", label)));
    }

    private <SnField extends SnFormField> void assertTextArea(SnField field) {
        var label = field.getLabel();
        var textArea = map().textAreaByLabel(field.getLabel());
        var textAreaClasses = Arrays.stream(textArea.getHtmlClass().split(" ")).toList();

        if (!textAreaClasses.contains("htmlField")) {
            textArea.validateIsVisible();
        }

        var isRequired = Boolean.parseBoolean(textArea.getAttribute("aria-required"));

        assertAll(() -> assertEquals(field.isRequired(), isRequired, String.format("%s input required state is wrong", label)));
    }

    /**
     * In the service now form we have tables tabs buttons whose names are split by &nbsp; to fix that and to remove the counting numbers we are converting the actual getText() result
     */
    public void assertBottomTablesTabs(List<String> expectedTabsText) {
        var actualTabsText = map().bottomTablesTabs().stream().map(x -> x.getText().replace("\u00A0", " ").split("\\(")[0].trim()).filter(x -> !x.isEmpty()).toList();

        assertEquals(expectedTabsText, actualTabsText, "Tabs are not as expected");
    }

    public void assertInnerTableColumns(String tabText, List<String> expectedValues) {
        var actualValues = map().getSecondTableColumns(tabText).stream().map(x -> {
            String rawText = x.getText();
            return rawText;
        }).collect(Collectors.toList());

        assertEquals(expectedValues, actualValues, "Mismatch between expected and actual table column values");
    }

    public void assertInputIsReadOnly(String label) {
        map().takeInput(label).validateIsReadonly();
    }

    public <SnField extends FieldReadonly> void assertFieldsAreReadOnly(List<SnField> fields) {
        fields.forEach(f -> {
            ComponentReadonly field = map().create().byXPath(f.getFieldType().getComponent(), f.getFieldType().getLocator(f.getLabel()));

            if (f.isReadonly()) {
                field.validateIsReadonly();
            } else {
                field.validateNotReadonly();
            }
        });
    }

    public <Form extends ServiceNowForm, FormComponent extends WebComponent> void validateFormState(Form formModel, FormComponent formComponent) {
        validateFormState(formModel, formComponent, true);
    }

    public <Form extends ServiceNowForm, FormComponent extends WebComponent> void validateFormState(Form formModel, FormComponent formComponent, Boolean checkSelectedValue) {
        var modelFields = formModel.getClass().getDeclaredFields();

        for (int i = 0; i < modelFields.length; i++) {
            var componentClass = modelFields[i].getDeclaredAnnotation(Component.class).value();
            var formGroup = getFormGroup(formComponent, i, componentClass);

            var fieldLabel = modelFields[i].getDeclaredAnnotation(FieldLabel.class).value();
            formGroup.validateLabel(fieldLabel);

            var fieldRequiredState = modelFields[i].getDeclaredAnnotation(Required.class);
            formGroup.validateIsRequired(fieldRequiredState != null);

            formGroup.validateType();

            if (componentClass == SnChoice.class) {
                var options = modelFields[i].getDeclaredAnnotation(SelectOptions.class).value();
                validateOptions(formComponent, i, options);
            }

            if (checkSelectedValue) {
                if (componentClass == SnChoice.class) {
                    var selectedOption = modelFields[i].getDeclaredAnnotation(SelectedOption.class).value();
                    validateSnTextIs(formComponent, modelFields[i], componentClass, selectedOption);
                }
            }
        }
    }

    private <FormComponent extends WebComponent> void validateOptions(FormComponent formComponent, int elementNumber, String[] options) {
        var formGroup = getFormGroup(formComponent, elementNumber, SnChoice.class);
        formGroup.validateOptions(options);
    }

    public <FormComponent extends WebComponent, SnComponent extends SnDefaultComponent> SnComponent getFormGroup(FormComponent formComponent, int count, Class<SnComponent> component) {
        var formLocator = formComponent.getFindStrategy().getValue();
        var xpathLocator = String.format("(%s//div[contains(concat(' ',normalize-space(@class),' '),' form-group ') and not(contains(@style,'display: none')) and not(contains(concat(' ',normalize-space(@class),' '),' custom-form-group '))])[%d]", formLocator, count + 1);
        return formComponent.createByXPath(component, xpathLocator);
    }

    @SneakyThrows
    public <Form extends ServiceNowForm, FormComponent extends WebComponent> void validateFormNotNullValues(Form formModel, FormComponent formComponent) {
        var fields = getNotNullFields(formModel);

        for (Field field : fields) {
            field.setAccessible(true);
            var expectedText = field.get(formModel);

            var componentClass = field.getDeclaredAnnotation(Component.class).value();

            validateSnTextIs(formComponent, field, componentClass, expectedText);
        }
    }

    private <SnComponent extends SnDefaultComponent, FormComponent extends WebComponent> void validateSnTextIs(FormComponent formComponent, Field field, Class<SnComponent> componentClass, Object expectedValue) {
        var elementId = field.getDeclaredAnnotation(Id.class).value();
        var component = (SnComponent) formComponent.createByXPath(componentClass, String.format(".//*[@id='%s']", elementId));

        var expectedText = expectedValue.toString();
        component.validateTextIs(expectedText);
    }

    public void assertButtonsAreVisible(String... buttonNames) {
        assertButtonsAreVisible(Arrays.stream(buttonNames).toList());
    }

    public void assertButtonsAreVisible(List<String> buttonNames) {
        buttonNames.forEach(x -> map().buttonByName(x).validateIsVisible());
    }

    public void assertErrorMessage(String message) {
        assertEquals(message, map().allertMessage().getText(), message);
    }
}