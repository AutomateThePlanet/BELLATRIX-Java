package solutions.bellatrix.servicenow.components.uiBuilder;

import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.services.ComponentCreateService;

public class UiBuilderGrid extends WebComponent implements ComponentDisabled, ComponentText {
    protected WebComponent customUiNowDateInputByLabel() {
        return this.shadowRootCreateByCss(WebComponent.class, "now-input");
    }

    protected TextInput customUiDateInputByLabel() {
        return customUiNowDateInputByLabel().shadowRootCreateByCss(TextInput.class, "input");
    }

    //calendar button
    protected Button customUiNowDateInputCalendarButton() {
        return this.shadowRootCreateByCss(Button.class, "*[icon='calendar-outline']");
    }

    //calendar pop up
    protected WebComponent customUiDropDownSeismicHoist() {
        return create().byCss(WebComponent.class, "seismic-hoist").toShadowRootToBeAttached();
    }

    protected Button calendarPopUpOkayButton() {
        return customUiDropDownSeismicHoist().shadowRootCreateByCss(Button.class, "sn-record-control:nth-of-type(2)");
    }

    protected Button calendarPopUpCloseButton() {
        return customUiDropDownSeismicHoist().shadowRootCreateByCss(Button.class, "sn-record-control:nth-of-type(1)");
    }

    protected Button calendarDateCellByAriaLabel(String ariaLabel) {
        return customUiDropDownSeismicHoist().shadowRootCreateByCss(Button.class, String.format("div[aria-label*='%s']", ariaLabel));
    }

    //TODO(I.D.) check disabled state
    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public String getText() {
        return customUiDateInputByLabel().getWrappedElement().getAttribute("value");
    }

    public void pickDateByPartialAriaLabel(String partialAriaLabel) {
        openCalendarPopUp();
        calendarDateCellByAriaLabel(partialAriaLabel).click();
        calendarPopUpOkayButton().scrollToVisible();
        calendarPopUpOkayButton().click();
    }

    public void clickOnDateCellByPartialAriaLabel(String partialAriaLabel) {
        calendarDateCellByAriaLabel(partialAriaLabel).click();
    }

    public void openCalendarPopUp() {
        customUiNowDateInputCalendarButton().click();
        browserService.waitForAjax();
    }

    public void setText(String text) {
        customUiDateInputByLabel().setText(text);
    }

    protected ComponentCreateService create() {
        return new ComponentCreateService();
    }
}