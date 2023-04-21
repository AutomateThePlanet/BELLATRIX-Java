package O7_common_services.java_script_service_04;

import org.junit.jupiter.api.Test;
import org.testng.Assert;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.PasswordInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class JavaScriptServiceTests extends WebTest {
    // BELLATRIX gives you an interface for easier execution of JavaScript code using the script method. You need to make
    // sure that you have navigated to the desired web page.
    @Test
    public void fillUpAllFields() {
        app().navigate().to("http://demos.bellatrix.solutions/my-account/");

        app().script().execute("document.getElementById('username').value = 'BELLATRIX';");
        // Execute a JavaScript code on the page. Here we find an element with id = ‘firstName’ and sets its value to ‘BELLATRIX’.


        app().create().byId(PasswordInput.class, "password").setPassword("Gorgeous");
        var button = app().create().byClassContaining(Button.class, "woocommerce-Button button");

        app().script().execute("arguments[0].click();", button);
        // It is possible to pass an element, and the script executes on it.
    }

    @Test
    public void getElementStyle() {
        app().navigate().to("http://demos.bellatrix.solutions/my-account/");

        var resultsCount = app().create().byClassContaining(WebComponent.class, "woocommerce-result-count");

        String fontSize = app().script().execute("return arguments[0].style.font-size", resultsCount.getWrappedElement());
        // Get the results from a script. After that, get the value for a specific style and assert it.
        Assert.assertEquals(fontSize, "14px");
    }
}