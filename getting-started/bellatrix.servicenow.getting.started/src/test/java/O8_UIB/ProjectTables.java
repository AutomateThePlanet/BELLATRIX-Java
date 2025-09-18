package O8_UIB;

import solutions.bellatrix.servicenow.contracts.ServiceNowTable;

public enum ProjectTables implements ServiceNowTable {
    CATALOG_ITEM_TABLE("sc_cat_item");
    private final String value;

    ProjectTables(String table) {
        this.value = table;
    }

    @Override
    public String getTableName() {
        return value;
    }
}