package O4_TableView.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.servicenow.components.data.ServiceNowForm;
import solutions.bellatrix.servicenow.components.serviceNow.*;
import solutions.bellatrix.servicenow.snSetupData.annotations.snFieldAnnotations.*;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ReservationFormModel extends ServiceNowForm {
    @Required
    @FieldLabel("Title")
    @Component(SnString.class)
    @Id("element.x_nuvo_sfm_v2_reservation.title")
    private String title;

    @FieldLabel("Number")
    @Component(SnString.class)
    @Id("element.x_nuvo_sfm_v2_reservation.number")
    private String number;

    @FieldLabel("Active")
    @Component(SnBoolean.class)
    @Id("element.x_nuvo_sfm_v2_reservation.active")
    private String active;

    @SelectOptions({"Submitted", "Pending Approval", "Rejected", "Reserved", "In Progress", "Closed Cancelled", "Closed Complete", "Closed Incomplete"})
    @SelectedOption("Reserved")
    @FieldLabel("Status")
    @Component(SnChoice.class)
    @Id("element.x_nuvo_sfm_v2_reservation.status")
    private String status;

    @SelectOptions({"Pending", "Open", "Work in Progress", "Closed Complete", "Closed Incomplete", "Closed Skipped"})
    @SelectedOption("Open")
    @FieldLabel("State")
    @Component(SnChoice.class)
    @Id("element.x_nuvo_sfm_v2_reservation.state")
    private String state;

    @FieldLabel("Description")
    @Component(SnString.class)
    @Id("element.x_nuvo_sfm_v2_reservation.description")
    private String description;

    @FieldLabel("Close notes")
    @Component(SnString.class)
    @Id("element.x_nuvo_sfm_v2_reservation.close_notes")
    private String closeNotes;

    @FieldLabel("Is Private")
    @Component(SnBoolean.class)
    @Id("element.x_nuvo_sfm_v2_reservation.is_private")
    private String isPrivate;

    @ExternalButtons({"Info"})
    @FieldLabel("Parent")
    @Component(SnReference.class)
    @Id("element.x_nuvo_sfm_v2_reservation.parent")
    private String parent;

    @FieldLabel("Number Of Participants")
    @Component(SnInteger.class)
    @Id("element.x_nuvo_sfm_v2_reservation.number_of_participants")
    private String numberOfParticipants;

    @ExternalButtons({"Info"})
    @FieldLabel("Origin Reservation")
    @Component(SnReference.class)
    @Id("element.x_nuvo_sfm_v2_reservation.origin_reservation")
    private String originReservation;

    @SelectOptions({"Private", "Requestable", "Reservable"})
    @SelectedOption("Reservable")
    @FieldLabel("Reservation Class")
    @Component(SnChoice.class)
    @Id("element.x_nuvo_sfm_v2_reservation.reservation_class")
    private String reservationClass;

    @Disabled
    @ExternalButtons({"Cross", "Locked"})
    @FieldLabel("Services")
    @Component(SnGlideList.class)
    @Id("element.x_nuvo_sfm_v2_reservation.services")
    private String services;

    @ExternalButtons({"Info"})
    @FieldLabel("Parent Event")
    @Component(SnReference.class)
    @Id("element.x_nuvo_sfm_v2_reservation.parent_event")
    private String parentEvent;

    @FieldLabel("Test_007")
    @Component(SnString.class)
    @Id("element.x_nuvo_sfm_v2_reservation.test_007")
    private String test007;
}