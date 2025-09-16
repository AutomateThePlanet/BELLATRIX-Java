package solutions.bellatrix.servicenow.pages.uib.pages.baseWorkspacePage;

import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

public class Map extends solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.Map {
    BaseWorkspacePage basePage = new BaseWorkspacePage();

    protected Div getFormSectionByName(String sectionHeader) {
        return getLeftSection()
                .createByXPath(Div.class, "//section[descendant::div[@class='sn-section-header-content' and @data-tooltip='%s']]".formatted(sectionHeader));
    }

    protected ShadowRoot getActiveScreen() {
        app().browser().waitForAjax();
        return basePage.mainContent.getActiveScreen();
    }

    public Div getLeftSection() {
        return getActiveScreen().createByXPath(Div.class, ".//now-uxf-tab-set[@slot='left']");
    }

    public Div getRightSection() {
        return getActiveScreen().createByXPath(Div.class, ".//now-uxf-tab-set[@slot='right']");
    }

    public ShadowRoot polarisMainMacroponent() {
        return create().byXPath(ShadowRoot.class, "//*[contains(name(),'macroponent')]");
    }

    public Div getModalPopup() {
        return polarisMainMacroponent().createByXPath(Div.class, ".//now-modal");
    }
}
