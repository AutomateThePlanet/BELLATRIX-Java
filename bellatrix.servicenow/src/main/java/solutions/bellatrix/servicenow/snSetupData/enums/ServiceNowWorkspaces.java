package solutions.bellatrix.servicenow.snSetupData.enums;

import lombok.Getter;

@Getter
public enum ServiceNowWorkspaces {
    ASSET_MANAGEMENT_REAL_ESTATE("Asset Management - Real Estate"),
    CMDB_WORKSPACE("CMDB Workspace"),
    MANAGER_WORKSPACE("Manager Workspace"),
    PLATFORM_ANALYTICS_WORKSPACE("Platform Analytics Workspace"),
    PORTFOLIO_MANAGEMENT("Portfolio Management"),
    SERVICE_OPERATIONS_WORKSPACE("Service Operations Workspace"),
    TECHNICIAN_WORKSPACE("Technician Workspace");

    private final String value;

    ServiceNowWorkspaces(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}