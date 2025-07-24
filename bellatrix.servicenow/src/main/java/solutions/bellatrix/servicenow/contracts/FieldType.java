package solutions.bellatrix.servicenow.contracts;

import solutions.bellatrix.servicenow.models.enums.serviceNowFormFieldsDescription.SnFormFieldType;

public interface FieldType {
    SnFormFieldType getFieldType();
}