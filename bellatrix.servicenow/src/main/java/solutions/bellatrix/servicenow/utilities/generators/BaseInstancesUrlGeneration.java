package solutions.bellatrix.servicenow.utilities.generators;

import java.util.ArrayList;
import java.util.Map;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.servicenow.contracts.Entity;
import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;

public class BaseInstancesUrlGeneration {
    public static String getBaseUrl() {
        String instance = ConfigurationService.get(ServiceNowProjectSettings.class).getInstance();
        var urlBuilder = new StringBuilder(instance);
        urlBuilder.append(".service-now.com/").insert(0, "https://");

        return urlBuilder.toString();
    }

    public static String getLoginUrl() {
        return getBaseUrl() + "loginSection";
    }

    public static <Table extends ServiceNowTable> String getSnTableViewBaseUrl(Table table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s_list.do", table.getTableName());
    }

    public static <Table extends ServiceNowTable> String getSnNewRecordBaseUrl(Table table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s.do", table.getTableName());
    }

    public static <Table extends ServiceNowTable> String getSnRecordBaseUrl(Table table, String sysId, Map<String, String> params) {
        var queryParams = new ArrayList<String>();
        for (var key :
            params.keySet()) {
            var value = params.get(key);
            queryParams.add(String.format("%s=%s", key, value));
        }
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s%%26%s", table.getTableName(), sysId, String.join("%26", queryParams));
    }

    public static <Table extends ServiceNowTable> String getSnRecordBaseUrl(Table table, String sysId) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s.do%%3Fsys_id%%3D%s", table.getTableName(), sysId);
//      /** isPolaris = false
//        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s", table.getTable(), sysId);
//       **/
    }

    public static <Table extends ServiceNowTable> String getSnBaseUrl(Table table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s", table.getTableName());
    }

    public static <Table extends ServiceNowTable> String getSnBaseUrl(String table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s", table);
    }

    public static <Table extends ServiceNowTable> String getSnRecordBaseUrl(Table table, Entity entity) {
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s", table.getTableName(), entity.getEntityId());
    }

    public static <Table extends ServiceNowTable> String getBasePath(Table table) {
        return "api/now/{api}/" + table.getTableName();
    }

    public static String getBasePath() {
        return "api/now/{api}/{table}";
    }

    public static String getSnNewRecordWorkOrderBaseUrl(String table) {
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do", table);
    }

    public static String getSnRecordViewEamWorkOrderUrl(String table, String sysId) {
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s", table, sysId);
    }

    public static String getSnDashBoardUrl() {
        return getBaseUrl() + "now/nav/ui/classic/params/target/%24pa_dashboard.do";
    }

    public static String getSnInitialLoadUrl() {
        return getBaseUrl() + "now/nav/ui/classic/params/target/ui_page.do";
    }

    public static String getBasePathNoTable() {
        return "api/now/{api}";
    }
}