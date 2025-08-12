package solutions.bellatrix.servicenow.components.serviceNow;

import solutions.bellatrix.servicenow.components.enums.SnComponentType;
import solutions.bellatrix.web.components.Span;

public class SnJournalInput extends SnDefaultComponent {
    @Override
    public SnComponentType componentType() {
        return SnComponentType.JOURNAL_INPUT;
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

    @Override
    public String getType() {
        var xpathLocator = "./div[@type]";
        return this.createByXPath(Span.class, xpathLocator).getWrappedElement().getAttribute("type");
    }
}