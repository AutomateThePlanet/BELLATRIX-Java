package O8_UIB.uibRecordView;

import O8_UIB.data.entities.CatalogItem;
import O8_UIB.data.entities.CatalogItemRepository;
import O8_UIB.data.models.RequestItemModel;
import O8_UIB.data.pages.assetsRequestPage.AssetsRequestsPage;
import O8_UIB.data.pages.assetsDashboardPage.AssetsDashboardPage;
import O8_UIB.data.pages.requestPage.RequestPage;
import O8_UIB.data.pages.workspaceGeneralPage.WorkspaceGeneralPage;
import O8_UIB.data.user.User;
import O8_UIB.data.user.UserRepository;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;

import java.util.List;

public class UibRecordViewTests extends ServiceNowBaseTest {
    protected WorkspaceGeneralPage workspaceGeneralPage;
    protected AssetsDashboardPage assetsDashboardPage;
    protected AssetsRequestsPage assetsRequestsPage;
    protected RequestPage requestPage;
    User currentUser;
    CatalogItem catalogItemRegistered;
    String  expectedIncidentNumber;

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        workspaceGeneralPage = app().createPage(WorkspaceGeneralPage.class);
        assetsDashboardPage = app().createPage(AssetsDashboardPage.class);
        assetsRequestsPage = app().createPage(AssetsRequestsPage.class);
        requestPage = app().createPage(RequestPage.class);

        UserRepository userRepository = new UserRepository();
        currentUser = userRepository.getEntitiesByParameters(List.of(new QueryParameter("user_name", ConfigurationService.get(ServiceNowProjectSettings.class).getUserName()))).get(0);

        CatalogItemRepository catalogItemRepository = new CatalogItemRepository();

        catalogItemRegistered = catalogItemRepository.getEntitiesByParameters(List.of(new QueryParameter("name", "Apple%20Watch"))).get(0);
        expectedIncidentNumber = "RITM0010004";
    }

    @Test
    public void openRecordForm() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);

        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        assetsRequestsPage.openRecordByColumnValue("Number", expectedIncidentNumber);

        var expectedFormData = RequestItemModel.builder()
                .number(expectedIncidentNumber)
                .item(catalogItemRegistered.getSysId())
                .openedBy(currentUser.getSysId())
                .build();

         requestPage.assertTemplateForm(expectedFormData);
    }

    @Test
    public void fillRecordForm() {
        serviceNowPage.loginSection().login();
        serviceNowPage.openWorkspace(ServiceNowWorkspaces.ASSET_WORKSPACE);
        assetsDashboardPage.mainContent.getNowScoreButton("Asset requests").click();

        var newFormData = RequestItemModel.builder()
                .item("Acrobat")
                .build();

        requestPage.mainContent.getButtonByText("New").click();

        requestPage.fillTemplateForm(newFormData);

        requestPage.map().recordSaveButton().click();
    }
}