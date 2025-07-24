package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.servicenow.snSetupData.enums.AttributeClassToFunctionality;

@SuppressWarnings("unused")
public class DevExtremeLocatorGenerator {
    public static String buttonLocatorByAriaLabel(String ariaLabel) {
        return buttonLocatorByAriaLabel(ariaLabel, false);
    }

    public static String buttonLocatorByAriaLabel(String ariaLabel, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-button ') and @aria-label='%s']", ariaLabel);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String buttonLocatorByContainingText(String ariaLabel) {
        return buttonLocatorByContainingText(ariaLabel, false);
    }

    public static String buttonLocatorByContainingText(String ariaLabel, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-button ') and .//descendant-or-self::*[text()='%s']]", ariaLabel);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String buttonLocatorByContainingClass(String className) {
        return buttonLocatorByContainingClass(className, false);
    }

    public static String buttonLocatorByContainingClass(String className, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-button ') and contains(concat(' ',normalize-space(@class),' '),' %s ')]", className);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String normalButtonLocatorByContainingText(String text) {
        return normalButtonLocatorByContainingText(text, false);
    }

    public static String normalButtonLocatorByContainingText(String text, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-button-normal ') and .//descendant-or-self::*[text()='%s']]", text);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String textBoxLocatorByLabel(String label) {
        return textBoxLocatorByLabel(label, false);
    }

    public static String textBoxLocatorByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//label[.//*[text()='%s']]/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-texteditor ')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String textEditorLocatorByLabel(String label) {
        return textEditorLocatorByLabel(label, false);
    }

    public static String textEditorLocatorByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//label[.//*[text()='%s']]/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-texteditor ')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String buttonLocatorByIcon(AttributeClassToFunctionality icon) {
        return buttonLocatorByIcon(icon, false);
    }

    public static String buttonLocatorByIcon(AttributeClassToFunctionality icon, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-button ') and .//*[name()='path' and starts-with(@d,'%s')]]", icon.getValue());

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String addIconElementLocator() {
        return addIconElementLocator(false);
    }

    public static String addIconElementLocator(boolean shouldBeRelative) {
        return iconElementLocator(AttributeClassToFunctionality.ADD, shouldBeRelative);
    }

    public static String iconElementLocator(AttributeClassToFunctionality icon) {
        return iconElementLocator(icon, false);
    }

    public static String iconElementLocator(AttributeClassToFunctionality icon, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//*[name()='svg' and ./*[name()='path' and contains(@d,'%s')]]", icon);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String checkBoxByLabel(String label) {
        return textBoxLocatorByLabel(label, false);
    }

    public static String checkBoxByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//label[.//*[text()='%s']]/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-checkbox ')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    private static String addRelativePart(boolean shouldBeRelative, String coreXpathLocator) {
        if (shouldBeRelative) {
            return "." + coreXpathLocator;
        }

        return coreXpathLocator;
    }

    public static String radioGroupByLabel(String label) {
        return radioGroupByLabel(label, false);
    }

    public static String radioGroupByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//label[.//*[text()='%s']]/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-radiogroup ')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String radioButtonByLabel(String label) {
        return radioButtonByLabel(label, false);
    }

    public static String radioButtonByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//label[.//*[text()='%s']]/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-radiobutton ')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String dropDownButtonByContainingText(String text) {
        return dropDownButtonByContainingText(text, false);
    }

    public static String dropDownButtonByContainingText(String text, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-dropdownbutton ') and .//descendant-or-self::*[text()='%s']]", text);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String dropDownBoxByLabel(String label) {
        return dropDownBoxByLabel(label, false);
    }

    public static String dropDownBoxByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//label[.//*[text()='%s']]/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-dropdownbox ')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String inputDropDownLocatorByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//*[text()='%s']/following-sibling::div//div[contains(concat(' ',normalize-space(@class),' '),' dx-dropdowneditor-input-wrapper')]", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String inputDropDownLocator() {
        return inputDropDownLocator(false);
    }

    public static String inputDropDownLocator(boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-scrollable-wrapper ')]");

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String inputDropDownLocatorByColumnName(String columnName, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//tr[contains(@class,'dx-datagrid-filter-row')]/td[count(//tr[contains(@class, 'dx-header-row')]/td[.//*[text()='%s']]/preceding-sibling::td)+1]//div[contains(concat(' ',normalize-space(@class),' '),' dx-scrollable-wrapper ')]", columnName);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String inputDropDownLocatorByColumnName(String columnName) {
        return inputDropDownLocatorByColumnName(columnName, false);
    }

    public static String checkBoxByText(String checkboxText) {
        return checkBoxByText(checkboxText, false);
    }

    public static String checkBoxByText(String checkboxText, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' dx-checkbox ') and .//descendant-or-self::*[text()='%s']]", checkboxText);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String normalButtonWithText() {
        return normalButtonWithText(false);
    }

    public static String normalButtonWithText(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[contains(concat(' ',normalize-space(@class),' '),' dx-button-normal ') and .//descendant-or-self::*[text()]]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String buttonWithText() {
        return buttonWithText(false);
    }

    public static String buttonWithText(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[contains(concat(' ',normalize-space(@class),' '),' dx-button ') and .//descendant-or-self::*[text()]]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String toastByClass() {
        return toastByClass(false);
    }

    public static String toastByClass(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[contains(concat(' ',normalize-space(@class),' '),' dx-toast ')]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String toastByClassDoesNotContainsStateInvisible() {
        return toastByClassDoesNotContainsStateInvisible(false);
    }

    public static String toastSuccess() {
        return toastSuccess(false);
    }

    private static String toastSuccess(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[contains(concat(' ',normalize-space(@class),' '),' dx-toast-success ')]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String toastByClassDoesNotContainsStateInvisible(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[contains(concat(' ',normalize-space(@class),' '),' dx-toast ') and not(contains(concat(' ',normalize-space(@class),' '),' dx-state-invisible '))]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String filterBuilder() {
        return filterBuilder(false);
    }

    public static String filterBuilder(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[@class[contains(string(), 'dx-filterbuilder dx-widget')]]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String dropDownButtonByLabel(String label) {
        return dropDownButtonByLabel(label, false);
    }

    public static String dropDownButtonByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//span[text()='%s']/ancestor::label/following-sibling::div", label);

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String dropDownChoiceByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = "./descendant::input[1]";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String divWithRoleTablist() {
        return divWithRoleTablist(false);
    }

    public static String divWithRoleTablist(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[@role='tablist']";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String elementWithClassFieldItemLabelText() {
        return elementWithClassFieldItemLabelText(false);
    }

    public static String elementWithClassFieldItemLabelText(boolean shouldBeRelative) {
        var coreXpathLocator = "//*[@class='dx-field-item-label-text']";

        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String grid(boolean shouldBeRelative) {
        var coreXpathLocator = "//div[@role='presentation' and ./div[contains(@class,'dx-datagrid') and @role='grid']]";
        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String elementWithClassActionText(String text) {
        return elementWithClassActionText(text, false);
    }

    public static String elementWithClassActionText(String textElement, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//*[contains(@class,'dx-datagrid-action')][text()='%s']", textElement);
        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String elementWithClassFieldItemLabelContent() {
        var coreXpathLocator = ".//descendant::span[@class='dx-field-item-label-content']/span[1]";

        return coreXpathLocator;
    }
}