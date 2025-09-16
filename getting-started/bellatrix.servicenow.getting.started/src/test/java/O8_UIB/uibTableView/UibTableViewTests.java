package O8_UIB.uibTableView;

import O8_UIB.data.pages.allWorkPage.AllWorkPage;
import O8_UIB.data.pages.managerDashboardPage.ManagerDashboardPage;
import O8_UIB.data.pages.workspaceGeneralPage.WorkspaceGeneralPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;

public class UibTableViewTests extends ServiceNowBaseTest {
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
    public void workSpaceTableViewSettings_refreshIconTest(){
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.map().refreshIcon().click();

        allWorkPage.asserts().assertMainHeading("expected_main_heading");
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsOpenPopupTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.map().listActionsIcon().click();
        allWorkPage.map().editColumnsButton().click();

        allWorkPage.asserts().assertPopupHeading();
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsClosePopupTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.map().listActionsIcon().click();
        allWorkPage.map().editColumnsButton().click();
        allWorkPage.map().closeDialogueOnEditList().click();

        allWorkPage.asserts().assertMainHeading("expected_main_heading");
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsPopupSelectColumnTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.addAnyColumnByListAction("column_name","column_locator" );

        allWorkPage.asserts().assertEditColumnIsChecked("column_locator");
    }

    @Test
    public void workSpaceTableViewSettings_editColumnsPopupDeselectColumnTest() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.removeAnyColumnByListAction("column_name","column_locator" );

        allWorkPage.asserts().assertEditColumnNotChecked("column_locator");
    }

    @Test
    public void openRecordByColumnValue() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.filterByColumnValue("column_name", "column_value");
        allWorkPage.openRecordFromDataGrid("column_name", "column_value");
    }
}



