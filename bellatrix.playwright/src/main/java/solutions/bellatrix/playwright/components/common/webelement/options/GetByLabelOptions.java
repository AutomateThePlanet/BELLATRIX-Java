package solutions.bellatrix.playwright.components.common.webelement.options;

/**
 * Absolute copy of the GetByLabelOptions of Locator and FrameLocator.
 * <br>
 * The idea is to combine them under one class and access them more easily.
 */
public class GetByLabelOptions implements Options {
    public Boolean exact;

    public GetByLabelOptions() {
    }

    public GetByLabelOptions setExact(boolean exact) {
        this.exact = exact;
        return this;
    }
}
