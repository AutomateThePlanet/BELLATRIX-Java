package solutions.bellatrix.servicenow.pages.serviceNowPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.web.components.Anchor;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.Frame;
import solutions.bellatrix.web.components.Label;
import solutions.bellatrix.web.components.Select;
import solutions.bellatrix.web.components.Span;
import solutions.bellatrix.web.components.TextArea;
import solutions.bellatrix.web.components.TextInput;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.PageMap;
import solutions.bellatrix.web.services.BrowserService;

public class Map extends PageMap {
    public Button userInfoDropDown() {
        return create().byId(Button.class, "user_info_dropdown");
    }

    public Button impersonateDropDownOption() {
        return create().byId(Button.class, "glide_ui_impersonator");
    }

    public Button searchUserToImpersonate() {
        return create().byXPath(Button.class, "//div[@ng-controller='Impersonate']//div[contains(@class,'select2-containe')]");
    }

    public Button impersonateUserCloseButton() {
        return create().byXPath(Button.class, "//div[@ng-controller='Impersonate']//button");
    }

    public TextInput searchUserInput() {
        return create().byId(TextInput.class, "s2id_autogen2_search");
    }

    public Anchor selectedUser() {
        return create().byXPath(Anchor.class, "//ul/li[contains(@class,'select2-highlighted')]");
    }

    public TextInput leftNavigatorFilter() {
        return create().byId(TextInput.class, "filter").toBeClickable();
    }

    public Anchor leftNavigatorElement(String label) {
        String xpathLocator = String.format("//div[@class='sn-widget-list-title' and contains(text(),'%s')]", label);
        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Anchor leftNavigatorElement(String section, String element) {
        String xpathLocator = String.format("//li[.//a/span[text()='%s']]/ul//a//div[@class='sn-widget-list-title' and text()='%s']", section, element);
        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Button leftNavigatorSubCategory(String section, String subCategory) {
        String xpathLocator = String.format("//li[./a/span[text()='%s']]/ul//a//span[@models-tooltip-overflow-only-text and text()='%s']", section, subCategory);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Button getAllButton() {
        var all = "div[aria-label='All']";
        return polarisMainMacroponent().createByCss(Button.class, all).toBeVisible().toBeClickable();
    }

    public Frame mainTableFrame() {
        return create().byId(Frame.class, "gsft_main");
    }

    public Span userName() {
        return create().byXPath(Span.class, "//span[contains(@class,'user-name')]");
    }

    public String userNamePolaris() {
        var shadowRoot = polarisHeader().shadowRootCreateByCss(Div.class, "div.user-name");
        var actualUserRole = shadowRoot.getText();
        return actualUserRole;
    }

    public TextInput homeTitle() {
        return create().byId(TextInput.class, "home_title");
    }

    public Div polarisHomeTitle() {
        return create().byXPath(Div.class, "//div[@class='title' and text()]");
    }

    public Button gearIcon() {
        return create().byXPath(Button.class, "(//i[@models-title='Personalize List Columns'])[1]");
    }

    public Anchor polarisAllElementButton() {
        return polarisHeader().shadowRootCreateByCss(Anchor.class, "div[aria-label='All']");
    }

    public TextInput polarisMenuShadow() {
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-menu.is-main-menu")
            .shadowRootCreateByCss(TextInput.class, "#filter");
    }

    public Anchor polarisMenuItem(String label) {
        String cssSelector = String.format("a[aria-label='%s'][class*='highlighted-item']", label);
        return polarisMenuCollapsibleList().shadowRootCreateByCss(Anchor.class, cssSelector);
    }

    public Anchor polarisMenuItem(String mainLabel, String searchMenu) {
        var cssLocator = String.format("[aria-label='%s']~div a[aria-label^='%s']", mainLabel, searchMenu);
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-menu.is-main-menu")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list:last-child")
            .shadowRootCreateByCss(Anchor.class, cssLocator);
    }

    public Anchor polarisMenuItems(String searchMenu) {
        var cssLocator = String.format("li a[aria-label='%s']", searchMenu);
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-menu")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list")
            .shadowRootCreateByCss(Anchor.class, cssLocator);
    }

    public Frame polarisMainPageFrame() {
        return polarisMainMacroponent().shadowRootCreateByCss(Frame.class, "#gsft_main");
    }

    public Anchor polarisAvatarUser() {
        return polarisAvatar().shadowRootCreateByCss(Anchor.class, "span[aria-label*='User Avatar']");
    }

    public Button polarisAvatar() {
        return polarisHeader().shadowRootCreateByCss(Button.class, "div > now-avatar");
    }

    public Button polarisImpersonateUserAnchor() {
        return polarisHeader().shadowRootCreateByCss(Button.class, "button[models-id='impersonateUser']");
    }

    public Anchor polarisUnimpersonateUserAnchor() {
        return polarisHeader().shadowRootCreateByCss(Anchor.class, "button[models-id='unimpersonate']");
    }

    public Anchor polarisImpersonateButton() {
        return polarisImpersonationNowModal().shadowRootCreateAllByCss(Anchor.class, "now-button").get(1);
    }

    public TextInput polarisSearchUserInput() {
        return polarisImpersonationNowPopover().createByCss(TextInput.class, "div > input[placeholder='Search for a user']");
    }

    public Anchor polarisSelectedUser() {
        return polarisSeismicHoist().shadowRootCreateByCss(Anchor.class, "mark.has-highlight");
    }

    public List<Anchor> polarisAllSelectedUser() {
        return polarisSeismicHoist().shadowRootCreateAllByCss(Anchor.class, "mark.has-highlight");
    }

    public Anchor polarisAvatarContextualMenu() {
        return polarisHeader().shadowRootCreateByCss(Anchor.class, "div > sn-contextual-menu[id=userMenu]]");
    }

    private WebComponent polarisSeismicHoist() {
        return create().byCss(WebComponent.class, "now-popover-panel > seismic-hoist");
    }

    private WebComponent polarisNowPopoverPanel() {
        return polarisMainMacroponent().shadowRootCreateByCss(WebComponent.class, "now-popover-panel");
    }

    public WebComponent polarisImpersonationNowPopover() {
        return polarisImpersonationNowTypeHead().shadowRootCreateByCss(WebComponent.class, "now-popover");
    }

    private WebComponent polarisImpersonationNowTypeHead() {
        return polarisImpersonationNowModal().createByCss(WebComponent.class, "div > now-typeahead");
    }

    private WebComponent polarisImpersonationNowModal() {
        return polarisImpersonation().shadowRootCreateByCss(WebComponent.class, "now-modal");
    }

    private WebComponent polarisImpersonation() {
        return polarisLayout().shadowRootCreateByCss(WebComponent.class, "sn-impersonation");
    }

    public ShadowRoot polarisHeader() {
        return polarisLayout().shadowRootCreateByCss(ShadowRoot.class, "sn-polaris-header").toShadowRootToBeAttached();
    }

    public Div polarisHeaderElement(){
        return polarisHeader().getShadowRoot().createByXPath(Div.class, ".//div[contains(@class ,'is-impersonating')]");
    }

    private WebComponent polarisAvatarHeader() {
        return polarisCanvasAppShellLayout().shadowRootCreateByCss(WebComponent.class, "sn-polaris-header");
    }

    public WebComponent polarisLayout() {
        return polarisMainMacroponent().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout").toShadowRootToBeAttached();
    }

    WebComponent polarisMenu() {
        return polarisHeader().shadowRootCreateByCss(WebComponent.class, "sn-polaris-menu").toShadowRootToBeAttached();
    }

    public WebComponent contextualMenu() {
        return polarisHeader().shadowRootCreateByCss(WebComponent.class, "sn-contextual-menu").toShadowRootToBeAttached();
    }

    WebComponent polarisMenuShadowTest() {
        return create().byCss(WebComponent.class, "macroponent-f51912f4c700201072b211d4d8c26010").toShadowRootToBeAttached();
    }

    private WebComponent polarisMenuCollapsibleList() {
        return polarisMenu().shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list").shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list");
    }

    private WebComponent polarisCanvasAppShellLayout() {
        return polarisCanvasAppShellRoot().shadowRootCreateByCss(WebComponent.class, "sn-canvas-appshell-layout");
    }

    private WebComponent polarisCanvasAppShellRoot() {
        return polarisMainMacroponent().shadowRootCreateByCss(WebComponent.class, "sn-canvas-appshell-root");
    }

    ShadowRoot polarisMainMacroponent() {
        return create().byXPath(ShadowRoot.class, "//*[contains(name(),'macroponent')]");
    }

    public Button leftNavigationItemByPath(List<String> pathSegment) {
        var locator = String.join("", pathSegment.stream().map(e -> String.format("//li[.//*[text()='%s']]", e)).toList()) + String.format("//*[text()='%s']", pathSegment.get(pathSegment.size() - 1));

        if (pathSegment.size() > 1) {
            locator = locator.replaceFirst("\\.", "./a");
        }

        return create().byXPath(Button.class, locator).toBeClickable();
    }

    public List<Button> allLeftNavigationItemByPath(List<String> pathSegment) {
        var xpathLocator = String.join("", pathSegment.stream().map(e -> String.format("//li[.//*[text()='%s']]", e)).toList()) + String.format("//*[text()='%s']", pathSegment.get(pathSegment.size() - 1));

        if (pathSegment.size() > 1) {
            xpathLocator = xpathLocator.replaceFirst("\\.", "./a");
        }

        return create().allByXPath(Button.class, xpathLocator);
    }

    public int countLeftNavigationItemsByPath(List<String> path) {
        int countOccurrences = 0;
        List<Button> list = new ArrayList<>();
        var isPolarisEnabled = ConfigurationService.get(ServiceNowProjectSettings.class).getIsPolarisEnabled();
        if (isPolarisEnabled) {
            var allCollapsibleLists = polarisMenu().shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

            for (var collapsibleList : allCollapsibleLists) {

                var collapsableListTitleElement = collapsibleList.shadowRootCreateByCss(Div.class, "span.label");
                if (collapsableListTitleElement.getText().equals(path.get(0))) {
                    var innerCollapsibleItemsLevel = collapsibleList.shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

                    for (var innerCollapsibleItemsLevelElement : innerCollapsibleItemsLevel) {
                        var innerCollapsableListTitleElement = innerCollapsibleItemsLevelElement.shadowRootCreateByCss(Div.class, "span.label");
                        if (innerCollapsableListTitleElement.getText().equals(path.get(1))) {
                            var innerList = innerCollapsibleItemsLevelElement.shadowRootCreateAllByClass(WebComponent.class, "snf-collapsible-list-holder");

                            for (var innerListElement : innerList) {
                                var innerListTitleElement = innerListElement.createByClass(Div.class, "filter-match");
                                if (innerListTitleElement.getText().equals(path.get(2))) {
                                    ++countOccurrences;
                                }
                            }
                        }
                    }
                }
            }
        }
        return countOccurrences;
    }

    public List<List<String>> getCollapsiblePaths() {
        var browser = InstanceFactory.create(BrowserService.class);
        List<List<String>> resultList = new ArrayList<>();
        var isPolarisEnabled = ConfigurationService.get(ServiceNowProjectSettings.class).getIsPolarisEnabled();
        if (isPolarisEnabled) {

            browser.waitUntil(x -> !polarisMenu().shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list").isEmpty());
            var allCollapsibleLists = polarisMenu().shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

            for (var collapsibleList : allCollapsibleLists) {
                List<String> currentList = new ArrayList<>();
                browser.waitUntil(x -> !Objects.isNull(collapsibleList.shadowRootCreateByCss(Div.class, "span.label")));
                var collapsableListTitleElement = collapsibleList.shadowRootCreateByCss(Div.class, "span.label");
                currentList.add(collapsableListTitleElement.getText());

                WebComponent currentCollapsibleComponent = collapsibleList;
                while (true) {
                    WebComponent finalCurrentCollapsibleComponent = currentCollapsibleComponent;
                    browser.waitUntil(x -> !Objects.isNull(finalCurrentCollapsibleComponent.shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list")));
                    var innerCollapsibleItemsLevel = currentCollapsibleComponent.shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

                    if (!innerCollapsibleItemsLevel.isEmpty()) {
                        for (var innerCollapsibleItemsLevelElement : innerCollapsibleItemsLevel) {
                            browser.waitUntil(x -> innerCollapsibleItemsLevelElement.shadowRootCreateByCss(WebComponent.class, "span.label").toBeVisible(5, 2).isVisible());
                            var innerCollapsableListTitleElement = innerCollapsibleItemsLevelElement.shadowRootCreateByCss(Div.class, "span.label");
                            currentList.add(innerCollapsableListTitleElement.getText());
                            currentCollapsibleComponent = innerCollapsibleItemsLevelElement;
                            continue;
                        }
                    } else {
                        break;
                    }
                }
                var currentComponent = currentCollapsibleComponent;
                browser.waitUntil(x -> !Objects.isNull(currentComponent.shadowRootCreateAllByClass(WebComponent.class, "snf-collapsible-list-holder")));

                var innerList = currentCollapsibleComponent.shadowRootCreateAllByClass(WebComponent.class, "snf-collapsible-list-holder");

                for (var innerListElement : innerList) {
                    browser.waitUntil(x -> innerListElement.createByClass(Div.class, "filter-match").toBeVisible(5, 2).isVisible());
                    var innerListTitleElement = innerListElement.createByClass(Div.class, "filter-match");
                    currentList.add(innerListTitleElement.getText());
                }

                resultList.add(currentList);
            }
        }

        return resultList;
    }

    public List<Div> listMenuElements(String menuElement) {
        var xpathLocator = String.format("//div[@class='sn-widget-list-title'][text()='%s']", menuElement);
        return create().allByXPath(Div.class, xpathLocator);
    }

    public Button paymentTypeButton(String paymentType) {
        var xpathLocator = String.format("//a/span[contains(text(),'%s')]", paymentType);
        return create().byXPath(Button.class, xpathLocator);
    }

    public List<Div> listLeftNavigationItemsByPath(String locator) {
        return create().allByXPath(Div.class, locator).stream().toList();
    }

    public Button leftNavigationItemByFullPath(String... pathSegmentArg) {
        var pathSegment = Arrays.stream(pathSegmentArg).toList();
        var xpathLocator = ServiceNowPage.leftNavigationItemLocatorByFullPath(pathSegment);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Frame dialogFrame() {
        return create().byId(Frame.class, "dialog_frame");
    }

    public Frame templateFrame() {
        return create().byId(Frame.class, "templateIframe");
    }

    public List<Label> loginWithoutSessionLabel() {
        return create().allByXPath(Label.class, "//div[@class='text_caption' and text()]");
    }

    public List<Label> loggedInExpiredSessionLabel() {
        return create().allByXPath(Label.class, "//h2[contains(text(),'System Security')]");
    }

    public Div myWorkElement() {
        return create().byXPath(Div.class, "//a[text()='My Work']");
    }

    public WebComponent impersonationEndButtonShadowRoot() {
        return polarisImpersonationNowModal().shadowRootCreateByCss(WebComponent.class, "now-button-iconic");
    }

    public Anchor polarisEndImpersonateUserAnchor() {
        return polarisHeader().shadowRootCreateByCss(Anchor.class, "button[models-id='unimpersonate']>div[class='user-menu-label polaris-enabled']");
    }

    public Anchor polarisMenuItemChoice(String label) {
        String cssSelector = String.format("a[aria-label='%s'][class*='highlighted-item'] span > span.label > mark", label);
        return polarisMenuCollapsibleList().shadowRootCreateByCss(Anchor.class, cssSelector);
    }

    public Button nowIcon() {
        return impersonationEndButtonShadowRoot().shadowRootCreateByCss(Button.class, "now-icon");
    }

    public Button polarisMenuDevices() {
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, ".is-main-menu.is-pinned.can-animate")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list[dir='ltr']")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list[class='nested-item']")
            .shadowRootCreateByCss(Button.class, "div[class='snf-collapsible-list']>div[class='snf-collapsible-list-holder']>ul>li>span>a[aria-label='Devices']");
    }

    public Anchor polarisMenuItemInLeftNav(String searchMenu) {
        var cssLocator = String.format("li a[aria-label='%s']", searchMenu);
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-menu")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list")
            .shadowRootCreateByCss(Anchor.class, cssLocator);
    }

    public Button tableSearchDropdown() {
        return create().byXPath(Button.class, "//span[@class='nav navbar-left']//select[@class='form-control default-focus-outline']");
    }

    public Button tableSearchOption(String option) {
        var xpathLocator = String.format("//span[@class='nav navbar-left']//select[@class='form-control default-focus-outline']/option[contains(text(),'%s')]", option);
        return create().byXPath(Button.class, xpathLocator);
    }

    public TextArea tableInputField() {
        return create().byXPath(TextArea.class, "//input[@placeholder='Search' and @class='form-control']");
    }

    public Button infoIconFirstRow() {
        return create().byXPath(Button.class, "(//td[contains(@class,'list_decoration_cell') and contains(@class,'col-small')])[2]");
    }

    public Button openRecord() {
        return create().byXPath(Button.class, "//a[@class='btn btn-sm btn-default pop-over-button pull-right' and text()='Open Record']");
    }

    public Button filterButton() {
        return create().byXPath(Button.class, "//div[@class='navbar-header']/a[@role='button']");
    }

    public Button firstFilterDropdownButton(String row) {
        return create().byXPath(Button.class, "(//td/table[@role='presentation']//td[@id='field'])[" + row + "]");
    }

    public TextArea firstFilterDropdownInnerRow() {
        return create().byXPath(TextArea.class, "//div[@id='select2-drop']//input[@role='combobox']");
    }

    public Button secondFilterDropdownButton(String row) {
        return create().byXPath(Button.class, "(//td/table[@role='presentation']//td[@id='oper']/select)[" + row + "]");
    }

    public Select secondFilterDropdown(String row) {
        return create().byXPath(Select.class, "(//td[@id='oper']/select)[" + row + "]");
    }

    public TextArea thirdFilterTextBox(String row) {
        return create().byXPath(TextArea.class, "(//td[@id='value']/input)[" + row + "]");
    }

    public Button andButton() {
        return create().byXPath(Button.class, "//div[@role='toolbar']/button[@alt='Add AND condition']");
    }

    public Button runButton() {
        return create().byXPath(Button.class, "//button[@id='test_filter_action_toolbar_run']");
    }

    public Button orButton() {
        return create().byXPath(Button.class, "//button[@id='test_filter_action_toolbar_or']");
    }

    public Button removeButton() {
        return create().byXPath(Button.class, "//td[@class='sn-filter-top condition-row__remove-cell']/button");
    }

    public TextInput polarisMenuFilterShadow() {
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, ".is-main-menu.is-pinned.can-animate")
            .shadowRootCreateByCss(TextInput.class, "#filter");
    }

    public TextInput polarisMenuFilterShadowOption(String option) {
        return polarisMenuShadowTest().shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout")
            .shadowRootCreateByCss(WebComponent.class, "sn-polaris-header")
            .shadowRootCreateByCss(WebComponent.class, ".is-main-menu.is-pinned.can-animate")
            .shadowRootCreateByCss(WebComponent.class, "sn-collapsible-list")
            .shadowRootCreateByCss(TextInput.class, "div:nth-child(1) > div:nth-child(2) > ul:nth-child(1) > li:nth-child(" + option + ") > span:nth-child(1) > a:nth-child(1) > span:nth-child(1) > span:nth-child(1)");
    }

    public Button infoIconRow(String row) {
        var xpathLocator = String.format("(//td[@class='list_decoration_cell col-small col-center ' and @rowspan='1'])['%s']", row);
        return create().byXPath(Button.class, xpathLocator);
    }

    public Anchor tableRow(String number) {
        var xpathLocator = String.format("(//tr[@class='list_row list_odd'])['%s']", number);
        return create().byXPath(Anchor.class, xpathLocator);
    }

    public Div sectionByName(String sectionName) {
        var xpathLocator = String.format("//div[@class[contains(string(),'sn-widget-list_dense')]]/descendant::span[text()='%s']", sectionName);
        return create().byXPath(Div.class, xpathLocator);
    }

    public Button closeModalButton() {
        return polarisMainMacroponent().createByXPath(Button.class, ".//button[contains(@aria-label,'Close dialog')]");
    }
}