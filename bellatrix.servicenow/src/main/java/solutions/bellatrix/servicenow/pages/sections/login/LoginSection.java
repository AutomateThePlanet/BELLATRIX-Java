package solutions.bellatrix.servicenow.pages.sections.login;


import solutions.bellatrix.servicenow.data.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.utilities.InstancesUrlGeneration;
import solutions.bellatrix.servicenow.utilities.UserInteraction;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.web.pages.WebPage;

public class LoginSection extends WebPage<Map, Asserts> {
    public void login() {
        navigate().to(InstancesUrlGeneration.getLoginUrl());
        map().usernameInput().setText(SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getUserName()));
        map().passwordInput().setPassword(SecretsResolver.getSecret(ConfigurationService.get(ServiceNowProjectSettings.class).getPassword()));
        map().logInButton().click();
        browser().waitForAjax();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
        UserInteraction.wait2Seconds();
    }

    public void login(String userName, String password) {
        navigate().to(InstancesUrlGeneration.getLoginUrl());
        map().usernameInput().setText(userName);
        map().passwordInput().setPassword(password);
        map().logInButton().click();
        browser().waitUntilPageLoadsCompletely();
        browser().waitForAjax();
    }

    @Override
    protected String getUrl() {
        return InstancesUrlGeneration.getLoginUrl();
    }
}