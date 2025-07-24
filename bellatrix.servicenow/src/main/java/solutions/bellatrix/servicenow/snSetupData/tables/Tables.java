package solutions.bellatrix.servicenow.snSetupData.tables;

import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;

public enum Tables implements ServiceNowProjectTable {
    FX_CURRENCY2_INSTANCE("fx_currency2_instance");
    private final String value;

    Tables(String label) {
        this.value = label;
    }

    @Override
    public String getTable() {
        return value;
    }
}