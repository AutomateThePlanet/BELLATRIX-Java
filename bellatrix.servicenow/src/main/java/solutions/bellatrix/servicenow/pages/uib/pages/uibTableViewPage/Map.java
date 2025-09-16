package solutions.bellatrix.servicenow.pages.uib.pages.uibTableViewPage;

import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.*;
import solutions.bellatrix.web.components.advanced.grid.Grid;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

import java.util.List;

public class Map extends solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.Map {
    UibTableViewPage baseUIBPage = new UibTableViewPage();

    public ShadowRoot getActiveScreen() {
        List<ShadowRoot> allVisibleScreens = baseUIBPage.base().createAllByCss(ShadowRoot.class, "sn-canvas-screen:not([style='display: none;'])");
        Log.info("Visible Screens found: %s. Selecting the last one.".formatted(allVisibleScreens.size()));
        return allVisibleScreens.get(allVisibleScreens.size() - 1);
    }

    public ShadowRoot customUiMainMacroponent() {
        return create().byXPath(ShadowRoot.class, "//*[contains(name(),'macroponent')]").getShadowRoot();
    }

    public ShadowRoot popoverShadowRoot() {
        return create().byXPath(ShadowRoot.class, "//seismic-hoist[@slot='content']").getShadowRoot();
    }

    public ShadowRoot popoverPanel() {
        return create().byXPath(ShadowRoot.class, "//seismic-hoist/sn-record-list-column-filter").getShadowRoot();
    }

    public ShadowRoot modalEditShadowRoot() {
        return create().byXPath(ShadowRoot.class, "//sn-record-list-modal-edit").getShadowRoot();
    }

    public Button buttonByAriaLabel(String ariaLabel ) {
        return getActiveScreen()
                .createByCss(Button.class, "button[aria-label*='%s']".formatted(ariaLabel));
    }

    public Button listActionsIcon() {
        return buttonByAriaLabel("List Actions");
    }

    public Button refreshIcon() {
        return buttonByAriaLabel("Refresh");
    }

    public Button newRecordButton() {
        return buttonByAriaLabel("New");
    }

    public Button exportButton() {
        return buttonByAriaLabel("Export");
    }

    public Button editRecordButton() {
        return buttonByAriaLabel("Edit");
    }

    public Button viewMapButton() {
        return buttonByAriaLabel("View Map");
    }

    public Button bulkUpdateButton() {
        return buttonByAriaLabel("Bulk Update");
    }

    public Grid dataGrid() {
        var gridComponent = getActiveScreen()
                .createByCss(ShadowRoot.class, "now-grid")
                .createByCss(Grid.class, "table");

        return gridComponent;
    }

    public Button closeMultiEditForm() {
        return getActiveScreen()
                .createByCss(Button.class, "button[aria-label='Close Multi Edit Form'] now-icon");
    }

    public Button editColumnsButton() {
        return popoverShadowRoot().createByXPath(Button.class, "//div[contains(text(),'Edit columns')]");
    }

    public Button resetWidthsButton() {
        return popoverShadowRoot().createByXPath(Button.class, "//div[contains(text(),'Reset widths')]");
    }

    public Anchor closeDialogueOnEditList() {
        return getActiveScreen()
                .createByCss(ShadowRoot.class, "sn-record-list-modal-edit")
                .createByCss(Anchor.class, "button[aria-label='Close dialog']");
    }

    public TextInput searchFieldOnEditList() {
        return getActiveScreen()
                .createByCss(TextInput.class, "input[aria-label='Search']");
    }

    public CheckBox anyColumnCheckboxOnEditColumnList(String name) {
        return getActiveScreen()
                .createByXPath(CheckBox.class, "//now-checkbox[@name='%s']".formatted(name));
    }

    public Button buttonByText(String text) {
        return getActiveScreen()
                .createByXPath(Button.class, "//now-button/descendant::button/descendant::span[contains(text(),'%s')]".formatted(text));
    }

    public Button okButtonOnEditList() {
        return buttonByText("OK");
    }

    public Button getAttachmentsSidebarButton() {
        return getActiveScreen().createByXPath(Button.class, ".//now-icon[@icon='paperclip-outline']");
    }

    public List<Anchor> getAttachmentCards() {
        return getActiveScreen().createAllByXPath(Anchor.class, ".//span[@class='now-card-header-heading-label']");
    }

    public Div mainHeading() {
        return customUiMainMacroponent().createByXPath(Div.class, ".//now-record-list/descendant::now-heading/descendant::h1");
    }

    public Div popupHeading() {
        return getActiveScreen().createByXPath(Div.class, ".//now-modal/descendant::div[contains(@class,'now-modal-header')]/descendant::h2");
    }

    public Button getColumnFilter(String columnName) {
        return getActiveScreen()
                .createByXPath(Button.class, ".//button[contains(@aria-label, 'Filter %s')]".formatted(columnName));
    }

    public TextInput inputPopoverFilter() {
        return popoverPanel()
                .createByXPath(TextInput.class, ".//now-input/descendant::input");
    }

    public Button popupButtonByText(String text) {
        return popoverPanel()
                .createByXPath(Button.class, "//now-button/descendant::button/descendant::span[contains(text(),'%s')]".formatted(text));
    }

}
