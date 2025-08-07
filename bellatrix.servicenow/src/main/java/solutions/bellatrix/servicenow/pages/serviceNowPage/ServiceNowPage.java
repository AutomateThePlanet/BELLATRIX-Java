package solutions.bellatrix.servicenow.pages.serviceNowPage;


import lombok.SneakyThrows;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import solutions.bellatrix.servicenow.contracts.ServiceNowLeftNavigationItem;
import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.pages.sections.availableSelectPopUpSection.AvailableSelectPopUpSection;
import solutions.bellatrix.servicenow.pages.sections.loginSection.LoginSection;
import solutions.bellatrix.servicenow.pages.sections.personalizeColumnSection.PersonalizeColumnSection;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowMenuItems;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowUser;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowWorkspaces;
import solutions.bellatrix.servicenow.utilities.generators.InstancesUrlGeneration;
import solutions.bellatrix.servicenow.utilities.UserInteraction;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.WebPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceNowPage extends WebPage<Map, Asserts> {
    private final LoginSection loginSection;
    private final PersonalizeColumnSection personalizeColumnSection;
    private final Boolean isPolarisEnabled;
    private final AvailableSelectPopUpSection availableSelectPopUpSection;
    private String url;

    public ServiceNowPage() {
        isPolarisEnabled = ConfigurationService.get(ServiceNowProjectSettings.class).getIsPolarisEnabled();
        loginSection = new LoginSection();
        personalizeColumnSection = new PersonalizeColumnSection();
        availableSelectPopUpSection = new AvailableSelectPopUpSection();
    }

    public static String leftNavigationItemLocatorByFullPath(List<String> pathSegment) {
        StringBuilder locator = new StringBuilder();
        for (int i = 0; i < pathSegment.size() - 1; i++) {
            locator.append(String.format("//li[.//*[text()='%s']]", pathSegment.get(i)));
        }

        locator.append(String.format("//ul//a//div[@class='sn-widget-list-title'][text()='%s']", pathSegment.get(pathSegment.size() - 1)));
        return locator.toString();
    }

    public static String leftNavigationElementsLocatorByPath(List<String> pathSegment) {
        var locator = String.join("", pathSegment.stream().map(e -> String.format("//li[.//*[text()='%s']]", e)).toList());

        if (pathSegment.size() > 1) {
            locator = locator.replaceFirst("\\.", "./a");
        }

        locator = locator + "//ul//li[not(@style='display: none;')]//a//div[@class='sn-widget-list-title']";

        return locator;
    }

    public void open() {
        url = InstancesUrlGeneration.getBaseUrl();
        super.open();
        browser().waitForAjax();
        UserInteraction.waitUntilSpinnerDisappears();
    }

    public void open(boolean b) {
        switchToInnerFrame();
    }

    public void clickMenuItem(ServiceNowMenuItems item) {
        if (!validateMenuItemPinned(item)) {
            var menuItems = map().polarisHeader().shadowRootCreateAllByCss(Button.class, "div.sn-polaris-tab.can-animate.polaris-enabled".formatted(item.getValue()));
            var menuItem = menuItems.stream().filter(x -> x.getText().contains(item.getValue())).findFirst().get();
            menuItem.click();
            browser().waitForAjax();
        }
    }

    public void dismissNewUserModal() {
        try {
            browser().waitUntil(x -> map().closeModalButton().isVisible());
            map().closeModalButton().click();
        } catch (Exception e) {
            Log.info("Failed to close the pop-up: " + e.getMessage());
        }
    }

    public void openWorkspace(ServiceNowWorkspaces workspace) {
        clickMenuItem(ServiceNowMenuItems.WORKSPACES);
        var workspaceItems = map().polarisHeader().createByCss(ShadowRoot.class, "sn-polaris-menu[aria-label='Unpinned Workspaces menu']")
            .createAllByCss(Button.class, "a.nested-item.item-label.keyboard-navigatable");
        boolean itemFound = false;
        for (var item : workspaceItems) {
            if (item.getText().contains(workspace.getValue())) {
                item.click();
                itemFound = true;
                break;
            }
        }
        if (!itemFound) {
            throw new RuntimeException("Workspace '%s' not found.".formatted(workspace.getValue()));
        }

        app().browser().waitUntilPageLoadsCompletely();
        app().browser().waitForAjax();
    }

    @SneakyThrows
    public void openTablePageByDirectSearch(ServiceNowTable serviceNowProjectTable) {
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
        if (isPolarisEnabled) {
            browser().waitForAjax();
            browser().waitUntilPageLoadsCompletely();
            for (int i = 0; i < 3; i++) {
                if (!browser().getUrl().contains(serviceNowProjectTable.getTableName())) {
                    map().polarisMenuShadow().setText(String.format("%s.list", serviceNowProjectTable.getTableName()) + Keys.ENTER);
                    browser().waitForAjax();
                    browser().waitUntilPageLoadsCompletely();
                } else {
                    break;
                }
            }
            browser().waitForAjax();
        } else {
            while (!browser().getUrl().contains(serviceNowProjectTable.getTableName())) {
                map().leftNavigatorFilter().setText(String.format("%s.list", serviceNowProjectTable.getTableName()) + Keys.ENTER);
                browser().waitForAjax();
                browser().waitUntilPageLoadsCompletely();
            }
        }

        switchToInnerFrame();

    }

    public PersonalizeColumnSection personalizeColumnSection() {
        return personalizeColumnSection;
    }

    public AvailableSelectPopUpSection availableSelectPopUpSection() {
        return availableSelectPopUpSection;
    }

    public LoginSection loginSection() {
        return loginSection;
    }

    public void impersonateUser(ServiceNowUser user) {
        impersonateUser(user.getValue());
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void switchToFirstBrowserTab() {
        app().browser().switchToFirstBrowserTab();
    }

    public void impersonateUser(String userToImpersonate) {
        if (isPolarisEnabled) {
            browser().waitUntil(x -> map().polarisHeader().toExist(2, 1).isVisible());
            browser().waitUntil(x -> map().polarisAvatar().toBeVisible(5, 2).isVisible());
            map().polarisAvatar().click();
            browser().waitUntil(x -> map().polarisImpersonateUserAnchor().toBeClickable(2, 1).isVisible());
            map().polarisImpersonateUserAnchor().click();
            browser().waitUntil(x -> map().polarisImpersonationNowPopover().toBeVisible(2, 1).isVisible());
            waitUntilTextIsSet(map().polarisSearchUserInput(), userToImpersonate);
            browser().waitUntil(x -> map().polarisSelectedUser().toBeVisible(2, 1).isVisible());
            map().polarisSelectedUser().click();
            map().polarisImpersonateButton().click();
            waitCompletely();
        } else {
            map().userInfoDropDown().click();
            map().impersonateDropDownOption().click();
            map().searchUserToImpersonate().click();
            map().searchUserInput().setText(userToImpersonate);
            try {
                map().selectedUser().click();
            } catch (Exception exception) {
                throw new ElementNotInteractableException("Element not found exception.");
            }
        }
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
        UserInteraction.waitASecond();
    }

    public void endImpersonateUser() {
        if (isPolarisEnabled) {
//            browser().waitForReactPageLoadsCompletely();
            map().polarisAvatar().click();
            map().polarisUnimpersonateUserAnchor().click();
            browser().waitForAjax();
            browser().waitUntilPageLoadsCompletely();
        }
    }

    @SneakyThrows
    public <Item extends ServiceNowLeftNavigationItem> void filterDataInLeftNavigationPane(Item element) {
        if (isPolarisEnabled) {
            waitUntilTextIsSet(map().polarisMenuShadow(), element.getText());
        } else {
            map().leftNavigatorFilter().setText(element.getText());
            browser().waitForAjax();
        }
        UserInteraction.wait4Seconds();
    }

    @SafeVarargs
    public final <Item extends ServiceNowLeftNavigationItem> void openFromLeftNavigation(Item... path) {
        var stringPath = Arrays.stream(path).map(ServiceNowLeftNavigationItem::getText).toList();
        openFromLeftNavigation(stringPath);
    }

    public void openFormLeftNavigation(String item) {
        map().polarisMenuItems(item).click();
    }

    public void openFromLeftNavigation(String... path) {
        openFromLeftNavigation(Arrays.stream(path).toList());
    }

    public void openFromLeftNavigation(List<String> path) {
        filterDataInLeftNavigationPane(path.get(path.size() - 1));
        openNavigationSectionItem(path);
        switchToInnerFrame();
    }

    private void openNavigationSectionItem(List<String> path) {
        browser().waitForAjax();
        openLeftNavigatorItem(path);
    }

    public void filterDataInLeftNavigationPane(String input) {
        try {
            if (isPolarisEnabled) {
                waitUntilTextIsSet(map().polarisMenuShadow(), input);
            } else {
                browser().waitForAjax();
                map().leftNavigatorFilter().setText(input);
                browser().waitForAjax();
                browser().waitUntilPageLoadsCompletely();
                browser().waitForAjax();
            }
        } catch (Exception e) {
            throw new RuntimeException("Sidebar Navigation has failed.", e);
        }

    }

    public <Item extends ServiceNowLeftNavigationItem> void openLeftNavigatorItem(Item element) {
        if (isPolarisEnabled) {
            map().polarisMenuItem(element.getText()).click();
        } else {
            openLeftNavigatorItem(element.getText());
        }
    }

    public void openLeftNavigatorItem(String input) {
        if (isPolarisEnabled) {
            map().polarisMenuItem(input).click();
        } else {
            map().leftNavigatorElement(input).click();
        }
    }

    public void openLeftNavigatorItem(List<String> elements) {
        if (isPolarisEnabled) {
            browser().waitUntil(x -> !map().polarisMenu().shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list").isEmpty());
            var allCollapsibleLists = map().polarisMenu().shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");
            for (var collapsibleList : allCollapsibleLists) {
                browser().waitUntil(x -> collapsibleList.shadowRootCreateByCss(Div.class, "span.label").toBeVisible(2, 1).isVisible());
                var collapsableListTitleElement = collapsibleList.shadowRootCreateByCss(Div.class, "span.label");
                if (collapsableListTitleElement.getText().equals(elements.get(0))) {
                    browser().waitUntil(x -> !Objects.isNull(collapsibleList.shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list")));
                    var innerCollapsibleItems = collapsibleList.shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

                    if (!innerCollapsibleItems.isEmpty()) {
                        browser().waitUntil(x -> collapsibleList.shadowRootCreateByCss(Button.class, "span.item-icon mark.filter-match").toBeVisible(2, 1).toBeClickable(2, 1).isVisible());
                        collapsibleList.shadowRootCreateByCss(Button.class, "span.item-icon mark.filter-match").click();
                        break;
                    }
                    browser().waitUntil(x -> collapsibleList.shadowRootCreateByCss(Button.class, "span.item-icon mark.filter-match").toBeVisible(2, 1).toBeClickable(2, 1).isVisible());
                    collapsibleList.shadowRootCreateByCss(Button.class, "span.item-icon mark.filter-match").click();
                    return;
                }
            }
        } else {
            map().leftNavigationItemByPath(elements).scrollToVisible();
            map().leftNavigationItemByPath(elements).click();
        }

        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void switchToInnerFrame() {
        browser().waitUntilPageLoadsCompletely();
        if (isPolarisEnabled) {
            browser().switchToFrame(map().polarisMainPageFrame());
        } else {
            browser().switchToFrame(map().mainTableFrame());
        }
        browser().waitForAjax();
    }

    public void switchToDialogFrame() {
        browser().switchToFrame(map().dialogFrame());
        browser().waitForAjax();
    }

    public void switchToTemplateFrame() {
        browser().switchToFrame(map().templateFrame());
        browser().waitForAjax();
    }

    public void switchToFirstTabInnerFrame() {
        browser().getWrappedDriver().close();
        browser().switchToFirstBrowserTab();
        switchToInnerFrame();

        browser().waitUntilPageLoadsCompletely();
    }

    public void switchToFirstTab() {
        browser().getWrappedDriver().close();
        browser().switchToFirstBrowserTab();

        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void refreshPage() {
        browser().refresh();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void clickOnGearIcon() {
        map().gearIcon().click();
        browser().waitForAjax();
    }

    public void addColumnToView(String columnLabel) {
        clickOnGearIcon();
        var selectedColumns = personalizeColumnSection().map().selectedColumns().stream().map(x -> x.getText().contains(columnLabel)).toList();
        if (selectedColumns.size() == 0) {
            personalizeColumnSection().map().availableColumnByName(columnLabel).click();
            personalizeColumnSection().map().addArrowAnchor().click();
        }

        personalizeColumnSection().map().okButton().click();
        browser().waitForAjax();
    }

    public void switchToDefault() {
        browser().switchToDefault();
    }

    public void switchToLastTab() {
        browser().switchToLastTab();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    //Assertions
    public void assertUserImpersonate(ServiceNowUser user) {
        waitForPageLoad();
        assertUserImpersonate(user.getValue());
    }

    public void assertUserImpersonate(String user) {
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
        if (isPolarisEnabled) {
            var ariaLabel = map().polarisAvatar().shadowRootCreateByCss(Div.class, ".now-avatar-content > span").getAttribute("aria-label");
            assertTrue(ariaLabel.contains(user));
        } else {
            map().userName().validateHtmlContains(user);
        }
    }

    public void assertHomeTitle(String expectedTitle) {
        browser().waitForAjax();
        switchToInnerFrame();
        map().homeTitle().validateTextIs(expectedTitle);
        switchToDefault();
    }

    public void navigateToPage(String url, Boolean isWithIFrame) {
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
        navigate().to(url);

        if (isWithIFrame) {
            switchToInnerFrame();
        }
        browser().waitForAjax();
    }

    public List<String> listLeftNavigationItemNamesByPath(String... pathSegmentArg) {
        var pathSegment = Arrays.stream(pathSegmentArg).toList();
        var locator = leftNavigationElementsLocatorByPath(pathSegment);
        UserInteraction.waitASecond();
        var elements = map().listLeftNavigationItemsByPath(locator);
        List<String> elementsNames = new ArrayList<>();
        elements.forEach(e -> elementsNames.add(e.getText()));
        return elementsNames;
    }

    public void navigateToPage(String url) {
        navigateToPage(url, false);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    public void endImpersonate() {
        if (isPolarisEnabled) {
            map().polarisAvatar().click();
            map().polarisEndImpersonateUserAnchor().click();
            browser().waitUntil(x -> map().polarisAvatar().getCssValue("border").contains("0px none rgb(193, 197, 205"));
        } else {
            map().userInfoDropDown().click();
        }
    }

    public void openLeftNavigatorItemByChoice(String input) {
        if (isPolarisEnabled) {
            map().polarisMenuItemChoice(input).click();
        } else {
            map().polarisMenuItemChoice(input).click();
        }
    }

    public void enterTextInFilter(String input) {
        map().polarisMenuShadow().setText(input);
        browser().waitForAjax();
    }

    public void searchTableInNavbar(String table) {
        enterTextInFilter(table);
        map().polarisMenuFilterShadow().setText("" + Keys.ENTER);
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void searchBy(String searchBy, String input) {
        map().tableSearchDropdown().click();
        browser().waitForAjax();
        map().tableSearchOption(searchBy).click();
        browser().waitForAjax();
        map().tableInputField().setText(input + Keys.ENTER);
        browser().waitForAjax();
    }

    public void clickOnFirstRowInfoIcon() {
        map().infoIconFirstRow().hover();
        map().infoIconFirstRow().click();
        browser().waitForAjax();
    }

    public void openRecord() {
        clickOnFirstInfoIcon();
        map().openRecord().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void clickOnFilterIcon() {
        map().filterButton().click();
        browser().waitForAjax();
    }

    public void clickOnRunButton() {
        map().runButton().click();
        browser().waitForAjax();
    }

    public void clickOnAndButton() {
        map().andButton().click();
        browser().waitForAjax();
    }

    public void selectFirstFilterOption(String row, String option) {
        map().firstFilterDropdownButton(row).click();
        map().firstFilterDropdownInnerRow().setText(option + Keys.ENTER);
        browser().waitForAjax();
    }

    public void selectSecondFilterOption(String row, String option) {
        map().secondFilterDropdownButton(row).click();
        map().secondFilterDropdown(row).selectByText(option);
        browser().waitForAjax();
    }

    public void listTableFromNavBar(String table) {
        enterTextInFilter(table + ".list");
        map().polarisMenuShadow().getWrappedElement().sendKeys(Keys.ENTER);
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void searchInTable(String table, String searchBy, String value) {
        listTableFromNavBar(table);
        switchToInnerFrame();
        searchBy(searchBy, value);
    }

    public void clickOnFirstInfoIcon() {
        map().infoIconFirstRow().click();
        browser().waitForAjax();
    }

    public void openRowRecord(String row) {
        map().infoIconRow(row).hover();
        map().infoIconRow(row).click();
        map().openRecord().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void openFromLeftNavigation(String item) {
        map().polarisMenuItems(item).click();
        waitForPageLoad();
    }

    public void doTableFromNavBar(String table) {
        enterTextInFilter(table + ".do");
        map().polarisMenuShadow().getWrappedElement().sendKeys(Keys.ENTER);
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void waitCompletely() {
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
        browser().waitForAjax();
        UserInteraction.waitUntilSpinnerDisappears();
    }

    public void switchToFirstBrowserTabAndRefresh() {
        switchToFirstBrowserTab();
        refreshPage();
        switchToInnerFrame();
    }

    private void waitUntilTextIsSet(TextInput component, String value) {
        browser().waitUntil(x -> {
            component.setText(value);
            var searchField = component.getText();
            if (Objects.equals(value, searchField)) {
                return true;
            }
            return false;
        });
    }

    public Boolean validateMenuItemPinned(ServiceNowMenuItems item) {
        var isPinned = false;
        var pinnedItems = map().polarisHeader().shadowRootCreateAllByCss(Button.class, "div.tab.name.shown");
        var itemList = pinnedItems.stream().filter(x -> x.getText().contains(item.getValue())).toList();
        browser().waitForAjax();
        return itemList.isEmpty();
    }
}