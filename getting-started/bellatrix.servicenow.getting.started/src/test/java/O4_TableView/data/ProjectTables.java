package O4_TableView.data;

import solutions.bellatrix.servicenow.contracts.ServiceNowTable;

public enum ProjectTables implements ServiceNowTable {
    INCIDENT_TABLE("incident");
    private final String value;

    ProjectTables(String table) {
        this.value = table;
    }

    @Override
    public String getTableName() {
        return value;
    }
}
