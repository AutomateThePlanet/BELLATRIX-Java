package solutions.bellatrix.servicenow.infrastructure.enums;

import lombok.Getter;

@Getter
public enum ServiceNowWorkspaces {
    ASSET_WORKSPACE("Asset Workspace"),
    CUSTOM_WORKSPACE_NAME("Custom workspace name");

    private final String value;

    ServiceNowWorkspaces(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}