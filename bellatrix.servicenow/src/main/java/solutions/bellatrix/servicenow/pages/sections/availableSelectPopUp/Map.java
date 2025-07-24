package solutions.bellatrix.servicenow.pages.sections.availableSelectPopUp;

import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.pages.PageMap;

import java.util.List;

public class Map extends PageMap {
    public List<Label> availableRoles() {
        return create().allByXPath(Label.class, "//select[@aria-label='Available']//option[@value]");
    }

    public Button rolesOkButton() {
        return create().byXPath(Button.class, "//span[@class='pull-right']//Button[text()='OK']");
    }

    public Button arrowAddButton() {
        return create().byXPath(Button.class, "//a[@data-original-title='Add']");
    }
}