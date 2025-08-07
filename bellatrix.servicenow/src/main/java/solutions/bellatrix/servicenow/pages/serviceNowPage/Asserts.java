package solutions.bellatrix.servicenow.pages.serviceNowPage;

import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.servicenow.contracts.ServiceNowLeftNavigationItem;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowUser;
import solutions.bellatrix.web.pages.PageAsserts;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unused")
public class Asserts extends PageAsserts<Map> {
    public void assertLeftNavigationContains(String section, String... labels) {
        assertLeftNavigationContains(section, Arrays.stream(labels).toList());
    }

    public void assertLeftNavigationContains(String section, List<String> labels) {
        for (String label : labels) {
            map().leftNavigatorElement(section, label).validateIsVisible();
        }
    }

    public void assertLeftNavigationContains(List<String> labels) {
        for (String label : labels) {
            map().polarisMenuItems(label).validateIsVisible();
        }
    }

    public void assertLeftNavigationContainsTable(String label) {
        map().polarisMenuItemInLeftNav(label).validateIsVisible();
    }

    public void assertRoleExistsInImpersonateDropDownMenu(ServiceNowUser user) {
        map().userInfoDropDown().click();
        map().impersonateDropDownOption().click();
        map().searchUserToImpersonate().click();
        map().searchUserInput().setText(user.getValue());
        map().selectedUser().validateIsVisible();
        map().selectedUser().validateTextContains(user.getValue());
    }

    @SafeVarargs
    public final <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemNotPresent(Item... navigationItem) {
        assertLeftNavigationItemNotPresent(Arrays.stream(navigationItem).toList());
    }

    @SafeVarargs
    public final <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemNotPresent(int expectedCount, Item... navigationItem) {
        assertLeftNavigationItemNotPresent(expectedCount, Arrays.stream(navigationItem).toList());
    }

    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemNotPresent(List<Item> navigationItems) {
        assertLeftNavigationItemNotPresent(0, navigationItems);
    }

    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemNotPresentByPath(Item... navigationItem) {
        var expectedPathItems =  Arrays.stream(navigationItem).map(ServiceNowLeftNavigationItem::getText).toList();
        var actualPath =  map().getCollapsiblePaths();
        assertEquals(false, actualPath.contains(expectedPathItems));
    }

    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemPresentByPath(Item... navigationItem) {
        var expectedPathItems =  Arrays.stream(navigationItem).map(i -> i.getText()).toList();
        var actualPath =  map().getCollapsiblePaths();
        Boolean areItemsPresent = false;
        for (int i = 0; i < actualPath.size() ; i++) {
            if(actualPath.get(i).size() == expectedPathItems.size()) {
                for (int j = 0; j < actualPath.get(i).size(); j++) {
                    if (expectedPathItems.get(j).equals(actualPath.get(i).get(j))) {
                        areItemsPresent= true;
                    } else {
                        areItemsPresent= false;
                        break;
                    }
                }
                if(areItemsPresent){
                    break;
                }
            }
        }
        assertEquals(true, areItemsPresent);
    }

    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemNotPresent(int expectedCount, List<Item> navigationItems) {
        var itemsPresentCount = map().allLeftNavigationItemByPath(navigationItems.stream().map(ServiceNowLeftNavigationItem::getText).toList()).size();

        assertEquals(expectedCount, itemsPresentCount, "Item Present in left navigation menu but it shouldn't be");
    }

    @SafeVarargs
    public final <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemPresent(Item... navigationItem) {
        assertLeftNavigationItemPresent(Arrays.stream(navigationItem).toList());
    }

    @SafeVarargs
    public final <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemPresent(int expectedCount, Item... navigationItem) {
        assertLeftNavigationItemPresent(expectedCount, Arrays.stream(navigationItem).toList());
    }

    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemPresent(List<Item> navigationItems) {
        assertLeftNavigationItemPresent(1, navigationItems);
    }

    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemPresent(int expectedCount, List<Item> navigationItems) {
        var itemsPresentCount = map().allLeftNavigationItemByPath(navigationItems.stream().map(ServiceNowLeftNavigationItem::getText).toList()).size();

        assertEquals(expectedCount, itemsPresentCount, "Item Present in left navigation menu but it shouldn't be");
    }

    public void assertDeviceInstallStatusDifferentFromRetired(String actualStatus) {
        Assertions.assertNotEquals(actualStatus, "Retired", "The Device install Status is 'Retired'!");
    }

    public void assertElementFromPolarisMenu(String option, String expectedText) {
        var actualText = map().polarisMenuFilterShadowOption(option).getText();

        assertEquals(expectedText, actualText, "Menu Item is not equal to expected! ");
    }

    public void assertValuesInLeftNav() {
        var actualDashboards = map().polarisMenuFilterShadowOption("1").getText();
        var actualDiscoveryQueue = map().polarisMenuFilterShadowOption("2").getText();
        var actualQueueLogs = map().polarisMenuFilterShadowOption("3").getText();
        var actualAllMatchingDevices = map().polarisMenuFilterShadowOption("4").getText();
        var actualClinicalDevicesPending = map().polarisMenuFilterShadowOption("5").getText();
        var actualGAMDevicesPendingVerification = map().polarisMenuFilterShadowOption("6").getText();
        var actualFacilitiesDevicesPending = map().polarisMenuFilterShadowOption("7").getText();
        var actualWorkOrdersPending = map().polarisMenuFilterShadowOption("8").getText();

        Assertions.assertAll("Assertions for Values in left Nav:",
            () -> Assertions.assertEquals("Dashboards", actualDashboards, "The Dashboard does not exist in left Nav!"),
            () -> Assertions.assertEquals("Discovery Queue", actualDiscoveryQueue, "The Discovery Queue does not exist in left Nav!"),
            () -> Assertions.assertEquals("Queue Logs", actualQueueLogs, "The Queue Logs does not exist in left Nav!"),
            () -> Assertions.assertEquals("All Unmatched Devices", actualAllMatchingDevices, "The All Unmatched Devices does not exist in left Nav!"),
            () -> Assertions.assertEquals("Clinical Devices Pending Verification/Onboarding", actualClinicalDevicesPending, "The Clinical Devices Pending Verification/Onboarding does not exist in left Nav!"),
            () -> Assertions.assertEquals("GAM Devices Pending Verification/Onboarding", actualGAMDevicesPendingVerification, "The GAM Devices Pending Verification/Onboarding does not exist in left Nav!"),
            () -> Assertions.assertEquals("Facilities Devices Pending Verification/Onboarding", actualFacilitiesDevicesPending, "The Facilities Devices Pending Verification/Onboarding does not exist in left Nav!"),
            () -> Assertions.assertEquals("Work Orders for pending devices", actualWorkOrdersPending, "The Work Orders for pending devices does not exist in left Nav!"));
    }

    public void assertLeftNavigationContainsSection(String sectionName) {
        map().sectionByName(sectionName).isVisible();
    }
}