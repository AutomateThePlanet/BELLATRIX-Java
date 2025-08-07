package solutions.bellatrix.servicenow.infrastructure.enums;

import lombok.Getter;

@Getter
public enum ServiceNowWorkspaces {
    WORKSPACE_NAME("Workspace name");

    private final String value;

    ServiceNowWorkspaces(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return this.value;
    }
}