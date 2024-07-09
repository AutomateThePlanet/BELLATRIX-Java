package solutions.bellatrix.web.components.shadowdom;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.List;

public class ShadowHost extends RemoteWebElement {
    public List<WebElement> findElements(By by) {
        return this.getShadowRoot().findElements(by);
    }

    public WebElement findElement(By by) {
        return this.getShadowRoot().findElement(by);
    }
}
