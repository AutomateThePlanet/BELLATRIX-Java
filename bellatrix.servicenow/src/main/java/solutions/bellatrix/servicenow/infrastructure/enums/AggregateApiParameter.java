package solutions.bellatrix.servicenow.infrastructure.enums;

import solutions.bellatrix.servicenow.contracts.ApiParameter;

public enum AggregateApiParameter implements ApiParameter {
    SYSPARM_COUNT("sysparm_count"),
    SYSPARM_SUM_FIELDS("sysparm_sum_fields"),
    SYSPARM_QUERY("sysparm_query");

    private final String value;

    AggregateApiParameter(String label) {
        this.value = label;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getParameter() {
        return this.value;
    }
}