package O8_UIB.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.components.models.ServiceNowForm;

import solutions.bellatrix.servicenow.components.uiBuilder.*;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.*;

@Data
@SuperBuilder
@NoArgsConstructor
public class RequestItemModel extends ServiceNowForm {
    @FieldLabel("Number")
    @UibComponent(RecordInput.class)
    @FieldLocator("number")
    private String number;

    @FieldLabel("Item")
    @UibComponent(RecordReference.class)
    @FieldLocator("cat_item_input")
    private String item;

    @FieldLabel("Request")
    @UibComponent(RecordReference.class)
    @FieldLocator("request_input")
    private String request;

    @FieldLabel("Requested for")
    @UibComponent(RecordReference.class)
    @FieldLocator("requested_for_input")
    private String requestedFor;

    @Disabled
    @FieldLabel("Due date")
    @UibComponent(RecordDateTimeInput.class)
    @FieldLocator("due_date-date")
    private String dueDate;

    @FieldLabel("Configuration item")
    @UibComponent(RecordReference.class)
    @FieldLocator("configuration_item_input")
    private String configurationItem;

    @FieldLabel("Watch list")
    @UibComponent(RecordReference.class)
    @FieldLocator("_input")
    private String watchList;

    @Disabled
    @FieldLabel("Opened")
    @UibComponent(RecordDateTimeInput.class)
    @FieldLocator("opened_at-date")
    private String opened;

    @FieldLabel("Opened by")
    @UibComponent(RecordReference.class)
    @FieldLocator("opened_by_input")
    private String openedBy;

    @FieldLabel("Stage")
    @UibComponent(RecordInput.class)
    @FieldLocator("")
    private String stage;

    @FieldLabel("Quantity")
    @UibComponent(RecordInput.class)
    @FieldLocator("quantity")
    private String quantity;

    @Disabled
    @FieldLabel("Estimated delivery")
    @UibComponent(RecordDateTimeInput.class)
    @FieldLocator("estimated_delivery-date")
    private String estimatedDelivery;

    @Disabled
    @FieldLabel("Backordered")
    @UibComponent(RecordCheckbox.class)
    @FieldLocator("backordered")
    private String backordered;

    @FieldLabel("Order Guide")
    @UibComponent(RecordReference.class)
    @FieldLocator("order_guide_input")
    private String orderGuide;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}