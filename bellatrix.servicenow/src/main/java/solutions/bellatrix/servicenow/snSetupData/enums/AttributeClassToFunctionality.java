package solutions.bellatrix.servicenow.snSetupData.enums;

public enum AttributeClassToFunctionality {
    BACK("M20 11H7.83l5.59-5.59L12 4l-8 8 8 8"),
    COLUMN_CHOOSER("M8"),
    DOWNLOAD("M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z"),
    ADD("M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"),
    DELETE("M6"),
    CLOSE("M19"),
    KEBAB_MENU("M12 8c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2"),
    PRINT("M19 8H5c-1.66 0-3 1.34-3 3v4c0 1.1.9 2 2 2h2v2c0"),
    INFO("M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48"),
    REFRESH("M17"),
    HELP("M11"),
    EDIT("M3"),
    OPEN_MAP("M18");

    private final String value;

    AttributeClassToFunctionality(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}