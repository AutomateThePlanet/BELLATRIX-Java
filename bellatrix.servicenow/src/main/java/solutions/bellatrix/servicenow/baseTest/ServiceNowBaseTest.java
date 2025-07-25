package solutions.bellatrix.servicenow.baseTest;


import solutions.bellatrix.servicenow.listeners.AutoWaitListener;
import solutions.bellatrix.servicenow.pages.serviceNow.ServiceNowPage;
import solutions.bellatrix.servicenow.pages.serviceNowTableView.ServiceNowTableViewPage;
import solutions.bellatrix.servicenow.plugins.authentication.AuthenticationPlugin;
import solutions.bellatrix.servicenow.plugins.fileuploads.FileUploadPlugin;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.web.infrastructure.BrowserLifecyclePlugin;
import solutions.bellatrix.web.infrastructure.LogLifecyclePlugin;
import solutions.bellatrix.web.services.App;

public class ServiceNowBaseTest extends BaseTest {
    protected ServiceNowPage serviceNowPage;
    protected ServiceNowTableViewPage serviceNowTableViewPage;

    protected App app() {
        return new App();
    }

    @Override
    protected void configure() {
        addPlugin(FileUploadPlugin.class);
        addPlugin(LogLifecyclePlugin.class);
        addPlugin(BrowserLifecyclePlugin.class);
        addPlugin(AuthenticationPlugin.class);
        addListener(AutoWaitListener.class);
    }

    @Override
    protected void beforeEach() throws Exception {
        super.beforeEach();
        serviceNowPage = app().createPage(ServiceNowPage.class);
        serviceNowTableViewPage = app().createPage(ServiceNowTableViewPage.class);
    }

    @Override
    protected void afterEach() {
        super.afterEach();
//        clearObjectsContainer();
    }
}