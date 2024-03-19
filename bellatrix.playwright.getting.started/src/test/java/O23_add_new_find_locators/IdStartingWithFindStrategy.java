package O23_add_new_find_locators;

import com.microsoft.playwright.Page;
import org.openqa.selenium.By;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.findstrategies.CssFindStrategy;
import solutions.bellatrix.playwright.findstrategies.options.AltTextOptions;

public class IdStartingWithFindStrategy extends CssFindStrategy {
    protected IdStartingWithFindStrategy(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.format("id starting with %s", value());
    }
}