package solutions.bellatrix.servicenow.contracts;

import solutions.bellatrix.servicenow.models.enums.serviceNowFormFieldsDescription.InputType;

public interface SnFormField extends FieldLabel, FieldType {
    Boolean isRequired();

    InputType getInputType();
}