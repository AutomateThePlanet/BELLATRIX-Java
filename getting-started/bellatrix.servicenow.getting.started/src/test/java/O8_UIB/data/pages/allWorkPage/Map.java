package O8_UIB.data.pages.allWorkPage;

import O8_UIB.data.models.ManagerWorkModel;
import solutions.bellatrix.web.components.advanced.grid.Grid;
import solutions.bellatrix.web.components.shadowdom.ShadowRoot;

public class Map extends solutions.bellatrix.servicenow.pages.uib.pages.uibTableViewPage.Map{
    @Override
    public Grid dataGrid() {
        var gridComponent = getActiveScreen()
                .createByCss(ShadowRoot.class, "now-grid")
                .createByCss(Grid.class, "table");

        gridComponent.setModelColumns(ManagerWorkModel.class);
        return gridComponent;
    }

}
