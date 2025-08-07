package solutions.bellatrix.servicenow.utilities;

import static solutions.bellatrix.web.infrastructure.DriverService.getWrappedDriver;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import lombok.SneakyThrows;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Wait;
import solutions.bellatrix.servicenow.infrastructure.exceptions.ElementStillExistException;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.FileInput;
import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.services.App;

public class UserInteraction {
    @SneakyThrows
    public static void waitASecond() {
        userInteractionWait(1000);
    }

    @SneakyThrows
    public static void waitHalfSecond() {
        userInteractionWait(500);
    }

    @SneakyThrows
    public static void waitASecondAndAHalf() {
        userInteractionWait(1500);
    }

    @SneakyThrows
    public static void wait2Seconds() {
        userInteractionWait(2000);
    }

    @SneakyThrows
    public static void wait4Seconds() {
        userInteractionWait(4000);
    }

    @SneakyThrows
    public static void waitForAffectedDeviceToHit() {
        userInteractionWait(22000);
    }

    public static void waitUntil(Function function) {
        long waitUntilReadyTimeout = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getWaitUntilReadyTimeout();
        long sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();

        waitUntil(function, waitUntilReadyTimeout, sleepInterval);
    }

    public static void waitUntil(Function function, long timeOutSeconds, long sleepSeconds) {
        var webDriverWait = new WebDriverWait(getWrappedDriver(), Duration.ofSeconds(timeOutSeconds), Duration.ofSeconds(sleepSeconds));
        webDriverWait.until(function);
    }

    @SneakyThrows
    public static void waitUntilSpaLoaded() {
        waitUntilSpinnerDisappears();
    }

    public static void waitUntilSpinnerDisappears() {
        var app = new App();
        var xpathLocator = "//*[name()='svg']//*[name()='circle']|//*[contains(concat(' ',normalize-space(@class),' '),' dx-icon-spindown ')]";
        Wait.retry(() -> {
            if (!app.create().getWrappedDriver().findElements(By.xpath(xpathLocator)).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, Duration.ofSeconds(200), Duration.ofSeconds(1), ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitAuthorizationPageHeadingDisplayed() {
        var app = new App();
        var xpathLocator = "//div[text()='Authorization Error']";
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, Duration.ofSeconds(200), Duration.ofSeconds(2), ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitUntilGridPopulated() {
        var app = new App();
        var xpathLocator = "//span[@class='dx-datagrid-nodata']";
        Wait.retry(() -> {
            if (!app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitUntilDropDownOptionsAppear() {
        var app = new App();
        var xpathLocator = "//div[contains(@class,'dx-selectbox-popup-wrapper')]//div[contains(@class,'dx-list-item') and text()]";
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).size() != 2) {
                throw new ElementStillExistException();
            }
        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitUntilHeadersAppear() {
        var app = new App();
        var xpathLocator = "//td[@role='columnheader']//div[text() and not(contains(@class,'checkbox'))]";
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, Duration.ofSeconds(90), Duration.ofSeconds(2), ElementStillExistException.class);
    }

    public static void waitUntilStateIsComplete() throws InterruptedException {
        var app = new App();
        var xpathLocator = "//div[@models-type='label' and .//span[text()='State']]/following-sibling::div/select";
        Wait.retry(() -> {
            if (!Objects.equals(app.create().byXPath(Select.class, xpathLocator).getSelected().getText(), "Completed")) {
                throw new ElementStillExistException();
            }
        }, Duration.ofMinutes(7), Duration.ofSeconds(5), ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForPropertyCardsToBeDisplayed() {
        var app = new App();
        var xpathLocator = "//div[./label[text()='Properties']]//following-sibling::div/div[.//label[text()]]";
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                throw new ElementStillExistException();
            }

        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForAttachInput() {
        var app = new App();
        var xpathLocator = "//input[@id='attachFile']";
        Wait.retry(() -> {
            if (app.create().allByXPath(FileInput.class, xpathLocator).isEmpty()) {
                throw new ElementStillExistException();
            }

        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForLabelsToBeDisplayed() {
        var app = new App();
        var xpathLocator = "//div[@role='form']//span[@class='dx-field-item-label-text']";
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, Duration.ofSeconds(60), Duration.ofSeconds(3), ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitUntilReactPageDisplayed() {
        var app = new App();
        var xpathLocator = "//div[@id='react-root']//*";
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                app.browser().refresh();
                app.browser().waitUntilPageLoadsCompletely();
                app.browser().waitForAjax();
                System.out.println("refreshed");
                throw new ElementStillExistException();
            }
        }, Duration.ofSeconds(60), Duration.ofSeconds(2), ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForButtonToLoad(String value) {
        var app = new App();
        var locator = String.format("//div[@class='dx-button-content' and contains(text(),'%s')]", value);
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, locator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForProcedureToLoad(int value) {
        var app = new App();
        var locator = String.format("(//div[@role='listbox']//div[@id and@aria-describedby])[%s]", value);
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, locator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForAssignmеntGroupToLoad(String value) {
        var app = new App();
        var locator = String.format("(//div[contains(@class,'dx-scrollview-content') and @role='listbox'])[1]//span[text()='%s']", value);
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, locator).isEmpty()) {
                throw new ElementStillExistException();
            }
        }, ElementStillExistException.class);
    }

    @SneakyThrows
    public static void waitForSecurityEventToBeCreatedForToday(String evenId) {
        var app = new App();
        var xpathLocator = String.format("//td[contains(text(),'%s')]//parent::tr//td[contains(@class,'list_decoration_cell')]//a", evenId);
        Wait.retry(() -> {
            if (app.create().allByXPath(Div.class, xpathLocator).isEmpty()) {
                app.browser().refresh();
                app.browser().waitUntilPageLoadsCompletely();
                app.browser().waitForAjax();
                throw new ElementNotInteractableException("Element not found exception.");
            }
        }, Duration.ofSeconds(70), Duration.ofSeconds(5), ElementNotInteractableException.class);

    }

    @SneakyThrows
    public static void waitForServiceNowFormDataToLoad() {
        var app = new App();
        int millis = 500;
        app.browser().injectInfoNotificationToast("Waiting for %s ms.".formatted(millis));
        Thread.sleep(millis);
    }

    public static Alert handleAlert(Runnable action) {
        var app = new App();
        int i = 0;
        while (i++ < 5) {
            try {
                return app.browser().getWrappedDriver().switchTo().alert();
            } catch (NoAlertPresentException e) {
                action.run();
                continue;
            }
        }
        return null;
    }

//    @SneakyThrows
//    public static void waitUntilEntityDeleted(TableApiRepository repository, Entity entity) {
//        Wait.retry(() -> {
//            try {
//                repository.getById(entity.getEntityId());
//            } catch (Exception e) {
//                throw new NullPointerException();
//            }
//        }, Duration.ofSeconds(20), Duration.ofSeconds(5), NullPointerException.class);
//    }

    private static void userInteractionWait(Integer millis) {
        try {
            var app = new App();
            app.browser().injectInfoNotificationToast("Waiting for user interaction %s s.".formatted(millis / 1000));
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }
}