package solutions.bellatrix.servicenow.utilities.setupData.tables;

import solutions.bellatrix.servicenow.contracts.ServiceNowTable;

public enum Tables implements ServiceNowTable {
    FX_CURRENCY2_INSTANCE("fx_currency2_instance");
    private final String value;

    Tables(String label) {
        this.value = label;
    }

    @Override
    public String getTableName() {
        return value;
    }
}