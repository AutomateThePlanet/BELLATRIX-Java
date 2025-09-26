package solutions.bellatrix.servicenow.pages.uib.sections.mainContentSection;

import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.WebSection;

import java.util.List;

public class MainContentSection extends WebSection {
    BaseUIBPage baseUIBPage;

    public MainContentSection(BaseUIBPage parentPage) {
        baseUIBPage = parentPage;
    }

    public ShadowRoot getActiveScreen() {
        List<ShadowRoot> allVisibleScreens = baseUIBPage.base().createAllByCss(ShadowRoot.class, "sn-canvas-screen:not([style='display: none;'])");
        Log.info("Visible Screens found: %s. Selecting the last one.".formatted(allVisibleScreens.size()));
        return allVisibleScreens.get(allVisibleScreens.size() - 1);
    }

    public Button getNowScoreButton(String tileName) {
        return getActiveScreen().createByXPath(Button.class, String.format(".//now-score-basic/descendant::button[contains(@aria-label,'%s')]/span", tileName));
    }

    public Button getNowCardButton(String cardName, String tag) {
        var xpathLocator = ".//now-card[descendant::%s[normalize-space(text())='%s']]".formatted(tag, cardName);
        return getActiveScreen().createByXPath(Button.class, xpathLocator);
    }

    public Button getNowCardButton(String cardName) {
        var xpathLocator = ".//now-card[descendant::*[normalize-space(text())='%s']]".formatted(cardName);
        return getActiveScreen().createByXPath(Button.class, xpathLocator);
    }

    public Button getButtonByDataAriaDescribed(String value) {
        var xpathLocator = String.format(".//now-button/descendant::button[contains(@data-ariadescribedby,'%s')]", value);
        return getActiveScreen()
                .createByXPath(Button.class, xpathLocator);
    }

    public Button getButtonByText(String value) {
        var xpathLocator = String.format(".//now-button/descendant::button/descendant::span[normalize-space(text())='%s']", value);
        return getActiveScreen()
                .createByXPath(Button.class, xpathLocator);
    }

    public Button getFilterButton() {
        return getButtonByDataAriaDescribed("Show filter panel");
    }


    public Anchor noRecordDisplayLabel() {
        return getActiveScreen().createByXPath(Anchor.class, ".//h2[text()='No records to display.']");
    }

    public Button getEssentialsSectionTilesText(String value) {
        var xpath = String.format(".//h4[text()= '%s']", value);
        return getActiveScreen()
                .createByXPath(Button.class, xpath);
    }

    public Button getEssentialsLabel(String value) {
        var xpath = String.format(".//h1[text()= '%s']", value);
        return getActiveScreen()
                .createByXPath(Button.class, xpath);
    }

    public List<Anchor> getEssentialsSectionTiles() {
        return getActiveScreen().createAllByXPath(Anchor.class, ".//now-card");
    }

    public Button getOverViewSectionTilesCount(String tileName) {
        return getActiveScreen().createByXPath(Button.class, String.format(".//button[contains(@aria-label,'%s')]/span", tileName));
    }

    public Anchor getSectionLable(String tileName) {
        return getActiveScreen().createByXPath(Anchor.class, String.format(".//h2[text()='%s']", tileName));
    }

    public Anchor getWorkOrderDashboardLabel() {
        return getActiveScreen().createByXPath(Anchor.class, ".//h1[text()='Work Order Dashboard']");
    }
}
