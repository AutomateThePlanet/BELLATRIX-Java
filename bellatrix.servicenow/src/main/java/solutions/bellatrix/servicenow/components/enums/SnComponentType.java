package solutions.bellatrix.servicenow.components.enums;

public enum SnComponentType {
    CHOICE("choice"),
    DATE("date"),
    DATE_TIME("date_time"),
    DOCUMENT_ID("document_id"),
    REFERENCE("reference"),
    STRING("string"),
    TABLE_NAME("table_name"),
    PICK_LIST("pick_list"),
    BOOLEAN("boolean"),
    FLOAT("float"),
    INTEGER("integer"),
    PERCENT_COMPLETE("percent_complete"),
    CURRENCY_2("currency2"),
    GLIDE_LIST("glide_list"),
    GLIDE_TIME("glide_time"),
    DURATION("glide_duration"),
    HTML("html"),
    CURRENCY("currency2"),
    JOURNAL_INPUT("journal_input"),
    CURRENCY2("currency2"),
    FIELD_NAME("field_name"),
    SN_CONDITION_ROW("condition_row"),
    SEARCH("search");

    private final String componentType;

    SnComponentType(String componentType) {
        this.componentType = componentType;
    }

    @Override
    public String toString() {
        return componentType;
    }
}