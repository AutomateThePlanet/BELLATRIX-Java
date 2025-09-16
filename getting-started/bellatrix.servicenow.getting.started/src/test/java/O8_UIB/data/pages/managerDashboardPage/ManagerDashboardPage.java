package O8_UIB.data.pages.managerDashboardPage;

import solutions.bellatrix.servicenow.pages.uib.pages.baseManagerWorkspacePage.*;

public class ManagerDashboardPage extends BaseManagerWorkspacePage<Map, Asserts>{
    public void assertDashboardMainHeading(String heading) {
        map().dashboardHeading().validateTextIs(heading);
    }
}
