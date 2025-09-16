package solutions.bellatrix.servicenow.pages.uib.pages.baseWorkspacePage;

import com.github.javafaker.Faker;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.servicenow.pages.uib.sections.leftSidebarSection.LeftSidebarSection;
import solutions.bellatrix.servicenow.pages.uib.sections.mainContentSection.MainContentSection;
import solutions.bellatrix.servicenow.pages.uib.sections.menuHeader.MenuHeaderSection;
import solutions.bellatrix.servicenow.pages.uib.sections.tabsSection.TabsSection;

public class BaseWorkspacePage<MapT extends Map, AssertsT extends Asserts<MapT>> extends solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.BaseUIBPage<MapT, AssertsT>
{
    public Faker faker = new Faker();

    public String entityId;

    public void openPage(String sysId) {
        this.entityId = sysId;
        Log.info("Navigating to URL: %s".formatted(getUrl()));
        app().navigate().to(getUrl());
    }

}
