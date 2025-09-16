package solutions.bellatrix.servicenow.components.enums;

import lombok.Getter;

@Getter
public enum UibToolbarButtonLabel {
    HOME("Home"),
    LIST("List"),
    TEAMS("Teams"),
    SCHEDULES("Schedules"),
    DASHBOARD("Dashboard"),
    WORK_ORDER_DASHBOARD("Work Order Dashboard"),
    ASSET_DASHBOARD("Asset Dashboard"),
    PLANNING_AND_SCHEDULING("Planning and Scheduling"),
    PARTS_AND_INVENTORY("Parts and Inventory"),
    RECEIPTS("Receipts");

    private final String value;

    UibToolbarButtonLabel(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return value;
    }
}
