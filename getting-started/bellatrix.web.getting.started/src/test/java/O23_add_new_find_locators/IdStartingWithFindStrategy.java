package O23_add_new_find_locators;

import org.openqa.selenium.By;
import solutions.bellatrix.web.findstrategies.FindStrategy;

public class IdStartingWithFindStrategy extends FindStrategy {
    protected IdStartingWithFindStrategy(String value) {
        super(value);
    }

    @Override
    public By convert() {
        return By.cssSelector(String.format("[id^='%s']", getValue()));
        // In the convert method, we use a standard WebDriver By locator, and in this case we implement our requirements
        // through a little CSS.
    }

    @Override
    public String toString() {
        return String.format("id starting with %s", getValue());
    }
}