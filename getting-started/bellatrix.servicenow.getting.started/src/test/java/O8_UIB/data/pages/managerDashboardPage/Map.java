package O8_UIB.data.pages.managerDashboardPage;

import solutions.bellatrix.web.components.Div;

public class Map extends solutions.bellatrix.servicenow.pages.uib.pages.baseManagerWorkspacePage.Map {
    public Div dashboardHeading() {
        return getActiveScreen().createByXPath(Div.class, "//now-uxf-page/descendant::h1");
    }
}
