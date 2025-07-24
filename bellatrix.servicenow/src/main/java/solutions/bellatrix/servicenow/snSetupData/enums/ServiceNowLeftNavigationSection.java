package solutions.bellatrix.servicenow.snSetupData.enums;

public enum ServiceNowLeftNavigationSection {
    NAME("Name");
    private final String value;

    ServiceNowLeftNavigationSection(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }
}