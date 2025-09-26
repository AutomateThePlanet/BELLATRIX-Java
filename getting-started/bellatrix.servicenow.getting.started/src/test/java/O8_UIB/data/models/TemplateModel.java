package O8_UIB.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.components.models.ServiceNowForm;
import solutions.bellatrix.servicenow.components.uiBuilder.RecordInput;
import solutions.bellatrix.servicenow.components.uiBuilder.RecordReference;
import solutions.bellatrix.servicenow.components.uiBuilder.UiBuilderRecordChoice;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.*;

@Data
@SuperBuilder
@NoArgsConstructor
public class TemplateModel extends ServiceNowForm {
    @Disabled
    @FieldLabel("Number")
    @UibComponent(RecordInput.class)
    @FieldLocator("number")
    private String number;

    @SelectOptions({"Asset", "Location", "Space"})
    @SelectedOption("Asset")
    @FieldLabel("Asset/Location?")
    @UibComponent(UiBuilderRecordChoice.class)
    @FieldLocator("asset_location")
    private String assetLocation;

    @Disabled
    @Required
    @FieldLabel("Asset")
    @UibComponent(RecordReference.class)
    @FieldLocator("asset")
    private String asset;

    @Required
    @FieldLabel("Work Type")
    @UibComponent(RecordReference.class)
    @FieldLocator("work_type")
    private String workType;

    @Required
    @FieldLabel("Assigned To")
    @UibComponent(RecordReference.class)
    @FieldLocator("assigned_to")
    private String assignedTo;

    @SelectOptions({"-- None --", "1 - Critical", "2 - High", "3 - Moderate", "4 - Low", "5 - Planning"})
    @SelectedOption("Low")
    @FieldLabel("Priority")
    @UibComponent(UiBuilderRecordChoice.class)
    @FieldLocator("priority")
    private String priority;

    @Required
    @FieldLabel("Assignment Group")
    @UibComponent(RecordReference.class)
    @FieldLocator("assignment_group")
    private String assignmentGroup;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAssignmentGroup(String assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }
}