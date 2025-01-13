package solutions.bellatrix.playwright.components.common.webelement.options;

/**
 * Absolute copy of the GetByTextOptions of Locator and FrameLocator.
 * <br>
 * The idea is to combine them under one class and access them more easily.
 */
public class GetByTextOptions implements Options {
    public Boolean exact;

    public GetByTextOptions() {
    }

    public GetByTextOptions setExact(boolean exact) {
        this.exact = exact;
        return this;
    }
}
