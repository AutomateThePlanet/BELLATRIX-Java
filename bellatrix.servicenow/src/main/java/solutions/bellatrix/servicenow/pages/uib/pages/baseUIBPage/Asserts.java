package solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage;

import solutions.bellatrix.web.pages.PageAsserts;

public class Asserts<MapT extends Map> extends PageAsserts<MapT> {
    public void assertUrlStartsWith(String expectedUrl) {
        app().browser().assertLandedOnPage(expectedUrl);
    }
}
