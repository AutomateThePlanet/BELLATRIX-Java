package solutions.bellatrix.servicenow.models.enums.serviceNowFormFieldsDescription;

public enum InputType {
    SEARCH("search"),
    CHECKBOX("checkbox"),
    DECIMAL("decimal"),
    TEXT("text");

    private final String value;

    InputType(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }
}