package solutions.bellatrix.servicenow.components.serviceNow;

import org.openqa.selenium.Keys;
import solutions.bellatrix.servicenow.components.data.enums.SnComponentType;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.TextInput;

public class SnTableName extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.TABLE_NAME;
    }

    @Override
    public String getText() {
        return elementWithText().getText();
    }

    @Override
    public void setText(String text) {
        formControl().getWrappedElement().click();
        browserService.waitForAjax();
        popUpInput().setText(text + Keys.ENTER);
    }

    @Override
    protected String formControlXpathLocator() {
        return ".//*[contains(@class,'form-control')]";
    }

    public Boolean isDisabled() {
        return formControl().getWrappedElement().getTagName().equals("p");
    }

    private Div elementWithText() {
        var xpathLocator = ".//*[text()]";
        return formControl().createByXPath(Div.class, xpathLocator);
    }

    private Div popUpWrapper() {
        var xpathLocator = "//div[contains(concat(' ',normalize-space(@class),' '),' select2-drop-active ')]";
        return createByXPath(Div.class, xpathLocator);
    }

    public TextInput popUpInput() {
        var xpathLocator = ".//input";
        return popUpWrapper().createByXPath(TextInput.class, xpathLocator);
    }
}