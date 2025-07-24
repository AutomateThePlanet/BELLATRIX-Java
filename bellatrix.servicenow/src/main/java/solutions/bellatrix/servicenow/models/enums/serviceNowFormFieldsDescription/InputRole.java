package solutions.bellatrix.servicenow.models.enums.serviceNowFormFieldsDescription;

public enum InputRole {
    NONE(""),
    COMBOBOX("combobox"),
    SELECT("select");

    private final String value;

    InputRole(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }
}