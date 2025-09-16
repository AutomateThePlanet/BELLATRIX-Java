package O8_UIB.uibRecordView;

import O8_UIB.data.models.TemplateModel;
import O8_UIB.data.pages.allWorkPage.AllWorkPage;
import O8_UIB.data.pages.managerDashboardPage.ManagerDashboardPage;
import O8_UIB.data.pages.entityPage.EntityPage;
import O8_UIB.data.pages.workspaceGeneralPage.WorkspaceGeneralPage;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;

public class UibRecordViewTests extends ServiceNowBaseTest {
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
    public void openRecordForm() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        allWorkPage.openRecordByColumnValue("column_name", "column_value");

        var entityPage = new EntityPage();

        var expectedFormData = TemplateModel.builder()
                .number("actual_number")
                .assetLocation("asset_location_choice")
                .asset("asset_id")
                .workType("work_type_id")
                .assignedTo("assigned_user_id")
                .priority("priority_choice")
                .assignmentGroup("assignment_group_id")
                .build();

         entityPage.assertTemplateForm(expectedFormData);
    }

    @Test
    public void fillRecordForm() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.CUSTOM_WORKSPACE_NAME);

        workspaceGeneralPage.sidebar.getMenuItem(LeftSidebarSection.MenuItems.DASHBOARD).click();

        managerDashboardPage.mainContent.getNowCardButton("card_button_name").click();

        var entityPage = new EntityPage();

        entityPage.mainContent.getButtonByText("New").click();

        var newFormData = TemplateModel.builder()
                .assetLocation("asset_location_choice")
                .asset("asset")
                .workType("work_type_id")
                .assignedTo("assigned_user_id")
                .priority("priority_choice")
                .assignmentGroup("assignment_group_id")
                .build();

        entityPage.fillTemplateForm(newFormData);
    }
}