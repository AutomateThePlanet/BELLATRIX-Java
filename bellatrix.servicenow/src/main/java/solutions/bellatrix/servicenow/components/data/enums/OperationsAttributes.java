package solutions.bellatrix.servicenow.components.data.enums;

public enum OperationsAttributes {
    EQUALS("Equals", "=", "Equals"),
    DOES_NOT_EQUAL("Does not equal", "!=", "DoesNotEqual"),
    IS_LESS_THAN("Is less than", "<", "IsLessThan"),
    IS_GREATER_THAN("Is greater than", ">", "IsGreaterThan"),
    IS_LESS_THAN_OR_EQUAL_TO("Is less than or equal to", "<=", "IsLessThanOrEqualTo"),
    IS_GREATER_THAN_OR_EQUAL_TO("Is greater than or equal to", ">=", "IsGreaterThanOrEqualTo"),
    IS_BLANK("Is blank", "", "IsBlank"),
    IS_NOT_BLANK("Is not blank", "", "IsNotBlank"),
    IS_BETWEEN("Is between", "", "IsBetween"),
    CONTAINS("Contains", "", "Contains"),
    DOES_NOT_CONTAIN("Does not contain", "", "DoesNotContain"),
    STARTS_WITH("Starts with", "", "StartsWith"),
    ENDS_WITH("Ends with", "", "EndsWith");

    private final String value;
    private final String sign;
    private final String nameMethod;

    OperationsAttributes(String value, String sign, String nameMethod) {
        this.value = value;
        this.sign = sign;
        this.nameMethod = nameMethod;
    }

    public String getValue() {
        return this.value;
    }

    public String getSign() {
        return sign;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    @Override
    public String toString() {
        return this.value;
    }
}