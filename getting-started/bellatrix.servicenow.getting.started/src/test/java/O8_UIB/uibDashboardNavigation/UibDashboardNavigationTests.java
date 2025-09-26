package O8_UIB.uibDashboardNavigation;

import O8_UIB.data.pages.assetsRequestPage.AssetsRequestsPage;
import O8_UIB.data.pages.assetsDashboardPage.AssetsDashboardPage;
import O8_UIB.data.pages.workspaceGeneralPage.WorkspaceGeneralPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;

public class UibDashboardNavigationTests extends ServiceNowBaseTest {
    protected WorkspaceGeneralPage workspaceGeneralPage;
    protected AssetsDashboardPage assetsDashboardPage;
    protected AssetsRequestsPage assetsRequestsPage;

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        workspaceGeneralPage = app().createPage(WorkspaceGeneralPage.class);
        assetsDashboardPage = app().createPage(AssetsDashboardPage.class);
        assetsRequestsPage = app().createPage(AssetsRequestsPage.class);
    }

    @Test
    public void openWorkspaceDashboardTest(){
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        workspaceGeneralPage.assertSidebarButtons();
        workspaceGeneralPage.assertTabButtons();
    }

    @Test
    public void dashboardSidebarOptionTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.ASSET_ESTATE).click();

        assetsDashboardPage.assertDashboardMainHeading("Asset estate");
    }

    @Test
    public void dashboardComponentCardTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.asserts().assertMainHeading("Asset requests");
    }
}
