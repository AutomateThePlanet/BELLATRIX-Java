package O8_UIB.data.pages.assetsRequestPage;

import org.junit.jupiter.api.Assertions;

public class Asserts extends solutions.bellatrix.servicenow.pages.uib.pages.uibTableViewPage.Asserts<Map> {
    public void assertPopupHeading() {
        var expectedHeading = "Edit List";
        var actualHeading = map().popupHeading().getText();

        Assertions.assertEquals(expectedHeading, actualHeading, "The PopUp Heading is not as expected. ");
    }

    public void assertEditColumnIsChecked(String locator) {
        map().listActionsIcon().click();
        map().editColumnsButton().click();
        var columnCheckbox = map().anyColumnCheckboxOnEditColumnList(locator);
        var isChecked = columnCheckbox.getAttribute("checked");
        if (!(isChecked == null || "null".equals(isChecked))) {
            map().closeDialogueOnEditList().click();
        } else {
            throw new RuntimeException("Column is not checked ");
        }
    }

    public void assertEditColumnNotChecked(String locator) {
        map().listActionsIcon().click();
        map().editColumnsButton().click();
        var columnCheckbox = map().anyColumnCheckboxOnEditColumnList(locator);
        var isChecked = columnCheckbox.getAttribute("checked");
        if (isChecked == null || "null".equals(isChecked)) {
            map().closeDialogueOnEditList().click();
        } else {
            throw new RuntimeException("Column is checked ");
        }
    }
}
