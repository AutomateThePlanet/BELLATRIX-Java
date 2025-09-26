package solutions.bellatrix.servicenow.pages.uib.sections.menuHeader;

import solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage;
import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;
import solutions.bellatrix.web.pages.WebSection;

import java.util.List;

public class MenuHeaderSection extends WebSection {
    BaseUIBPage baseUIBPage;

    public enum MenuItems {
        All,
        Favorites,
        History,
        Workspaces,
        Admin,
    }

    public MenuHeaderSection(BaseUIBPage parentPage) {
        baseUIBPage = parentPage;
    }

    public ShadowRoot getWrapper() {
        return baseUIBPage.base().createByCss(ShadowRoot.class, "div.header-bar > sn-polaris-header");
    }

    public Button getMenuItem(MenuItems menuItem) {
        var itemFound = getAllButtons().stream().filter(b -> b.getText().contains(menuItem.name())).findFirst();
        if (itemFound.isPresent()) {
            return itemFound.get();
        } else {
            throw new RuntimeException("Menu Item with text %s not found.".formatted(menuItem));
        }
    }

    public void clickMenuItem(MenuItems menuItem) {
        getMenuItem(menuItem).click();
    }

    private List<Button> getAllButtons() {
        return getWrapper().createAllByXPath(Button.class, "//div[@class='sn-polaris-navigation polaris-header-menu']/div[text()]");
    }
}
