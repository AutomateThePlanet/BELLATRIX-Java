package solutions.bellatrix.servicenow.components.data.enums;

public enum StyleAlignment {
    LEFT("left"),
    RIGHT("right"),
    CENTER("center");

    private final String value;

    StyleAlignment(String label) {
        this.value = label;
    }

    public String getValue() {
        return "text-align: " + this.value + ";";
    }
}