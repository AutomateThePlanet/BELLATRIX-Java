package solutions.bellatrix.servicenow.pages.uib.pages.uibRecordViewPage;

import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.servicenow.pages.uib.pages.uibTableViewPage.UibTableViewPage;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

import java.util.List;

public class Map extends solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.Map{
    UibRecordViewPage baseUIBPage = new UibRecordViewPage();

    public ShadowRoot getActiveScreen() {
        List<ShadowRoot> allVisibleScreens = baseUIBPage.base().createAllByCss(ShadowRoot.class, "sn-canvas-screen:not([style='display: none;'])");
        Log.info("Visible Screens found: %s. Selecting the last one.".formatted(allVisibleScreens.size()));
        return allVisibleScreens.get(allVisibleScreens.size() - 1);
    }
}
