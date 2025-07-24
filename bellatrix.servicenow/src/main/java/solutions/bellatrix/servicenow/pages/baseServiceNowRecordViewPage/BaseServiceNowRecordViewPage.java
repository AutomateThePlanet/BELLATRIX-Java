package solutions.bellatrix.servicenow.pages.baseServiceNowRecordViewPage;

import solutions.bellatrix.servicenow.components.data.ServiceNowForm;
import solutions.bellatrix.servicenow.components.data.enums.AdditionalView;
import solutions.bellatrix.servicenow.components.serviceNow.SnDefaultComponent;
import solutions.bellatrix.servicenow.contracts.Entity;
import solutions.bellatrix.servicenow.contracts.FieldLabel;
import solutions.bellatrix.servicenow.contracts.ServiceNowProjectTable;
import solutions.bellatrix.servicenow.data.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;
import solutions.bellatrix.servicenow.pages.serviceNow.ServiceNowPage;
import solutions.bellatrix.servicenow.snSetupData.annotations.snFieldAnnotations.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.SneakyThrows;
import org.openqa.selenium.interactions.Actions;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.servicenow.snSetupData.annotations.snFieldAnnotations.Id;
import solutions.bellatrix.web.components.CheckBox;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.pages.WebPage;
import solutions.bellatrix.servicenow.utilities.BaseInstancesUrlGeneration;
import solutions.bellatrix.servicenow.utilities.UserInteraction;

public abstract class BaseServiceNowRecordViewPage<MapT extends Map, AssertsT extends Asserts<MapT>> extends WebPage<MapT, AssertsT> {
    private final Boolean isPolarisEnabled;
    private String url;

    public BaseServiceNowRecordViewPage() {
        isPolarisEnabled = ConfigurationService.get(ServiceNowProjectSettings.class).getIsPolarisEnabled();
    }

    protected Actions getActions() {
        return new Actions(app().browser().getWrappedDriver());
    }

    public static boolean hasAriaDisabledAncestorOrSelf(CheckBox checkBox) {
        var xpathLocator = "./ancestor-or-self::*[@aria-disabled='true']";
        var disabledAncestor = checkBox.createAllByXPath(CheckBox.class, xpathLocator);

        return disabledAncestor.size() > 0;
    }

    private static <Form extends ServiceNowForm> List<Field> getNotNullFields(Form formModel) {
        return Arrays.stream(formModel.getClass().getDeclaredFields()).filter(f -> {
            try {
                f.setAccessible(true);
                return f.get(formModel) != null;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @SneakyThrows
    private static <SnClass extends SnDefaultComponent> Method getSetTextMethod(Class<SnClass> snClass) {
        List<Method> methodContainingClass = new ArrayList<>();
        Class<?> currentClass = snClass;
        while (methodContainingClass.size() == 0) {
            methodContainingClass.addAll(Arrays.stream(currentClass.getDeclaredMethods()).filter(x -> {
                x.setAccessible(true);
                return x.getName().equals("getSetTextParamClass");
            }).toList());

            currentClass = currentClass.getSuperclass();
        }

        var setTextParamType = methodContainingClass.get(0).invoke(snClass.getConstructor().newInstance());
        return snClass.getDeclaredMethod("setText", (Class<?>) setTextParamType);
    }

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

    public void open() {
        waitForPageLoad();
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    public void open(ServiceNowProjectTable serviceNowProjectTable) {
        url = BaseInstancesUrlGeneration.getSnNewRecordBaseUrl(serviceNowProjectTable);
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    public List<String> columnDataByHeaderText(String columnHeader) {
        return map().columnDataByHeader(columnHeader).stream().map(Div::getText).toList();
    }

    public void open(String sysId, java.util.Map<String, String> queryParams) {
        url = BaseInstancesUrlGeneration.getSnRecordBaseUrl(getServiceNowProjectTable(), sysId, queryParams);
        navigate().to(url);
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
        serviceNowPage().switchToInnerFrame();
    }

    public void clickInnerTableNavigationByTabText(String tabText) {
        map().bottomTablesLabelsButton(tabText).click();
    }

    @Deprecated
    public void open(ApiEntity entity) {
        url = BaseInstancesUrlGeneration.getSnRecordBaseUrl(getServiceNowProjectTable(), entity.getSysId());
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    //todo: preferable way
    public void open(Entity entity) {
        var url = BaseInstancesUrlGeneration.getSnRecordBaseUrl(getServiceNowProjectTable(), entity);
        setUrl(url);
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    @Deprecated(since = "Due to poor design decision", forRemoval = true)
    public void open(ApiEntity entity, boolean isPolaris) {
        url = BaseInstancesUrlGeneration.getSnRecordBaseUrl(getServiceNowProjectTable(), entity.getSysId());
        super.open();
        if (!isPolaris) {
            browser().switchToFrame(map().innerFrame());
        }
    }

    public void open(Entity entity, AdditionalView additionalViews) {
        var params = new HashMap<String, String>();
        params.put("sysparm_view", additionalViews.toString());
        open(entity.getEntityId(), params);
    }

    //for deprication to poor design
    //PlancTests
    @Deprecated(since = "Due to poor design decision", forRemoval = true)
    public void openByTable(ServiceNowProjectTable table, ApiEntity entity) {
        url = BaseInstancesUrlGeneration.getSnRecordBaseUrl(table, entity.getSysId());
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    @Deprecated(since = "Due to poor design decision", forRemoval = true)
    public void open(ServiceNowProjectTable table, Entity entity) {
        url = BaseInstancesUrlGeneration.getSnRecordBaseUrl(table, entity.getEntityId());
        super.open();
        serviceNowPage().switchToInnerFrame();
    }

    @Override
    public void waitForPageLoad() {
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public <Label extends FieldLabel> void setInputByLabel(Label label, String text) {
        setInputByLabel(label.getLabel(), text);
        browser().waitForAjax();
    }

    public void setInputByLabel(String labelName, String text) {
        map().takeInput(labelName).setText(text);
        browser().waitForAjax();
    }

    public void clickOnButtonInRightNavigationBar(String buttonName) {
        var button = map().buttonInRightNavigationBarByText(buttonName);
        button.toBeClickable().waitToBe();
        button.click();
        browser().waitForAjax();
    }

    public void clickBackButton() {
        map().backButton().click();
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
    }

    public <Label extends FieldLabel> void pickOptionFromSelectInput(Label selectLabel, String selectOption) {
        pickOptionFromSelectInput(selectLabel.getLabel(), selectOption);
    }

    public void pickOptionFromSelectInput(String selectLabel, String selectOption) {
        map().selectFormInput(selectLabel).selectByText(selectOption);
        browser().waitForAjax();
    }

    public void submitForm() {
        map().submitButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void updateForm() {
        map().updateButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void updateFormBottom() {
        map().updateLastButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void clickOnDeleteButton() {
        map().deleteButton().click();
        browser().waitForAjax();
    }

    public void cancelDeletion() {
        map().popUpConfirmationCancelButton().click();
        browser().waitForAjax();
    }

    public void submitDeletion() {
        map().popUpConfirmationDeleteButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void clickOnBackButton() {
        map().backButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    public void attachFileToInput(String filePathAfterAttachments) {
        Path rootPath = Paths.get(System.getProperty("user.dir")).getParent();
        Path fullFilePath = Paths.get(String.valueOf(rootPath), "system-tests-customizations", "src", "main", "java", "attachments", filePathAfterAttachments);
        var filePath = fullFilePath.toFile().getPath();
        map().inputWithIdAttachFile().upload(filePath);
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
        map().viewAttachedFileAnchor().toExist().waitToBe();
    }

    public void clickNewButtonByTabLabel(String tabLabel) {
        map().newButtonByHeading(tabLabel).click();
        browser().waitForAjax();
    }

    public void clickNewButtonByPartialTabLabel(String tabLabel) {
        map().newButtonByPartialHeading(tabLabel).click();
        browser().waitForAjax();
    }

    @SneakyThrows
    public <Form extends ServiceNowForm, FormComponent extends WebComponent> void fillForm(Form formModel, FormComponent formComponent) {
        var fields = getNotNullFields(formModel);

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getAnnotations()[0].toString().contains("Disabled()")) {
                continue;
            }

            var componentClass = field.getDeclaredAnnotation(Component.class).value();
            var elementId = field.getDeclaredAnnotation(Id.class).value();
            var component = formComponent.createByXPath(componentClass, String.format(".//*[@id='%s']", elementId));

            Method setTextMethod = getSetTextMethod(componentClass);
            var setTextValue = field.get(formModel);

            setTextMethod.invoke(component, setTextValue);
        }
    }

    @SneakyThrows
    public <Form extends ServiceNowForm, FormComponent extends WebComponent> void fillFormDynamicRetention(Form formModel, FormComponent formComponent) {
        var fields = getNotNullFields(formModel);
        var parentFields = formModel.getClass().getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getAnnotations()[0].toString().contains("Disabled()") && field.getName() != "number") {
                continue;
            }

            var componentClass = field.getDeclaredAnnotation(Component.class).value();
            var elementId = field.getDeclaredAnnotation(Id.class).value();
            var component = formComponent.createByXPath(componentClass, String.format(".//*[@id='%s']", elementId));
            if (elementId.contains("number")) {
                var value = component.getText();
                var method = formModel.getClass().getMethod("setNumber", String.class);
                method.invoke(formModel, value);
            } else {
                Method setTextMethod = getSetTextMethod(componentClass);
                var setTextValue = field.get(formModel);
                setTextMethod.invoke(component, setTextValue);
                UserInteraction.wait2Seconds();
            }
        }
        for (Field field : parentFields) {
            field.setAccessible(true);

            if (field.getAnnotations()[0].toString().contains("Disabled()") && field.getName() != "number") {
                continue;
            }

            var componentClass = field.getDeclaredAnnotation(Component.class).value();
            var elementId = field.getDeclaredAnnotation(Id.class).value();
            var component = formComponent.createByXPath(componentClass, String.format(".//*[@id='%s']", elementId));
            if (elementId.contains("number")) {
                var value = component.getText();
                var method = formModel.getClass().getMethod("setNumber", String.class);
                method.invoke(formModel, value);
            } else {
                Method setTextMethod = getSetTextMethod(componentClass);
                var setTextValue = field.get(formModel);
                setTextMethod.invoke(component, setTextValue);
                UserInteraction.wait2Seconds();
            }
        }
    }

    public void validateButtonByNameIsVisible(String name) {
        map().buttonInRightNavigationBarByText(name).validateIsVisible();
    }

    public void validateAttachmentIconButtonVisible() {
        map().buttonInRightNavigationBarById("header_add_attachment").validateIsVisible();
    }

    public void openNewRecordPageByTabLabel(String label) {
        map().tabByLabel(label).click();
        map().newButtonByPartialHeading(label).click();
    }

    public void clickTabByLabel(String tabLabel) {
        map().tabByLabel(tabLabel).click();
    }

    public void clickTabByLabel(FieldLabel tabLabel) {
        map().tabByLabel(tabLabel).click();

    }

    public void clickButtonByName(String name) {
        map().buttonByName(name).click();
    }

    public void clickOnCellData(String tableName, String cellContainingText) {
        map().cellByTableAndText(tableName, cellContainingText).click();
        browser().waitForAjax();
    }

    public void saveForm() {
        map().buttonByName("Save").click();
        waitForPageLoad();
        browser().waitForAjax();
    }

    public void switchToInnerFrame() {
        if (isPolarisEnabled) {
            browser().switchToFrame(map().polarisMainPageFrame());
        } else {
            browser().switchToFrame(map().mainTableFrame());
        }
        browser().waitForAjax();
    }

    public void saveFormFromBottom() {
        map().lastSaveButton().click();
        waitForPageLoad();
    }
}