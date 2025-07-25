package O1_Login;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.pages.serviceNow.ServiceNowPage;
import solutions.bellatrix.servicenow.utilities.BaseInstancesUrlGeneration;

public class LoginTests extends ServiceNowBaseTest {
    @Test
    public void loadInitialServiceNowPageWithCredentialsInput() {
        ServiceNowPage serviceNowPage = new ServiceNowPage();
        serviceNowPage.loginSection().login("user", "pass");

        serviceNowPage.browser().assertLandedOnPage(BaseInstancesUrlGeneration.getSnInitialLoadUrl());
    }

    @Test
    public void loadInitialServiceNowPageWithConfigCredentials() {
        ServiceNowPage serviceNowPage = new ServiceNowPage();
        serviceNowPage.loginSection().login();

        serviceNowPage.browser().assertLandedOnPage(BaseInstancesUrlGeneration.getSnInitialLoadUrl());
    }
}
