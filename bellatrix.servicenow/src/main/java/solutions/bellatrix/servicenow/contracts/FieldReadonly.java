package solutions.bellatrix.servicenow.contracts;

public interface FieldReadonly extends FieldLabel, FieldType {
    Boolean isReadonly();
}