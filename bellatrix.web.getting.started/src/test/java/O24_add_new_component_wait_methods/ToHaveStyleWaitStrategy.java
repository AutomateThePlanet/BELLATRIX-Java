package O24_add_new_component_wait_methods;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.waitstrategies.WaitStrategy;

public class ToHaveStyleWaitStrategy extends WaitStrategy {

    // Imagine that you want to wait for an element to have a specific style. First, you need to create a new ‘WaitStrategy’
    // class that inheriting the WaitStrategy class.

    public final String elementStyle;

    @Override
    public void waitUntil(SearchContext searchContext, By by) {
        waitUntil((x) -> elementHasStyle(searchContext, by));
    }

    public ToHaveStyleWaitStrategy(String elementStyle) {
        timeoutInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getElementToBeClickableTimeout();
        sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
        this.elementStyle = elementStyle;
    }

    public static ToHaveStyleWaitStrategy of(String elementStyle) {
        return new ToHaveStyleWaitStrategy(elementStyle);
    }

    private boolean elementHasStyle(SearchContext searchContext, By by) {
        var element = findElement(searchContext, by);
        try {
            return element != null && element.getAttribute("style").equals(elementStyle);
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return false;
        }
    }
}