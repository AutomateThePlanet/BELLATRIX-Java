package solutions.bellatrix.servicenow.components.serviceNow;

import org.openqa.selenium.Keys;
import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextInput;

public class SnDocumentId extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.DOCUMENT_ID;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    @Override
    public void setText(String text) {
        searchIcon().click();
        documentInput().setText(text + Keys.ENTER);
        okButton().click();
    }

    public Button searchIcon() {
        var xpathLocator = ".//span[contains(concat(' ',normalize-space(@class),' '),' icon-search ')]";
        return this.createByXPath(Button.class, xpathLocator);
    }

    private Span documentSelector() {
        var xpathLocator = "//div[@id='document_selector']";
        return this.createByXPath(Span.class, xpathLocator);
    }

    private TextInput documentInput() {
        var xpathLocator = ".//input[@id='sys_display.document_key']";
        return documentSelector().createByXPath(TextInput.class, xpathLocator);
    }

    private Button okButton() {
        var xpathLocator = ".//button[@id='ok_button']";
        return documentSelector().createByXPath(Button.class, xpathLocator);
    }
}