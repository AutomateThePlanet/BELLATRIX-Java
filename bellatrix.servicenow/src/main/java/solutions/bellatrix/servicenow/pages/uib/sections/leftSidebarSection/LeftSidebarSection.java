package solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.pages.WebSection;

import java.util.List;

public class LeftSidebarSection extends WebSection {
    BaseUIBPage baseUIBPage;

    public LeftSidebarSection(BaseUIBPage parentPage) {
        baseUIBPage = parentPage;
    }

    public enum MenuItems {
        DASHBOARD("dashboard"),
        LIST("list"),
        HOME("home"),
        INVENTORY("inventory"),
        ASSET_ESTATE("assetestate"),
        MODEL_MANAGEMENT("modelmanagement"),
        CONTRACT_MANAGEMENT("contractmanagement"),
        ASSET_OPERATIONS("assetoperations");

        @Getter
        @Setter
        private String id;

        MenuItems(String id) {
            setId(id);
        }
    }

    public WebComponent getWrapper() {
        return baseUIBPage.base().createByCss(WebComponent.class, "sn-canvas-toolbar#item-wsToolbar");
    }

    public Button getMenuItem(MenuItems menuItem) {
        return getWrapper().createByCss(Button.class, "sn-canvas-toolbar-button#%s".formatted(menuItem.id));
    }

    public List<Button> getMenuItems() {
        return getWrapper().createAllByCss(Button.class, "sn-canvas-toolbar-button");
    }

    public void clickDashboardMenuItem() {
        getMenuItem(MenuItems.DASHBOARD).click();
        browser().waitUntilPageLoadsCompletely();
    }

    public void clickListsMenuItem() {
        getMenuItem(MenuItems.LIST).click();
        browser().waitUntilPageLoadsCompletely();
    }
}
