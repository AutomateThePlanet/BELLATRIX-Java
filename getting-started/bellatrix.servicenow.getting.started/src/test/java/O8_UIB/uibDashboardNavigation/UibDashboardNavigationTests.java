package O8_UIB.uibDashboardNavigation;

import O8_UIB.data.pages.allWorkPage.AllWorkPage;
import O8_UIB.data.pages.managerDashboardPage.ManagerDashboardPage;
import O8_UIB.data.pages.workspaceGeneralPage.WorkspaceGeneralPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;

public class UibDashboardNavigationTests extends ServiceNowBaseTest {
    protected WorkspaceGeneralPage workspaceGeneralPage;
    protected ManagerDashboardPage managerDashboardPage;
    protected AllWorkPage allWorkPage;

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        workspaceGeneralPage = app().createPage(WorkspaceGeneralPage.class);
        managerDashboardPage = app().createPage(ManagerDashboardPage.class);
        allWorkPage = app().createPage(AllWorkPage.class);
    }

    @Test
    public void openWorkspaceDashboardTest(){
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.assertSidebarButtons();
        workspaceGeneralPage.assertTabButtons();
    }

    @Test
    public void dashboardSidebarOptionTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.assertDashboardMainHeading("expected_dashboard_heading");
    }

    @Test
    public void dashboardComponentCardTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.asserts().assertMainHeading("expected_main_heading");
    }
}
