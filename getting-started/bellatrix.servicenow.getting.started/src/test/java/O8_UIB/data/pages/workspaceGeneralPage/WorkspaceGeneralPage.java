package O8_UIB.data.pages.workspaceGeneralPage;

import org.junit.jupiter.api.Assertions;
import solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceGeneralPage extends BaseUIBPage<Map, Asserts> {
    public void assertSidebarButtons() {
        var expectedButtons = new ArrayList<String>(List.of(
            LeftSidebarSection.MenuItems.DASHBOARD.getId()
        ));

        var sidebarButtons = sidebar.getMenuItems().stream().map(m-> m.getWrappedElement().getAttribute("id")).toList();

        Assertions.assertEquals(expectedButtons, sidebarButtons, "Buttons are not as expected.");
    }

    public void assertTabButtons() {
        var expectedTabs = new ArrayList<String>(List.of(
            "Home"
        ));

        var tabsButtons = tabs.getAllTabs().stream().map(m-> m.getText()).toList();

        Assertions.assertEquals(expectedTabs, tabsButtons, "Tabs are not as expected.");
    }
}
