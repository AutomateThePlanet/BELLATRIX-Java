package solutions.bellatrix.servicenow.components.enums;

public enum InputRole {
    COMBOBOX("combobox"),
    SPINBUTTON("spinbutton"),
    TEXTBOX("textbox");

    private final String role;

    InputRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}