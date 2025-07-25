package O5_LeftNavigation.data;

import solutions.bellatrix.servicenow.contracts.ServiceNowLeftNavigationItem;

public enum ProjectLeftNavigationItem implements ServiceNowLeftNavigationItem {
    SELF_SERVICE("Self-Service"),
    INCIDENTS("Incidents"),
    SERVICE_DESK("Service Desk");

    private final String value;

    ProjectLeftNavigationItem(String label) {
        this.value = label;
    }

    @Override
    public String getText() {
        return this.value;
    }
}