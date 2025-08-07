package solutions.bellatrix.servicenow.plugins.authentication;

import lombok.SneakyThrows;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.Wait;
import solutions.bellatrix.servicenow.infrastructure.configuration.ServiceNowProjectSettings;
import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;
import solutions.bellatrix.web.components.Div;
import solutions.bellatrix.web.services.App;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class AuthenticationPlugin extends Plugin {
    private final App app;

    public AuthenticationPlugin() {
        this.app = new App();
    }

    @Override
    @SneakyThrows
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        if (Arrays.stream(memberInfo.getAnnotations()).map(Annotation::annotationType).toList().contains(Authenticate.class)) {
            Authenticate configuration = getAuthenticationUserConfiguration(memberInfo);
            var currentUser = configuration.user();
            var serviceNowPage = app.createPage(ServiceNowPage.class);
            boolean isPolarisEnabled = ConfigurationService.get(ServiceNowProjectSettings.class).getIsPolarisEnabled();
            Log.info("Is polaris -> " + isPolarisEnabled);

            serviceNowPage.loginSection().login();
            Wait.retry(() -> {
                try {
                    serviceNowPage.impersonateUser(currentUser);
                } catch (Exception exception) {
                    if (isPolarisEnabled) {
                        serviceNowPage.map().nowIcon().click();
                        serviceNowPage.map().polarisAvatar().click();
                        serviceNowPage.impersonateUser(currentUser);
                    } else {
                        serviceNowPage.refreshPage();
                        serviceNowPage.impersonateUser(currentUser);
                    }
                }
            }, Exception.class);
            serviceNowPage.open();
            app.browser().waitUntilPageLoadsCompletely();
            app.browser().waitForAjax();
            if (isPolarisEnabled) {
                serviceNowPage.map().polarisAvatar().click();
                app.browser().waitForAjax();
                var shadowRoot = serviceNowPage.map().polarisHeader().shadowRootCreateByCss(Div.class, "div.user-name");
                var actualUserRole = shadowRoot.getText();
                serviceNowPage.map().polarisAvatar().click();
                Log.info("Actual user -> " + actualUserRole);
                Log.info("Current user -> " + currentUser.getValue());
                if (!actualUserRole.equals(currentUser.getValue())) {
                    throw new RuntimeException("User not authenticated successfully");
                }
            } else {
                var actualUserRole = serviceNowPage.map().userName().getText();
                if (!actualUserRole.equals(currentUser.getValue())) {
                    throw new RuntimeException("User not authenticated successfully");
                }
            }

            ExecutionContext.setUser(memberInfo.getDeclaringClass(), configuration.user());
        }
    }

    private Authenticate getAuthenticationUserConfiguration(Method memberInfo) {
        var classAppType = (Authenticate) memberInfo.getDeclaringClass().getDeclaredAnnotation(Authenticate.class);
        var methodAppType = (Authenticate) memberInfo.getDeclaredAnnotation(Authenticate.class);
        Authenticate result = Objects.requireNonNullElse(methodAppType, classAppType);
        return result;
    }
}