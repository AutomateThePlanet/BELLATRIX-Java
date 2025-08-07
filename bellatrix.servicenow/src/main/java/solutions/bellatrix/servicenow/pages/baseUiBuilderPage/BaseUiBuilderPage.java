package solutions.bellatrix.servicenow.pages.baseUiBuilderPage;

import solutions.bellatrix.servicenow.contracts.Entity;
import solutions.bellatrix.servicenow.contracts.ServiceNowTable;
import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;
import solutions.bellatrix.web.pages.WebPage;
import solutions.bellatrix.servicenow.utilities.generators.BaseInstancesUrlGeneration;

public abstract class BaseUiBuilderPage<MapT extends Map, AssertsT extends Asserts<MapT>> extends WebPage<MapT, AssertsT> {
    private String url;

    protected String getUrl() {
        return url != null ? url : BaseInstancesUrlGeneration.getSnNewRecordBaseUrl(getServiceNowProjectTable());
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    protected ServiceNowTable getServiceNowProjectTable() {
        return null;
    }

    protected ServiceNowPage serviceNowPage() {
        return app().createPage(ServiceNowPage.class);
    }

    @Override
    public void waitForPageLoad() {
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void saveRecord() {
        map().recordSaveButton().scrollToVisible();
        map().recordSaveButton().click();
        waitForPageLoad();
    }
}