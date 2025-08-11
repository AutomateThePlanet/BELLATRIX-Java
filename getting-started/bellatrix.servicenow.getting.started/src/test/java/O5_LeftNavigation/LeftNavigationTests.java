package O5_LeftNavigation;

import O4_TableView.data.ProjectTables;
import O5_LeftNavigation.data.ProjectLeftNavigationItem;
import O5_LeftNavigation.data.leftNavigationSection.LeftNavigationSection;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowMenuItems;

public class LeftNavigationTests extends ServiceNowBaseTest {
    @Test
    public void optionNotVisible_when_nonAuthorizedUserSearchInLeftNavigation() {
        serviceNowPage.loginSection().login();
        serviceNowPage.impersonateUser("User Impersonate");
        serviceNowPage.clickMenuItem(ServiceNowMenuItems.All);

        serviceNowPage.filterDataInLeftNavigationPane(ProjectLeftNavigationItem.INCIDENTS);

        var leftNavigationSection = new LeftNavigationSection();
        leftNavigationSection.asserts().assertLeftNavigationItemNotPresentByPath(ProjectLeftNavigationItem.SERVICE_DESK, ProjectLeftNavigationItem.INCIDENTS);
    }

    @Test
    public void optionVisible_when_authorizedUserSearchInLeftNavigation() {
        serviceNowPage.loginSection().login();
        serviceNowPage.clickMenuItem(ServiceNowMenuItems.All);

        serviceNowPage.filterDataInLeftNavigationPane(ProjectLeftNavigationItem.INCIDENTS);

        serviceNowPage.asserts().assertLeftNavigationItemPresentByPath(ProjectLeftNavigationItem.SERVICE_DESK, ProjectLeftNavigationItem.INCIDENTS);
    }

    @Test
    public void correctPageOpen_when_selectOptionInLeftNavigation() {
        serviceNowPage.loginSection().login();
        serviceNowPage.clickMenuItem(ServiceNowMenuItems.All);

        serviceNowPage.openFromLeftNavigation(ProjectLeftNavigationItem.SELF_SERVICE, ProjectLeftNavigationItem.INCIDENTS);
        serviceNowPage.switchToLastTab();

        serviceNowTableViewPage.asserts().assertPartialUrl(ProjectTables.INCIDENT_TABLE);
    }
}