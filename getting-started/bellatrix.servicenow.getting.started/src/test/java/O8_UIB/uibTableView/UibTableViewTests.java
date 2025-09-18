package O8_UIB.uibTableView;

import O8_UIB.data.pages.assetsRequestPage.AssetsRequestsPage;
import O8_UIB.data.pages.assetsDashboardPage.AssetsDashboardPage;
import O8_UIB.data.pages.requestPage.RequestPage;
import O8_UIB.data.pages.workspaceGeneralPage.WorkspaceGeneralPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;

public class UibTableViewTests extends ServiceNowBaseTest {
    protected WorkspaceGeneralPage workspaceGeneralPage;
    protected AssetsDashboardPage assetsDashboardPage;
    protected AssetsRequestsPage assetsRequestsPage;
    protected RequestPage requestPage;

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        workspaceGeneralPage = app().createPage(WorkspaceGeneralPage.class);
        assetsDashboardPage = app().createPage(AssetsDashboardPage.class);
        assetsRequestsPage = app().createPage(AssetsRequestsPage.class);
        requestPage = app().createPage(RequestPage.class);
    }

    @Test
    public void workSpaceTableViewSettings_refreshIconTest(){
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.map().refreshIcon().click();

        assetsRequestsPage.asserts().assertMainHeading("Asset requests");
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsOpenPopupTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.map().listActionsIcon().click();
        assetsRequestsPage.map().editColumnsButton().click();

        assetsRequestsPage.asserts().assertPopupHeading();
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsClosePopupTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.map().listActionsIcon().click();
        assetsRequestsPage.map().editColumnsButton().click();
        assetsRequestsPage.map().closeDialogueOnEditList().click();

        assetsRequestsPage.asserts().assertMainHeading("Asset requests");
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsPopupSelectColumnTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.addAnyColumnByListAction("Active","active" );

        assetsRequestsPage.asserts().assertEditColumnIsChecked("active");
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsPopupDeselectColumnTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.removeAnyColumnByListAction("Active","active" );

        assetsRequestsPage.asserts().assertEditColumnNotChecked("active");
    }

    @Test
    public void openRecordByColumnValue() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.filterByColumnValue("Number", "RITM0010004");
        assetsRequestsPage.openRecordFromDataGrid("Number", "RITM0010004");
        requestPage.asserts().assertRecordHeading("RITM0010004");
    }
}