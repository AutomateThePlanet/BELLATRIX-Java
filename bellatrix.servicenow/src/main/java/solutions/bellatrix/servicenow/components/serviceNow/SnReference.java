package solutions.bellatrix.servicenow.components.serviceNow;

import org.openqa.selenium.Keys;
import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.Span;

@SuppressWarnings("unused")
public class SnReference extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.REFERENCE;
    }

    @Override
    public String getText() {
        return formControl().getText();
    }

    @Override
    public void setText(String text) {
        formControl().setText(text + Keys.ENTER);
        super.invokeValueSetEvent(text);
    }

    public Span searchIcon() {
        var xpathLocator = ".//span[contains(@class,'icon-search')]";
        return this.createByXPath(Span.class, xpathLocator);
    }
}