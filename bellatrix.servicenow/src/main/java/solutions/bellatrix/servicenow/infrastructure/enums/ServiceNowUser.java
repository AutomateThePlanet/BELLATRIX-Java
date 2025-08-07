package solutions.bellatrix.servicenow.infrastructure.enums;

public enum ServiceNowUser {
    NONE(""),
    USER_NAME("User Name");

    private final String value;

    ServiceNowUser(String label) {
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