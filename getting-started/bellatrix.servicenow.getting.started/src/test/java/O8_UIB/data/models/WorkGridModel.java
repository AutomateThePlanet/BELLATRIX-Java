package O8_UIB.data.models;

import solutions.bellatrix.web.components.advanced.TableHeader;

public class WorkGridModel {
    @TableHeader(order = 0, name = "Number")
    public String number;

    @TableHeader(order = 1, name = "Priority")
    public String priority;

    @TableHeader(order = 2, name = "Asset/Location?")
    public String assetLocation;

    @TableHeader(order = 2, name = "Asset")
    public String asset;

    @TableHeader(order = 6, name = "Substate")
    public String subState;

    @TableHeader(order = 4, name = "Work Type")
    public String workOrderType;

    @TableHeader(order = 5, name = "Order Summary")
    public String orderSummary;

    @TableHeader(order = 6, name = "State")
    public String state;

    @TableHeader(order = 7, name = "Reported By")
    public String reportedBy;

    @TableHeader(order = 8, name = "Assignment Group")
    public String assignmentGroup;

    @TableHeader(order = 9, name = "Assigned To")
    public String assignedTo;
}
