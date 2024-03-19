package solutions.bellatrix.playwright.components.common.webelement.options;

/**
 * Absolute copy of the GetByTitleOptions of Locator and FrameLocator.
 * <br>
 * The idea is to combine them under one class and access them more easily.
 */
public class GetByTitleOptions implements Options {
    public Boolean exact;

    public GetByTitleOptions() {
    }

    public GetByTitleOptions setExact(boolean exact) {
        this.exact = exact;
        return this;
    }
}
