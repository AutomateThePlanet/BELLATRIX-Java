package O5_LeftNavigation;

import O4_TableView.data.ProjectTables;
import O5_LeftNavigation.data.ProjectLeftNavigationItem;
import O5_LeftNavigation.data.leftNavigationSection.LeftNavigationSection;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;

public class LeftNavigationTests extends ServiceNowBaseTest {
    @Test
    public void functionalityOptionNotVisible_when_userWithNoPermissionsTryToNavigateToFunctionalityFromLeftNavigationMenu() {
        serviceNowPage.loginSection().login();
        serviceNowPage.impersonateUser("User Impersonate");

        serviceNowPage.filterDataInLeftNavigationPane(ProjectLeftNavigationItem.INCIDENTS);

        var leftNavigationSection = new LeftNavigationSection();
        leftNavigationSection.asserts().assertLeftNavigationItemNotPresentByPath(ProjectLeftNavigationItem.SERVICE_DESK, ProjectLeftNavigationItem.INCIDENTS);
    }

    @Test
    public void functionalityOptionVisible_when_userWithPermissionsSearchToNavigateToFunctionalityFromLeftNavigationMenu() {
        serviceNowPage.loginSection().login();

        serviceNowPage.filterDataInLeftNavigationPane(ProjectLeftNavigationItem.INCIDENTS);

        serviceNowPage.asserts().assertLeftNavigationItemPresentByPath(ProjectLeftNavigationItem.SERVICE_DESK, ProjectLeftNavigationItem.INCIDENTS);
    }

    @Test
    public void correctPageOpen_when_selectOptionInLeftNavigation() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openFromLeftNavigation(ProjectLeftNavigationItem.SELF_SERVICE, ProjectLeftNavigationItem.INCIDENTS);
        serviceNowPage.switchToLastTab();

        serviceNowTableViewPage.asserts().assertPartialUrl(ProjectTables.INCIDENT_TABLE);
    }
}
