package solutions.bellatrix.servicenow.components.uiBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import org.apache.commons.lang3.NotImplementedException;
import solutions.bellatrix.servicenow.components.enums.UibComponentType;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.contracts.ComponentDisabled;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.services.ComponentCreateService;

public class RecordDateInput extends UIBDefaultComponent implements ComponentDisabled, ComponentText {
    protected ShadowRoot customUiNowDateInputByLabel() {
        return this.createByCss(ShadowRoot.class, "now-input");
    }

    protected TextInput customUiDateInputByLabel() {
        return customUiNowDateInputByLabel().createByCss(TextInput.class, "input");
    }

    //calendar button
    protected Button customUiNowDateInputCalendarButton() {
        return this.createByCss(Button.class, "*[icon='calendar-outline']");
    }

    //calendar pop up
    protected ShadowRoot customUiDropDownSeismicHoist() {
        return create().byCss(ShadowRoot.class, "seismic-hoist").getShadowRoot();
    }

    protected Button calendarPopUpOkayButton() {
        return customUiDropDownSeismicHoist().createByCss(Button.class, "sn-record-control:nth-of-type(2)");
    }

    protected Button calendarPopUpCloseButton() {
        return customUiDropDownSeismicHoist().createByCss(Button.class, "sn-record-control:nth-of-type(1)");
    }

    protected Button getCalendarDateCell(String date, String month) {
        String formattedDate = String.format("%s, %s", date, month);
        return customUiDropDownSeismicHoist().createByXPath(Button.class, String.format(".//span[text()='%s']/..", formattedDate));
    }

    protected Button getCalendarMonthFromMonthsTab(String ariaLabel) {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, String.format(".//span[text()='%s']/..", ariaLabel));
    }

    protected Button getCalendarMonthsTab() {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, ".//now-icon[@icon='caret-down-fill']/..");
    }

    @Override
    public boolean isDisabled() {
        throw new NotImplementedException("Method not implemented. Please use: isDateDisabled(String date)");
    }

    @Override
    public UibComponentType componentType() {
        return UibComponentType.DATE;
    }

    public boolean isDateDisabled(String date, String targetedMonth) {
        var currentMonth = String.valueOf(LocalDate.now().getMonth());
        openCalendarPopUp();
        if (currentMonth.equalsIgnoreCase(targetedMonth)) {
            navigateToTargetMonth(targetedMonth);
            var calendarDate = getCalendarDateCell(date, targetedMonth);
            return calendarDate.getAttribute("class").contains("is-disabled");
        } else {
            navigateToTargetMonth(targetedMonth);
            browserService.waitForAjax();
            var calendarDate = getCalendarDateCell(date, targetedMonth);
            return calendarDate.getAttribute("class").contains("is-disabled");
        }
    }

    public void navigateToTargetMonth(String targetedMonthName) {
        getCalendarMonthsTab().click();
        getCalendarMonthFromMonthsTab(targetedMonthName).click();
        browserService.waitForAjax();
    }

    @Override
    public String getText() {
        return customUiDateInputByLabel().getWrappedElement().getAttribute("value");
    }

    public void setText(String text) {
        customUiDateInputByLabel().setText(text);
    }

    public void clickOnDateCell(String date, String targetedMonthName) {
        getCalendarDateCell(date, targetedMonthName).click();
    }

    public void openCalendarPopUp() {
        if (!isDropDownOpen()) {
            customUiNowDateInputCalendarButton().click();
            browserService.waitForAjax();
        }
    }

    protected ComponentCreateService create() {
        return new ComponentCreateService();
    }

    public Boolean isDateDisabled(LocalDateTime targetedDate) {
        return isDateDisabled(String.valueOf(targetedDate.getDayOfMonth()),targetedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }

    public void clickOnDateCell(LocalDateTime targetedDate) {
        clickOnDateCell(String.valueOf(targetedDate.getDayOfMonth()),targetedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }
}