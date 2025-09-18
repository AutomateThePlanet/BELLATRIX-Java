package solutions.bellatrix.servicenow.pages.uib.pages.uibRecordViewPage;

public class Asserts<MapT extends Map> extends solutions.bellatrix.servicenow.pages.uib.pages.baseUIBPage.Asserts<MapT> {
    public void assertRecordHeading (String record) {
        map().recordHeading().validateTextIs(record);
    }
}
