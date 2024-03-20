package O21_extend_components;

import com.microsoft.playwright.Keyboard;
import solutions.bellatrix.playwright.components.Button;

public class ExtendedButton extends Button {
    public void submitButtonWithEnter() {
        wrappedElement().press("Enter");
    }

    public void javaScriptFocus() {
        wrappedBrowser().currentPage().evaluate("window.focus();");
        wrappedElement().evaluate("el => el.arguments.focus();");
    }
    // The way of extending an existing component is to create a child component. Extend the component you need. In this
    // case, two methods are added to the standard Button component. Next in your tests, use the ExtendedButton instead
    // of regular Button to have access to these methods. The same strategy can be used to create a completely new component
    // that BELLATRIX does not provide. You need to extend the WebComponent as a base class.
}