package O4_TableView.data;

import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;

public enum ProjectTables implements ServiceNowProjectTable {
    INCIDENT_TABLE("incident");

    private final String table;

    ProjectTables(String table) {
        this.table = table;
    }
    @Override
    public String getTable() {
        return table;
    }
}
