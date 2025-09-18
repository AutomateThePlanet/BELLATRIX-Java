package solutions.bellatrix.servicenow.pages.uib.sections.tabsSection;

import solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.WebSection;

import java.util.List;

public class TabsSection extends WebSection {
    BaseUIBPage baseUIBPage;

    public TabsSection(BaseUIBPage parentPage) {
        baseUIBPage = parentPage;
    }
    public ShadowRoot getWrapper() {
        return baseUIBPage.base().createByCss(ShadowRoot.class, "sn-canvas-tabs#item-wsTabs");
    }

    public Button getTabByTitle(String title) {
        return getAllTabs().stream().filter(t -> t.getAttribute("data-tooltip").contains(title)).findFirst().orElse(null);
    }

    public Button getFirstTab() {
        return getAllTabs().stream().findFirst().orElse(null);
    }

    public Button getLastTab() {
        return getAllTabs().get(getAllTabs().size() - 1);
    }

    public List<Button> getAllTabs() {
        return getWrapper().createAllByXPath(
            Button.class,
            "//li[@class='sn-chrome-one-tab-container' and contains(@id,'li_tab')]");
    }
}
