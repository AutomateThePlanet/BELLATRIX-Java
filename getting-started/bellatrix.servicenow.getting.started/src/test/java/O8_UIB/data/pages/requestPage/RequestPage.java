package O8_UIB.data.pages.requestPage;

import O8_UIB.data.models.RequestItemModel;
import O8_UIB.data.models.TemplateModel;

public class RequestPage extends solutions.bellatrix.servicenow.pages.uib.pages.uibRecordViewPage.UibRecordViewPage<Map, Asserts> {
    public TemplateModel readTemplateForm() {
        waitForPageLoad();
        app().browser().waitForAjax();
        browser().waitUntil(x -> map().getTemplateForm().isVisible());
        return readForm(map().getTemplateForm(), TemplateModel.class);
    }

    public void assertTemplateForm(RequestItemModel expectedModel) {
        waitForPageLoad();
        app().browser().waitForAjax();
        browser().waitUntil(x -> map().getTemplateForm().isVisible());
        assertForm(expectedModel, map().getTemplateForm(), RequestItemModel.class);
    }

    public void fillTemplateForm(RequestItemModel formData) {
        fillForm(formData, map().getTemplateForm());
        app().browser().waitForAjax();
    }
}
