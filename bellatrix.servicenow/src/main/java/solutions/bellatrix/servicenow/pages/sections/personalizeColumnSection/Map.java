package solutions.bellatrix.servicenow.pages.sections.personalizeColumnSection;

import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.pages.PageMap;

import java.util.List;

public class Map extends PageMap {
    public List<Button> availableColumns() {
        return create().allByXPath(Button.class, "//select[@aria-label='Available']//option");
    }

    public List<Button> selectedColumns() {
        return create().allByXPath(Button.class, "//select[@aria-label='Selected']//option");
    }

    public Button availableColumnByName(String name) {
        var locator = String.format("//select[@aria-label='Available']//option[text()='%s']", name);

        return create().byXPath(Button.class, locator);
    }

    public Anchor addArrowAnchor() {
        return create().byXPath(Anchor.class, "//a[@models-original-title='Add']");
    }

    public Anchor removeArrowAnchor() {
        return create().byXPath(Anchor.class, "//a[@models-original-title='Remove']");
    }

    public Button okButton() {
        return create().byId(Button.class, "ok_button");
    }
}