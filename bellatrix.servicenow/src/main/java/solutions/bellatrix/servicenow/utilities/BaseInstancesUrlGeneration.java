package solutions.bellatrix.servicenow.utilities;

import java.util.ArrayList;
import java.util.Map;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.servicenow.contracts.Entity;
import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;
import solutions.bellatrix.servicenow.data.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;

public class BaseInstancesUrlGeneration {
    public static String getBaseUrl() {
        String instance = ConfigurationService.get(ServiceNowProjectSettings.class).getInstance();
        var urlBuilder = new StringBuilder(instance);
        urlBuilder.append(".service-now.com/").insert(0, "https://");

        return urlBuilder.toString();
    }

    public static String getLoginUrl() {
        return getBaseUrl() + "login";
    }

    public static <Table extends ServiceNowProjectTable> String getSnTableViewBaseUrl(Table table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s_list.do", table.getTable());
    }

    public static <Table extends ServiceNowProjectTable> String getSnNewRecordBaseUrl(Table table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s.do", table.getTable());
    }

    public static <Table extends ServiceNowProjectTable> String getSnRecordBaseUrl(Table table, String sysId, Map<String, String> params) {
        var queryParams = new ArrayList<String>();
        for (var key :
            params.keySet()) {
            var value = params.get(key);
            queryParams.add(String.format("%s=%s", key, value));
        }
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s%%26%s", table.getTable(), sysId, String.join("%26", queryParams));
    }

    public static <Table extends ServiceNowProjectTable> String getSnRecordBaseUrl(Table table, String sysId) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s.do%%3Fsys_id%%3D%s", table.getTable(), sysId);
//      /** isPolaris = false
//        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s", table.getTable(), sysId);
//       **/
    }

    public static <Table extends ServiceNowProjectTable> String getSnBaseUrl(Table table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s", table.getTable());
    }

    public static <Table extends ServiceNowProjectTable> String getSnBaseUrl(String table) {
        return getBaseUrl() + String.format("now/nav/ui/classic/params/target/%s", table);
    }

    public static <Table extends ServiceNowProjectTable> String getSnRecordBaseUrl(Table table, ApiEntity apiEntity) {
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s", table.getTable(), apiEntity.getEntityId());
    }

    // todo: preferable way
    public static <Table extends ServiceNowProjectTable> String getSnRecordBaseUrl(Table table, Entity entity) {
        return getBaseUrl() + String.format("nav_to.do?uri=%%2F%s.do%%3Fsys_id%%3D%s", table.getTable(), entity.getEntityId());
    }


    public static <Table extends ServiceNowProjectTable> String getBasePath(Table table) {
        return "api/now/{api}/" + table.getTable();
    }

    public static String getBasePath() {
        return "api/now/{api}/{table}";
    }

    protected static <Entity extends ApiEntity> String appendEntityId(String url, Entity entity) {
        return url + "&entityID=" + entity.getSysId();
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

    protected static String getUibBaseUrl() {
        return getBaseUrl() + "x/nuvo/otcs/";
    }

    public static String getUibHomeUrl() {
        return getUibBaseUrl() + "home";
    }

    public static <Table extends ServiceNowProjectTable> String getUibRecordBaseUrl(Table table, Entity entity) {
        return getUibBaseUrl() + String.format("record/%s/%s", table.getTable(), entity.getEntityId());
    }

    public static <Table extends ServiceNowProjectTable> String getUibNewRecordBaseUrl(Table table) {
        return getUibBaseUrl() + String.format("record/%s/-1", table.getTable());
    }
}