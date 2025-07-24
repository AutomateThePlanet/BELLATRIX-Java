package O2_Impersonate;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.pages.serviceNow.ServiceNowPage;
import solutions.bellatrix.servicenow.utilities.UserInteraction;
import solutions.bellatrix.web.infrastructure.junit.WebTest;

public class ImpersonateTests extends ServiceNowBaseTest {
    @Test
    public void impersonateAsUserWithRole() {
        var serviceNowPage = new ServiceNowPage();
        serviceNowPage.loginSection().login();
        serviceNowPage.impersonateUser("User Impersonate");

        serviceNowPage.assertUserImpersonate("User Impersonate");
    }
}
