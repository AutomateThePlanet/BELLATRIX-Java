package solutions.bellatrix.servicenow.components.data.enums;

public enum AdditionalView {
    OT_SECURITY("CyberSecurity"),
    WAREHOUSE("Warehouse"),
    ASSET_USER("AssetUser");

    private final String value;

    AdditionalView(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}