package solutions.bellatrix.servicenow.utilities.generators;

import solutions.bellatrix.servicenow.contracts.ServiceNowTable;

public class InstancesUrlGeneration extends BaseInstancesUrlGeneration {
    public static <Table extends ServiceNowTable> String getUrlForTableApi(Table table) {
        return getBaseUrl() + "api/now/table/" + table.getTableName();
    }

    public static String getHomeUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fhome.do%3F";
    }
}