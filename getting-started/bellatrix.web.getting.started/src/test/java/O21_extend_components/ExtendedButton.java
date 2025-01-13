package O21_extend_components;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import solutions.bellatrix.web.components.Button;

public class ExtendedButton extends Button {
    public void submitButtonWithEnter() {
        var action = new Actions(getWrappedDriver());
        action.moveToElement(getWrappedElement()).sendKeys(Keys.ENTER).perform();
    }

    public void javaScriptFocus() {
        getJavaScriptService().execute("window.focus();");
        getJavaScriptService().execute("arguments[0].focus();", this);
    }
    // The way of extending an existing component is to create a child component. Extend the component you need. In this
    // case, two methods are added to the standard Button component. Next in your tests, use the ExtendedButton instead
    // of regular Button to have access to these methods. The same strategy can be used to create a completely new component
    // that BELLATRIX does not provide. You need to extend the WebComponent as a base class.
}