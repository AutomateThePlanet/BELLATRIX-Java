package solutions.bellatrix.servicenow.snSetupData.enums;

import lombok.Getter;

@Getter
public enum ServiceNowMenuItems {
    All("All"),
    FAVORITES("Favorites"),
    HISTORY("History"),
    WORKSPACES("Workspaces"),
    ADMIN("Admin");

    private final String value;

    ServiceNowMenuItems(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}