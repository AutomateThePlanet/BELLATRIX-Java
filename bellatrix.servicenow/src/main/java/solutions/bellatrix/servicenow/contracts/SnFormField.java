package solutions.bellatrix.servicenow.contracts;

import solutions.bellatrix.servicenow.models.enums.InputType;

public interface SnFormField extends FieldLabel, FieldType {
    Boolean isRequired();

    InputType getInputType();
}