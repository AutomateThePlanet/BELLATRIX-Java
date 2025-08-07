package O3_RecordForm.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.components.models.ServiceNowForm;
import solutions.bellatrix.servicenow.components.serviceNow.*;
import solutions.bellatrix.servicenow.models.annotations.snFieldAnnotations.*;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class NewIncidentForm extends ServiceNowForm {
    @FieldLabel("Number")
    @Component(SnString.class)
    @Id("element.incident.number")
    private String number;

    @Required
    @ExternalButtons({"Info"})
    @FieldLabel("Caller")
    @Component(SnReference.class)
    @Id("element.incident.caller_id")
    private String caller;

    @SelectOptions({"-- None --", "Inquiry / Help", "Software", "Hardware", "Network", "Database", "Password Reset"})
    @SelectedOption("Inquiry / Help")
    @FieldLabel("Category")
    @Component(SnChoice.class)
    @Id("element.incident.category")
    private String category;

    @SelectOptions({"-- None --", "Antivirus", "Email", "Internal Application"})
    @SelectedOption("-- None --")
    @FieldLabel("Subcategory")
    @Component(SnChoice.class)
    @Id("element.incident.subcategory")
    private String subcategory;

    @ExternalButtons({"Info"})
    @FieldLabel("Service")
    @Component(SnReference.class)
    @Id("element.incident.business_service")
    private String service;

    @ExternalButtons({"Info"})
    @FieldLabel("Service offering")
    @Component(SnReference.class)
    @Id("element.incident.service_offering")
    private String serviceOffering;

    @ExternalButtons({"Info"})
    @FieldLabel("Configuration item")
    @Component(SnReference.class)
    @Id("element.incident.cmdb_ci")
    private String configurationItem;

    @SelectOptions({"-- None --", "Chat", "Email", "Phone", "Self-service", "Virtual Agent", "Walk-in"})
    @SelectedOption("-- None --")
    @FieldLabel("Channel")
    @Component(SnChoice.class)
    @Id("element.incident.contact_type")
    private String channel;

    @SelectOptions({"New", "In Progress", "On Hold", "Resolved", "Closed", "Canceled"})
    @SelectedOption("New")
    @FieldLabel("State")
    @Component(SnChoice.class)
    @Id("element.incident.state")
    private String state;

    @SelectOptions({"1 - High", "2 - Medium", "3 - Low"})
    @SelectedOption("3 - Low")
    @FieldLabel("Impact")
    @Component(SnChoice.class)
    @Id("element.incident.impact")
    private String impact;

    @SelectOptions({"1 - High", "2 - Medium", "3 - Low"})
    @SelectedOption("3 - Low")
    @FieldLabel("Urgency")
    @Component(SnChoice.class)
    @Id("element.incident.urgency")
    private String urgency;

    @Disabled
    @SelectOptions({"-- None --", "1 - Critical", "2 - High", "3 - Moderate", "4 - Low", "5 - Planning"})
    @SelectedOption("5 - Planning")
    @FieldLabel("Priority")
    @Component(SnChoice.class)
    @Id("element.incident.priority")
    private String priority;

    @ExternalButtons({"Info"})
    @FieldLabel("Assignment group")
    @Component(SnReference.class)
    @Id("element.incident.assignment_group")
    private String assignmentGroup;

    @ExternalButtons({"Info"})
    @FieldLabel("Assigned to")
    @Component(SnReference.class)
    @Id("element.incident.assigned_to")
    private String assignedTo;

    @Required
    @ExternalButtons({"Knowledge", "LightBulb"})
    @FieldLabel("Short description")
    @Component(SnPickList.class)
    @Id("element.incident.short_description")
    private String shortDescription;

    @FieldLabel("Description")
    @Component(SnString.class)
    @Id("element.incident.description")
    private String description;

    @FieldLabel("Related Search")
    @Component(SnSearch.class)
    @Id("cxs_related_search")
    private String relatedSearch;

    @SelectOptions({"Knowledge & Catalog (All)", "Knowledge Articles", "Catalog Items", "Pinned Articles", "Outages", "Open Outages", "Open Incidents", "Resolved Incidents", "Problems", "Open Problems", "Resolved Problems", "Open Changes", "Closed Changes"})
    @SelectedOption("Knowledge & Catalog (All)")
    @FieldLabel("Search Resource Dropdown")
    @Component(SnChoice.class)
    @Id("searchresource_dropdown")
    private String searchResourceDropdown;
}