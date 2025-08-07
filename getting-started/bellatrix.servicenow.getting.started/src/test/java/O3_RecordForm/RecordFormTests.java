package O3_RecordForm;

import O3_RecordForm.data.NewIncidentForm;
import O3_RecordForm.data.ProjectTables;
import O3_RecordForm.data.recordPage.IncidentRecordPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.utilities.generators.BaseInstancesUrlGeneration;

public class RecordFormTests extends ServiceNowBaseTest {
    @Test
    public void correctPageOpen_when_selectFirstRecord() {
        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        serviceNowTableViewPage.openRecordInfoFromMainTable(1);

        var recordPage = new IncidentRecordPage();
        recordPage.asserts().assertRecordMainHeader("Incident");
    }

    @Test
    public void openRecordFormBySysId () {
        serviceNowPage.loginSection().login();
        var recordUrl =BaseInstancesUrlGeneration.getSnRecordBaseUrl(ProjectTables.INCIDENT_TABLE,"57af7aec73d423002728660c4cf6a71c");
        serviceNowPage.navigateToPage(recordUrl);

        var recordPage = new IncidentRecordPage();
        serviceNowPage.switchToInnerFrame();
        recordPage.asserts().assertRecordMainHeader("Incident");
        serviceNowPage.browser().assertLandedOnPage(BaseInstancesUrlGeneration.getSnRecordBaseUrl(ProjectTables.INCIDENT_TABLE, "57af7aec73d423002728660c4cf6a71c"));
    }

    @Test
    public void newRecordFormOpen_when_clickNewButton () {
        serviceNowPage.loginSection().login();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        serviceNowTableViewPage.clickOnNewButton();

        var newRecordPage = new IncidentRecordPage();
        var mainFormModel = NewIncidentForm.builder().build();
        newRecordPage.asserts().validateFormState(mainFormModel, newRecordPage.map().mainForm(), false);
    }
}
