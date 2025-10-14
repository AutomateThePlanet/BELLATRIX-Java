package O8_UIB.data.models;

import solutions.bellatrix.web.components.advanced.TableHeader;

public class ManagerWorkModel {
    @TableHeader(order = 0, name = "Record Preview")
    public String recordPreview;

    @TableHeader(order = 1, name = "Row Selection")
    public String rowSelection;

    @TableHeader(order = 2, name = "Number")
    public String number;

    @TableHeader(order = 3, name = "Priority")
    public String priority;

    @TableHeader(order = 4, name = "Work Order Type")
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

    @TableHeader(order = 10, name = "Work Location")
    public String workLocation;

    @TableHeader(order = 11, name = "Floor")
    public String floor;

    @TableHeader(order = 12, name = "Site")
    public String site;
}
