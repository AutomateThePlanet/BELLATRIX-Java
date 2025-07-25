package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Div;

public class SnGlideList extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.GLIDE_LIST;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    @Override
    public void setText(String text) {
        formControl().setText(text);
        super.invokeValueSetEvent(text);
    }

    public void addMe() {
        addMeButton().click();
        browserService.waitForAjax();
    }

    public Button addMeButton() {
        var xpathLocator = ".//span[contains(concat(' ',normalize-space(@class),' '),' icon-user-add ')]";
        return this.createByXPath(Button.class, xpathLocator);
    }

    public Div staticFormControl() {
        var xpathLocator = ".//p[contains(concat(' ',normalize-space(@class),' '),' form-control-static ')]";
        return this.createByXPath(Div.class, xpathLocator);
    }

    @Override
    public Boolean isDisabled() {
        var staticFormControlTagName = staticFormControl().getWrappedElement().getTagName();
        if (staticFormControlTagName!=null && staticFormControlTagName.equals("p")) {
            return false;
        }

        return staticFormControl().getAttribute("title") == null || formControl().getHtmlClass().contains("form-control-static");
    }

    @Override
    public TextInput formControl() {
        return this.createByXPath(TextInput.class, ".//*[contains(concat(' ',normalize-space(@class),' '),' form-control')]");
    }
}