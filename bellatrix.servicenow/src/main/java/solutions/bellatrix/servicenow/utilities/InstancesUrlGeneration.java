package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;

public class InstancesUrlGeneration extends BaseInstancesUrlGeneration {
    public static <Table extends ServiceNowProjectTable> String getUrlForTableApi(Table table) {
        return getBaseUrl() + "api/now/table/" + table.getTable();
    }

    public static String getUrlForDiscoveryEvents() {
        return getOtcs() + "createDiscoveryQueue";
    }

    public static String getOtcs() {
        return getBaseUrl() + "api/x_nuvo_cs/otcs/";
    }

    public static String getUrlForAdditionalApis() {
        return getBaseUrl() + "api/x_nuvo_cs/otcs/getAlertStatus";
    }

    public static String getUrlForSecurityEvents() {
        return getBaseUrl() + "api/x_nuvo_cs/otcs/createSecurityQueue";
    }

    public static String getHomeUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fhome.do%3F";
    }

    public static String getRemLeaseContactsListUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_re_rem_lease_contact_list.do";
    }

    public static String getRemLeaseContactsNavigatedListUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_re_rem_lease_contact_list.do%3Fsysparm_userpref_module";
    }

    public static String getRemLeaseContactUrl(String sysId) {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_re_rem_lease_contact.do%3Fsys_id%3D" + sysId;
    }

    public static String getActivityUrl(String sysId) {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_re_activities.do%3Fsys_id%3D" + sysId;
    }

    public static String getTransactionsListNavigatedUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_tm_transaction_list.do";
    }

    public static String getTasksListNavigatedUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_tm_transaction_task_list.do";
    }

    public static String getTransactionTemplatesListNavigatedUrl() {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_cppm_project_template_list.do";
    }

    public static String getNewTransactionNavigatedUrl() {
        return getTransactionsListNavigatedUrl() + "nav_to.do?uri=%2Fx_nuvo_tm_transaction_list.do";
    }

    public static String getUrlForCreatingTransaction() {
        return getBaseUrl() + "nav_to.do?uri=%2Fx_nuvo_tm_transaction.do";
    }

    public static String getCriticalDatesListNavigatedUrl() {
        return getBaseUrl() + "x_nuvo_re_critical_date_list.do";
    }

    public static String getNewCriticalDateNavigatedUrl() {
        return getBaseUrl() + "x_nuvo_nps_critical_date.do";
    }

    public static String getUrlDeleteAutomationRecords() {
        return getBaseUrl() + "api/x_nuvo_cs/otcs/delete_automation_records";
    }

    public static String getFloorPlanPageByApplicationTable(String applicationTable) {
        return getBaseUrl() + "%s.do#/".formatted(applicationTable);
    }

    public static String getFloorPlanPageByApplicationAndTab(String applicationTable, String tab) {
        return getFloorPlanPageByApplicationTable(applicationTable) + tab;
    }
}