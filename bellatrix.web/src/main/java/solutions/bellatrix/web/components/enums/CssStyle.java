package solutions.bellatrix.web.components.enums;

public enum CssStyle {
    BACKGROUND_COLOR("background-color"),
    COLOR("color");

    private final String style;

    CssStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return this.style;
    }
}
