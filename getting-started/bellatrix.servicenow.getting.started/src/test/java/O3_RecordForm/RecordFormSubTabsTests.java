package O3_RecordForm;

import O3_RecordForm.data.ProjectTables;
import O3_RecordForm.data.recordPage.IncidentRecordPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.utilities.BaseInstancesUrlGeneration;

import java.util.ArrayList;
import java.util.List;

public class RecordFormSubTabsTests extends ServiceNowBaseTest {
    @Test
    public void correctTabsRecordExist_when_openRecord() {
        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        serviceNowTableViewPage.openRecordInfoFromMainTable(1);

        var recordPage = new IncidentRecordPage();

            List<String> expectedTabs = new ArrayList<>(List.of(
                "Task SLAs",
                "Affected CIs",
                "Impacted Services/CIs",
                "Child Incidents"));

        recordPage.asserts().assertBottomTablesTabs(expectedTabs);
    }

    @Test
    public void subTabDetailsOpen_when_clickOnTab() {
        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        serviceNowTableViewPage.openRecordInfoFromMainTable(1);

        var recordPage = new IncidentRecordPage();
        recordPage.map().tabByLabel("Child Incidents").click();

        List<String> expectedTabColumns = new ArrayList<>(List.of(
                "Number",
                "Opened",
                "Short description",
                "Caller",
                "Priority",
                "State",
                "Category",
                "Assignment group",
                "Assigned to",
                "Updated",
                "Updated by"));

        recordPage.asserts().assertInnerTableColumns("Child Incidents",expectedTabColumns);
    }

    @Test
    public void newRecordFormOpen_when_clickNewButtonForTheSelectedBottomTableTab() {
        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        serviceNowTableViewPage.openRecordInfoFromMainTable(1);

        var recordPage = new IncidentRecordPage();
        recordPage.map().tabByLabel("Child Incidents").click();
        recordPage.map().newButtonByHeading("Child Incidents").click();

        recordPage.asserts().assertRecordMainHeader("Incident");
        recordPage.asserts().assertRecordHeader("New record");
    }
}