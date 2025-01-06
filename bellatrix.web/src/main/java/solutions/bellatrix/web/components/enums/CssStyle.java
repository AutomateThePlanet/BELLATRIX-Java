package solutions.bellatrix.web.components.enums;

public enum CssStyle {
    BACKGROUND_COLOR("background-color"),
    BORDER_COLOR("border-color"),
    BORDER("border"),
    BORDER_RIGHT("border-right"),
    COLOR("color"),
    FONT_FAMILY("font-family"),
    FONT_WEIGHT("font-weight"),
    FONT_SIZE("font-size"),
    TEXT_ALIGN("text-align"),
    VERTICAL_ALIGN("vertical-align"),
    LINE_HEIGHT("line-height"),
    LETTER_SPACING("letter-spacing"),
    MARGIN_TOP("margin-top"),
    MARGIN_BOTTOM("margin-bottom"),
    MARGIN_LEFT("margin-left"),
    MARGIN_RIGHT("margin-right"),
    PADDING_TOP("padding-top"),
    PADDING_BOTTOM("padding-bottom"),
    PADDING_LEFT("padding-left"),
    PADDING_RIGHT("padding-right"),
    POSITION("position"),
    HEIGHT("height"),
    WIDTH("width");

    private final String style;

    CssStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return this.style;
    }
}
