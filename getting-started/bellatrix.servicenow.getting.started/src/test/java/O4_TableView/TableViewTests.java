package O4_TableView;

import O4_TableView.data.ProjectTables;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.pages.serviceNowTableViewPage.ServiceNowTableViewPage;

import java.util.*;

public class TableViewTests extends ServiceNowBaseTest {
    @Test
    public void correctRecordsDisplayedInTableView_when_navigateToTableViewByTableName() {
        serviceNowPage.loginSection().login();
        var serviceNowTableViewPage = new ServiceNowTableViewPage();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);

        serviceNowTableViewPage.asserts().assertPartialUrl(ProjectTables.INCIDENT_TABLE);
    }

    @Test
    public void correctMainHeaderDisplayedInTableView_when_navigateToTableViewByTableName() {
        serviceNowPage.loginSection().login();
        var serviceNowTableViewPage = new ServiceNowTableViewPage();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);

        serviceNowTableViewPage.asserts().assertMainHeader("Incidents");
    }

    @Test
    public void correctColumnsAreVisibleInTableView_when_navigateToTableViewByTableName() {
        serviceNowPage.loginSection().login();
        var serviceNowTableViewPage = new ServiceNowTableViewPage();
        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        List<String> expectedColumns = new ArrayList<>(List.of(
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

        serviceNowTableViewPage.asserts().assertTableLabels(expectedColumns);
    }

    @Test
    public void confirmRecordExistByValueAndColumnName() {
        serviceNowPage.loginSection().login();

        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);

        serviceNowTableViewPage.assertTableColumnContains("Number", "target_record_value");
    }

    @Test
    public void correctRecordsSelected_when_searchByValueInColumn() {
        serviceNowPage.loginSection().login();

        serviceNowTableViewPage.open(ProjectTables.INCIDENT_TABLE);
        serviceNowTableViewPage.searchBy("Number", "target_record_value");

        serviceNowTableViewPage.asserts().assertTableRowsAre(1);
    }
}
