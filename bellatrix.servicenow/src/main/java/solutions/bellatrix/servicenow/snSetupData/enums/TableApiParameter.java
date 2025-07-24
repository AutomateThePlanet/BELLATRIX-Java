package solutions.bellatrix.servicenow.snSetupData.enums;


import solutions.bellatrix.servicenow.contracts.ApiParameter;

public enum TableApiParameter implements ApiParameter {
    SYSPARM_EXCLUDE_REFERENCE_LINK("sysparm_exclude_reference_link"),
    SURVEY_JOB("survey_job"),
    SYSPARM_LIMIT("sysparm_limit"),
    SYSPARM_FIELDS("sysparm_fields"),
    SYSPARM_DISPLAY_VALUE("sysparm_display_value"),
    SYSPARM_INPUT_DISPLAY_VALUE("sysparm_input_display_value"),
    SYSPARM_QUERY("sysparm_query"),
    SYSPARM_COUNT("sysparm_count");

    private final String value;

    TableApiParameter(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getParameter() {
        return this.getValue();
    }
}