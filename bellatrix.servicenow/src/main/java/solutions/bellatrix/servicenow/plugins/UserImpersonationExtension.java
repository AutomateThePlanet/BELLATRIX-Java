package solutions.bellatrix.servicenow.plugins;

import solutions.bellatrix.servicenow.contracts.TestUser;
import solutions.bellatrix.servicenow.pages.serviceNowPage.ServiceNowPage;
import solutions.bellatrix.servicenow.infrastructure.enums.ServiceNowUser;
import java.lang.reflect.Method;
import java.util.Optional;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import solutions.bellatrix.web.services.App;
import solutions.bellatrix.servicenow.utilities.UserInteraction;

/**
 * This class is an extension for JUnit 5 that allows user impersonation during test execution. It intercepts the test method invocation and performs user impersonation if the test instance implements the TestUser interface. It uses the ServiceNowPage class to perform the loginSection and impersonation actions.
 */
public class UserImpersonationExtension implements InvocationInterceptor, Extension {
    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        var app = new App();
        var serviceNowPage = app.createPage(ServiceNowPage.class);
        serviceNowPage.loginSection().login();
        Optional<Object> testInstance = extensionContext.getTestInstance();
        if (testInstance.get() instanceof TestUser testUser) {
            var user = testUser.getUser();
            serviceNowPage.impersonateUser(user);
            UserInteraction.wait2Seconds();
        }

        invocation.proceed();
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        var app = new App();
        var serviceNowPage = app.createPage(ServiceNowPage.class);
        serviceNowPage.loginSection().login();
        Optional<Object> testInstance = extensionContext.getTestInstance();
        if (testInstance.get() instanceof TestUser testUser) {
            ServiceNowUser user = testUser.getUser();
            serviceNowPage.impersonateUser(user);
            UserInteraction.wait2Seconds();
        }

        invocation.proceed();
    }
}