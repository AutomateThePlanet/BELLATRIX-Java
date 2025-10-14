package O8_UIB.data.pages.assetsDashboardPage;

import solutions.bellatrix.servicenow.pages.uib.pages.baseManagerWorkspacePage.*;

public class AssetsDashboardPage extends BaseManagerWorkspacePage<Map, Asserts>{
    public void assertDashboardMainHeading(String heading) {
        map().dashboardHeading().validateTextIs(heading);
    }
}
