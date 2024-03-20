package solutions.bellatrix.playwright.components.common.webelement.options;

/**
 * Absolute copy of the GetByPlaceholderOptions of Locator and FrameLocator.
 * <br>
 * The idea is to combine them under one class and access them more easily.
 */
public class GetByPlaceholderOptions implements Options {
    public Boolean exact;

    public GetByPlaceholderOptions() {
    }

    public GetByPlaceholderOptions setExact(boolean exact) {
        this.exact = exact;
        return this;
    }
}
