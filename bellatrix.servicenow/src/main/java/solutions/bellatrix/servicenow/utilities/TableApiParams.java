package solutions.bellatrix.servicenow.utilities;


import static solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter.SYSPARM_DISPLAY_VALUE;
import static solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter.SYSPARM_EXCLUDE_REFERENCE_LINK;
import static solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter.SYSPARM_FIELDS;
import static solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter.SYSPARM_LIMIT;
import static solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter.SYSPARM_QUERY;

import com.google.gson.annotations.SerializedName;
import solutions.bellatrix.servicenow.snSetupData.enums.TableApiParameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.SneakyThrows;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class TableApiParams {
    private static String sysparmQuery;
    private final LinkedHashMap<TableApiParameter, Object> params;

    public TableApiParams() {
        params = new LinkedHashMap<>();
        sysparmQuery = "";
    }

    private static void addSysparm(String query) {
        if (!sysparmQuery.isEmpty()) {
            sysparmQuery += "^";
        }

        sysparmQuery += query;
    }

    private static void addOrSysparm(String query) {
        if (!sysparmQuery.isEmpty()) {
            sysparmQuery += "^OR";
        }

        sysparmQuery += query;
    }

    public LinkedHashMap<TableApiParameter, Object> build() {
        params.put(SYSPARM_QUERY, sysparmQuery);

        return this.params;
    }

    public TableApiParams excludeReferenceLink() {
        params.put(SYSPARM_EXCLUDE_REFERENCE_LINK, true);

        return this;
    }

    public TableApiParams addSysparmEqualQuery(String field, Object value) {
        addSysparmQuery(field + "=" + value.toString());

        return this;
    }

    @SneakyThrows
    public TableApiParams addCustomParam(Class targetObject, String targetObjectProperty, Object targetObjectPropertyValue) {
        var field = targetObject.getField(targetObjectProperty);
        var annotationValue = field.getAnnotation(SerializedName.class).value();
        addSysparmEqualQuery(annotationValue, targetObjectPropertyValue);
        return this;
    }

    @SneakyThrows
    public TableApiParams addCustomParams(Class targetObject, Object... params) {
        var targetObjectsFields = targetObject.getFields();
        for (int i = 0; i < params.length; i += 2) {
            var propertyName = String.valueOf(params[i]);
            var targetValue = String.valueOf(params[i + 1]);
            var field = targetObject.getField(propertyName);
            var annotationValue = field.getAnnotation(SerializedName.class).value();
            addSysparmEqualQuery(annotationValue, targetValue);
        }
        return this;
    }

    public TableApiParams addSysparmContainsQuery(String field, Object value) {
        addSysparmQuery(field + "LIKE" + value.toString());

        return this;
    }

    public TableApiParams addSysparmNotEqualQuery(String field, Object value) {
        addSysparmQuery(field + "!=" + value.toString());

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public TableApiParams addSysparmQuery(String query) {
        addSysparm(query);

        return this;
    }

    public TableApiParams createdLastMinute() {
        String query = "sys_created_onONLast minute@javascript:gs.beginningOfLastMinute()@javascript:gs.endOfLastMinute()";
        addSysparm(query);

        return this;
    }

    public TableApiParams setLimit() {
        return setLimit(1);
    }

    public TableApiParams setLimit(Integer limit) {
        params.put(SYSPARM_LIMIT, limit);

        return this;
    }

    public TableApiParams orderBySysCreatedOnDesc() {
        String query = "ORDERBYDESCsys_created_on";
        addSysparm(query);

        return this;
    }

    public TableApiParams orderBySysUpdatedOnDesc() {
        String query = "ORDERBYDESCsys_updated_on";
        addSysparm(query);

        return this;
    }

    public TableApiParams orderByDesc(String field) {
        String query = "ORDERBYDESC" + field;
        addSysparm(query);

        return this;
    }

    public TableApiParams orderBy(String field) {
        String query = "ORDERBY" + field;
        addSysparm(query);

        return this;
    }

    public TableApiParams numberEquals(String number) {
        addSysparmQuery("number=" + number);

        return this;
    }

    public TableApiParams queueEquals(String number) {
        addSysparmQuery("queue=" + number);

        return this;
    }

    public TableApiParams multiMatchEquals(String number) {
        addSysparmQuery("x_nuvo_cs_multiple_match=" + number);

        return this;
    }

    public TableApiParams eventIdEquals(String eventId) {
        addSysparmQuery("event_id=" + eventId);

        return this;
    }

    public TableApiParams addSysParmFields(String... sysparmFields) {
        addSysParmFields(Arrays.stream(sysparmFields).toList());

        return this;
    }

    public TableApiParams addSysParmFields(List<String> sysparmFields) {
        params.put(SYSPARM_FIELDS, String.join(",", sysparmFields));

        return this;
    }

    public TableApiParams addOrSysparmEqualQuery(String field, Object value) {
        addOrSysparm(field + "=" + value.toString());

        return this;
    }

    public TableApiParams displayValue() {
        params.put(SYSPARM_DISPLAY_VALUE, true);

        return this;
    }

    public TableApiParams addFieldIsNotEmpty(String field) {
        addSysparmQuery(field + "ISNOTEMPTY");

        return this;
    }

    public TableApiParams isOneOf(String field, List<String> entities) {
        addSysparmQuery(field + "IN" + String.join(",", entities));

        return this;
    }

    public TableApiParams addDateFieldIsCurrentYear(String field) {
        addSysparmQuery(field + "ON" + "This year@javascript:gs.beginningOfThisYear()@javascript:gs.endOfThisYear()");

        return this;
    }

    public TableApiParams startWith(String queryParameter, String startingLetters) {
        String query = String.format("%sSTARTSWITH%s", queryParameter, startingLetters);
        addSysparm(query);

        return this;
    }

    public TableApiParams nameEquals(String name) {
        addSysparmQuery("name=" + name);

        return this;
    }
}