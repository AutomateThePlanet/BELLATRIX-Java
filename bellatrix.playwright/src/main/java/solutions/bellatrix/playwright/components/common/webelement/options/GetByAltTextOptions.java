package solutions.bellatrix.playwright.components.common.webelement.options;

/**
 * Absolute copy of the GetByAltTextOptions of Locator and FrameLocator.
 * <br>
 * The idea is to combine them under one class and access them more easily.
 */
public class GetByAltTextOptions implements Options {
    public Boolean exact;

    public GetByAltTextOptions() {
    }

    public GetByAltTextOptions setExact(boolean exact) {
        this.exact = exact;
        return this;
    }
}
