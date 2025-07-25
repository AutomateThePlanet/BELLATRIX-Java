package O3_RecordForm.data.recordPage;

import solutions.bellatrix.web.components.Div;

public class Map extends solutions.bellatrix.servicenow.pages.baseServiceNowRecordViewPage.Map{
    public Div mainForm() {
        var xpathLocator = "//div[@aria-label='Incident form section']";
        return create().byXPath(Div.class, xpathLocator);
    }
}
