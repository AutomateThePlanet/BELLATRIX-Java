package O25_visual_regression_tracker.data;

import solutions.bellatrix.web.pages.WebPage;

public class MobileServicePage extends WebPage<MobileServiceMap, MobileServiceAssertions> {
    @Override
    public String getUrl() {
        return "https://www.automatetheplanet.com/services/mobile-automation/";
    }
}
