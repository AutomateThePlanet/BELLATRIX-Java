package solutions.bellatrix.servicenow.pages.uib.pages.uibTableViewPage;

public class Asserts<MapT extends Map> extends solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.Asserts<MapT> {
    public void assertMainHeading(String heading) {
        map().mainHeading().validateTextIs(heading);
    }
}
