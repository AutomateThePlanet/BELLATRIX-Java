package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.servicenow.contracts.FieldLabel;

@SuppressWarnings("SameParameterValue")
public class ServiceNowLocatorGenerator {
    private static String addRelativePart(boolean shouldBeRelative, String coreXpathLocator) {
        if (shouldBeRelative) {
            return "." + coreXpathLocator;
        }

        return coreXpathLocator;
    }

    public static String formGroupByLabel(String label) {
        return formGroupByLabel(label, false);
    }

    private static String formGroupByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' form-group ') and .//span[text()='%s']]", label);
        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String customFormGroupByLabel(String label) {
        return customFormGroupByLabel(label, false);
    }

    private static String customFormGroupByLabel(String label, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//div[contains(concat(' ',normalize-space(@class),' '),' custom-form-group ') and .//span[text()='%s']]", label);
        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String tabByLabel(FieldLabel fieldLabel) {
        var labelText = fieldLabel.getLabel().replace(" ", "\u00A0");
        var xpath = "//span[@class='tab_caption_text' and contains(text(),'%s')]".formatted(labelText);
        return xpath;
    }

    public static String divByAriaLabelText(String text) {
        return divByAriaLabelText(text, false);
    }

    private static String divByAriaLabelText(String text, boolean shouldBeRelative) {
        var coreXpathLocator = "//div[@aria-label='%s']".formatted(text);
        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String spanWithTabCaptionText(String text) {
        return spanWithTabCaptionText(text, false);
    }

    private static String spanWithTabCaptionText(String text, boolean shouldBeRelative) {
        var coreXpathLocator = String.format("//span[@tab_caption='%s']", text);
        return addRelativePart(shouldBeRelative, coreXpathLocator);
    }

    public static String elementWithTabCaption(FieldLabel fieldLabel) {
        var xpath = "//*[@tab_caption='%s']".formatted(fieldLabel.getLabel());
        return xpath;
    }

    public static String buttonByContainingText(String name) {
        var xpath = "//button[contains(text(), '%s')]".formatted(name);
        return xpath;
    }
}