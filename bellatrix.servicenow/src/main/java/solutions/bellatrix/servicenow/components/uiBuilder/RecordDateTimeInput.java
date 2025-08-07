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

public class RecordDateTimeInput extends UIBDefaultComponent implements ComponentDisabled, ComponentText {

    protected ShadowRoot customUiDropDownSeismicHoist() {
        return create().byCss(ShadowRoot.class, "seismic-hoist").toShadowRootToBeAttached();
    }

    protected ShadowRoot customUiNowDateTimeInputByLabel() {
        return this.createByCss(ShadowRoot.class, "now-input");
    }

    protected ShadowRoot customUiNowSetTimeInputByLabel() {
        return customUiDropDownSeismicHoist().createByCss(ShadowRoot.class, "now-input");
    }

    protected TextInput customUiDateTimeInputByLabel() {
        return customUiNowDateTimeInputByLabel().createByCss(TextInput.class, "input");
    }

    protected TextInput customUiSetTimeInputByLabel() {
        return customUiNowSetTimeInputByLabel().createByCss(TextInput.class, "input");
    }

    protected Button customUiNowDateTimeInputCalendarButton() {
        return this.createByCss(Button.class, "*[icon='calendar-clock-outline']");
    }

    protected Button getCalendarDateCell(String date, String month) {
        String formattedDate = String.format("%s, %s", date, month);
        return customUiDropDownSeismicHoist().createByXPath(Button.class, String.format(".//span[text()='%s']/..", formattedDate));
    }

    protected Button getCalendarMonthFromMonthsTab(String ariaLabel) {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, String.format(".//span[text()='%s']/..", ariaLabel));
    }

    protected Button getCalendarNextMonthButton() {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, "");
    }

    protected Button getCalendarPreviousMonthButton() {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, ".//button[@aria-label='Go to previous month']/..");
    }

    protected Button getCalendarMonthsTab() {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, ".//now-icon[@icon='caret-down-fill']/..");
    }

    protected Button getCalendarNowButton() {
        return customUiDropDownSeismicHoist().createByXPath(Button.class, ".//span[text()='Now']/..");
    }

    @Override
    public boolean isDisabled() {
        throw new NotImplementedException("Method not implemented. Please use: isDateDisabled(String date)");
    }

    @Override
    public UibComponentType componentType() {
        return UibComponentType.DATE_TIME;
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

    public Boolean isDateDisabled(LocalDateTime targetedDate) {
        return isDateDisabled(String.valueOf(targetedDate.getDayOfMonth()),targetedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }

    public void navigateToTargetMonth(String targetedMonthName) {
        getCalendarMonthsTab().click();
        getCalendarMonthFromMonthsTab(targetedMonthName).click();
        browserService.waitForAjax();
    }

    @Override
    public String getText() {
        return customUiDateTimeInputByLabel().getWrappedElement().getAttribute("value");
    }

    public void setText(String text) {
        customUiDateTimeInputByLabel().setText(text);
    }

    public void clickOnDateCell(String date, String targetedMonth) {
        getCalendarDateCell(date, targetedMonth).click();
    }

    public void clickOnDateCell(LocalDateTime dateTime) {
        clickOnDateCell(String.valueOf(dateTime.getDayOfMonth()), dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }

    public void clickCalenderNowButton() {
        getCalendarNowButton().click();
    }

    public void openCalendarPopUp() {
        if (!isDropDownOpen()) {
            customUiNowDateTimeInputCalendarButton().click();
            browserService.waitForAjax();
        }
    }

    public void setTime(String time) {
        customUiSetTimeInputByLabel().setText(time);
    }

    protected ComponentCreateService create() {
        return new ComponentCreateService();
    }
}