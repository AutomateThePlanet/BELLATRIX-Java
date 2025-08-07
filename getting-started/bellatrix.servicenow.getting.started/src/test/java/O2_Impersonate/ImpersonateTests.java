package O2_Impersonate;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.servicenow.baseTest.ServiceNowBaseTest;
import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;

public class ImpersonateTests extends ServiceNowBaseTest {
    @Test
    public void impersonateAsUserWithRole() {
        var serviceNowPage = new ServiceNowPage();
        serviceNowPage.loginSection().login();
        serviceNowPage.impersonateUser("User Impersonate");

        serviceNowPage.assertUserImpersonate("User Impersonate");
    }
}
