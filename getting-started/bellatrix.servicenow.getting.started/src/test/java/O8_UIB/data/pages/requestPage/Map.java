package O8_UIB.data.pages.requestPage;

import O8_UIB.data.models.WorkGridModel;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.advanced.grid.Grid;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

public class Map extends solutions.bellatrix.servicenow.pages.uib.pages.uibRecordViewPage.Map {
    public Grid workkDataGrid() {
        var gridComponent = getActiveScreen()
                .createByCss(ShadowRoot.class, "now-grid")
                .createByCss(Grid.class, "table");

        gridComponent.setModelColumns(WorkGridModel.class);
        return gridComponent;
    }

    protected Div getFormSectionByName(String sectionHeader) {
        return getLeftSection()
                .createByXPath(Div.class, "//section[descendant::div[@class='sn-section-header-content' and @data-tooltip='%s']]".formatted(sectionHeader));
    }

    public Div getTemplateForm() {
        return getFormSectionByName("Requested Item");
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

    public Div getAlertNotification() {
        return polarisMainMacroponent().createAllByXPath(Div.class, ".//now-alert").stream().findFirst().orElse(null);
    }

    @Override
    public Button recordSaveButton() {
        var xpathLocator = "./descendant::*[contains(name(),'now-button')]/descendant::button[@data-ariadescribedby='Save']";
        return getActiveScreen().createByXPath(Button.class, xpathLocator);
    }
}
