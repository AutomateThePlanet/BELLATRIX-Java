package solutions.bellatrix.servicenow.pages.baseUiBuilderPage;

import solutions.bellatrix.web.pages.PageAsserts;

public class Asserts<MapT extends Map> extends PageAsserts<MapT> {
    @SuppressWarnings("resource")
    public void assertUrlStartsWith(String expectedUrl) {
        app().browser().assertLandedOnPage(expectedUrl);
    }
}