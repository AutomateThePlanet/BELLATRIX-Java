package O5_LeftNavigation.data.leftNavigationSection;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.servicenow.data.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

import java.util.ArrayList;
import java.util.List;

public class Map extends solutions.bellatrix.servicenow.pages.baseServiceNowRecordViewPage.Map {
    WebComponent polarisMenu() {
        return create().byXPath(ShadowRoot.class, "//*[contains(name(),'macroponent')]").shadowRootCreateByCss(WebComponent.class, "sn-polaris-layout").toShadowRootToBeAttached().shadowRootCreateByCss(ShadowRoot.class, "sn-polaris-header").toShadowRootToBeAttached().shadowRootCreateByCss(WebComponent.class, "sn-polaris-menu").toShadowRootToBeAttached();
    }

    public List<List<String>> getCollapsiblePaths(){
        List<List<String>> resultList = new ArrayList<>();
        var isPolarisEnabled = ConfigurationService.get(ServiceNowProjectSettings.class).getIsPolarisEnabled();
        if (isPolarisEnabled) {
            var allCollapsibleLists = polarisMenu().shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

            for (var collapsibleList : allCollapsibleLists) {
                List<String> currentList= new ArrayList<>();
                var collapsableListTitleElement = collapsibleList.shadowRootCreateByCss(Div.class, "span.label");
                currentList.add(collapsableListTitleElement.getText());

                WebComponent currentCollapsibleComponent = collapsibleList;
                while(true){
                    var innerCollapsibleItemsLevel = currentCollapsibleComponent.shadowRootCreateAllByCss(WebComponent.class, "sn-collapsible-list");

                    if(innerCollapsibleItemsLevel.size()!=0) {
                        for (var innerCollapsibleItemslevelElement : innerCollapsibleItemsLevel) {
                            var innerCollapsableListTitleElement = innerCollapsibleItemslevelElement.shadowRootCreateByCss(Div.class, "span.label");
                            currentList.add(innerCollapsableListTitleElement.getText());
                            currentCollapsibleComponent = innerCollapsibleItemslevelElement;
                            continue;
                        }
                    }else{
                        break;
                    }
                }

                var innerList = currentCollapsibleComponent.shadowRootCreateAllByClass(WebComponent.class, "snf-collapsible-list-holder");

                for (var innerListElement : innerList) {
                    var innerListTitleElement = innerListElement.createByClass(Div.class, "filter-match");
                    currentList.add(innerListTitleElement.getText());
                }

                resultList.add(currentList);
            }
        }
        return resultList;
    }
}
