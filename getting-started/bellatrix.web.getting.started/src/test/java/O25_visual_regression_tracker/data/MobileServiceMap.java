package O25_visual_regression_tracker.data;

import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.pages.PageMap;

public class MobileServiceMap extends PageMap {
    public Div rightPeopleSection() {
        return app().create().byId(Div.class, "right-people-section bottom-40");
    }
}
