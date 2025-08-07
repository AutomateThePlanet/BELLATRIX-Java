package solutions.bellatrix.servicenow.infrastructure.enums;

public enum ServiceNowApiPathSegment {
    TABLE_API("table"),
    AGGREGATE_API("stats"),
    ATTACHMENT_API("attachment");

    private final String value;

    ServiceNowApiPathSegment(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}