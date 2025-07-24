package solutions.bellatrix.servicenow.pages.baseUiBuilderPage;

import solutions.bellatrix.servicenow.contracts.Entity;
import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;
import solutions.bellatrix.servicenow.pages.serviceNow.ServiceNowPage;
import solutions.bellatrix.web.pages.WebPage;
import solutions.bellatrix.servicenow.utilities.BaseInstancesUrlGeneration;

public abstract class BaseUiBuilderPage<MapT extends Map, AssertsT extends Asserts<MapT>> extends WebPage<MapT, AssertsT> {
    private String url;

    protected String getUrl() {
        return url != null ? url : BaseInstancesUrlGeneration.getSnNewRecordBaseUrl(getServiceNowProjectTable());
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    protected ServiceNowProjectTable getServiceNowProjectTable() {
        return null;
    }

    protected ServiceNowPage serviceNowPage() {
        return app().createPage(ServiceNowPage.class);
    }

    public void open(Entity entity) {
        url = BaseInstancesUrlGeneration.getUibRecordBaseUrl(getServiceNowProjectTable(), entity);
        super.open();
    }

    public void openNew() {
        url = BaseInstancesUrlGeneration.getUibNewRecordBaseUrl(getServiceNowProjectTable());
        super.open();
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