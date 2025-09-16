package solutions.bellatrix.servicenow.components.enums;

import lombok.Getter;

@Getter
public enum UibToolbarButtonLabel {
    HOME("Home"),
    LIST("List"),
    TEAMS("Teams"),
    SCHEDULES("Schedules"),
    DASHBOARD("Dashboard");

    private final String value;

    UibToolbarButtonLabel(String label) {
        this.value = label;
    }

    @Override
    public String toString() {
        return value;
    }
}
