package O5_LeftNavigation.data.leftNavigationSection;

import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.servicenow.contracts.ServiceNowLeftNavigationItem;

import java.util.Arrays;

public class Asserts extends solutions.bellatrix.servicenow.pages.baseServiceNowRecordViewPage.Asserts <Map> {
    public <Item extends ServiceNowLeftNavigationItem> void assertLeftNavigationItemNotPresentByPath(Item... navigationItem) {
        var expectedPathItems =  Arrays.stream(navigationItem).map(ServiceNowLeftNavigationItem::getText).toList();
        var actualPath =  map().getCollapsiblePaths();
        Assertions.assertEquals(false, actualPath.contains(expectedPathItems));
    }
}
