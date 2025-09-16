package O8_UIB.data.pages.entityPage;

import O8_UIB.data.models.TemplateModel;

public class EntityPage extends solutions.bellatrix.servicenow.pages.uib.pages.uibRecordViewPage.UibRecordViewPage<Map, Asserts> {
    public TemplateModel readTemplateForm() {
        waitForPageLoad();
        app().browser().waitForAjax();
        browser().waitUntil(x -> map().getTemplateForm().isVisible());
        return readForm(map().getTemplateForm(), TemplateModel.class);
    }

    public void assertTemplateForm(TemplateModel expectedModel) {
        waitForPageLoad();
        app().browser().waitForAjax();
        browser().waitUntil(x -> map().getTemplateForm().isVisible());
        assertForm(expectedModel, map().getTemplateForm(), TemplateModel.class);
    }

    public void fillTemplateForm(TemplateModel formData) {
        fillForm(formData, map().getTemplateForm());
    }
}
