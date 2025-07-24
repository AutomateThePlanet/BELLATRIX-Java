package solutions.bellatrix.servicenow.snSetupData.enums;

public enum ServiceNowView {
    ASSET_USER("AssetUser"),
    CALIBRATION("Calibration"),
    DEFAULT_VIEW("Default view"),
    EAM_SERVICE_PORTAL("EAM Service Portal"),
    FSM_STATUS_POPUP("FSM Status Popup"),
    NEW_WORK_ORDER("New Work Order"),
    OT_SECURITY("OT Security"),
    PUBLIC("Public");

    private final String value;

    ServiceNowView(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }
}