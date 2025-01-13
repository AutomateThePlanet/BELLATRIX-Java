package solutions.bellatrix.playwright.components.common.webelement;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;
import solutions.bellatrix.playwright.components.common.webelement.options.*;

import java.util.regex.Pattern;

/**
 * Wrapper for Playwright FrameLocator.
 */
@Getter
public class FrameElement extends WebElement {
    public FrameElement(Locator locator) {
        super(locator);
        this.wrappedFrameLocator = locator.contentFrame();
    }

    public FrameElement(WebElement element) {
        this(element.getWrappedLocator());
    }

    private final FrameLocator wrappedFrameLocator;

    public WebElement owner() {
        return new WebElement(wrappedFrameLocator.owner());
    }

    @Override
    public WebElement getByAltText(String text, GetByAltTextOptions options) {
        return new WebElement(wrappedFrameLocator.getByAltText(text, options.convertTo(FrameLocator.GetByAltTextOptions.class)));
    }

    @Override
    public WebElement getByAltText(String text) {
        return new WebElement(wrappedFrameLocator.getByAltText(text));
    }

    @Override
    public WebElement getByAltText(Pattern text, GetByAltTextOptions options) {
        return new WebElement(wrappedFrameLocator.getByAltText(text, options.convertTo(FrameLocator.GetByAltTextOptions.class)));
    }

    @Override
    public WebElement getByAltText(Pattern text) {
        return new WebElement(wrappedFrameLocator.getByAltText(text));
    }

    @Override
    public WebElement getByLabel(String text, GetByLabelOptions options) {
        return new WebElement(wrappedFrameLocator.getByLabel(text, options.convertTo(FrameLocator.GetByLabelOptions.class)));
    }

    @Override
    public WebElement getByLabel(String text) {
        return new WebElement(wrappedFrameLocator.getByLabel(text));
    }

    @Override
    public WebElement getByLabel(Pattern text, GetByLabelOptions options) {
        return new WebElement(wrappedFrameLocator.getByLabel(text, options.convertTo(FrameLocator.GetByLabelOptions.class)));
    }

    @Override
    public WebElement getByLabel(Pattern text) {
        return new WebElement(wrappedFrameLocator.getByLabel(text));
    }

    @Override
    public WebElement getByPlaceholder(String text, GetByPlaceholderOptions options) {
        return new WebElement(wrappedFrameLocator.getByPlaceholder(text, options.convertTo(FrameLocator.GetByPlaceholderOptions.class)));
    }

    @Override
    public WebElement getByPlaceholder(String text) {
        return new WebElement(wrappedFrameLocator.getByPlaceholder(text));
    }

    @Override
    public WebElement getByPlaceholder(Pattern text, GetByPlaceholderOptions options) {
        return new WebElement(wrappedFrameLocator.getByPlaceholder(text, options.convertTo(FrameLocator.GetByPlaceholderOptions.class)));
    }

    @Override
    public WebElement getByPlaceholder(Pattern text) {
        return new WebElement(wrappedFrameLocator.getByPlaceholder(text));
    }

    @Override
    public WebElement getByRole(AriaRole role, GetByRoleOptions options) {
        return new WebElement(wrappedFrameLocator.getByRole(role, options.convertTo(FrameLocator.GetByRoleOptions.class)));
    }

    @Override
    public WebElement getByRole(AriaRole role) {
        return new WebElement(wrappedFrameLocator.getByRole(role));
    }

    @Override
    public WebElement getByText(String text, GetByTextOptions options) {
        return new WebElement(wrappedFrameLocator.getByText(text, options.convertTo(FrameLocator.GetByTextOptions.class)));
    }

    @Override
    public WebElement getByText(String text) {
        return new WebElement(wrappedFrameLocator.getByText(text));
    }

    @Override
    public WebElement getByText(Pattern text, GetByTextOptions options) {
        return new WebElement(wrappedFrameLocator.getByText(text, options.convertTo(FrameLocator.GetByTextOptions.class)));
    }

    @Override
    public WebElement getByText(Pattern text) {
        return new WebElement(wrappedFrameLocator.getByText(text));
    }

    @Override
    public WebElement getByTitle(String text, GetByTitleOptions options) {
        return new WebElement(wrappedFrameLocator.getByTitle(text, options.convertTo(FrameLocator.GetByTitleOptions.class)));
    }

    @Override
    public WebElement getByTitle(String text) {
        return new WebElement(wrappedFrameLocator.getByTitle(text));
    }

    @Override
    public WebElement getByTitle(Pattern text, GetByTitleOptions options) {
        return new WebElement(wrappedFrameLocator.getByTitle(text, options.convertTo(FrameLocator.GetByTitleOptions.class)));
    }

    @Override
    public WebElement getByTitle(Pattern text) {
        return new WebElement(wrappedFrameLocator.getByTitle(text));
    }

    @Override
    public WebElement locate(String selector) {
        return new WebElement(wrappedFrameLocator.locator(selector));
    }

    @Override
    public WebElement locate(WebElement webElement) {
        return new WebElement(wrappedFrameLocator.locator(webElement.getWrappedLocator()));
    }

    @Override
    public FrameElement locateFrame(String selector) {
        return new FrameElement(locate(selector));
    }
}
