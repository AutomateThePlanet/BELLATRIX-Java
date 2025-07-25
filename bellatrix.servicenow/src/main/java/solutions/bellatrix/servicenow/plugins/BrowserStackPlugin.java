package solutions.bellatrix.servicenow.plugins;

import java.lang.reflect.Method;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.services.App;

public class BrowserStackPlugin extends Plugin {
    private App app;

    public BrowserStackPlugin() {
        this.app = new App();
    }

    @Override
    @SneakyThrows
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        var executionType = ConfigurationService.get(WebSettings.class).getExecutionType();
        boolean isBrowserStackRun = executionType.equals("browserstack");

        String errorMessage = "Check Report Portal for detailed information.";
        if (failedTestException != null) {
            errorMessage = StringEscapeUtils.escapeJava(failedTestException.getMessage());
        }
        try {
            if (isBrowserStackRun && testResult == TestResult.SUCCESS) {
                var result = app.script().execute("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Everything is OK.'\"}}");
                if (result != null)
                    Log.info((String)result);
            } else  if (isBrowserStackRun) {
                var result = app.script().execute("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \""+ errorMessage +"\"}}");
                if (result != null)
                    Log.info((String)result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void beforeTestFailed(Exception e) throws Exception {
        var executionType = ConfigurationService.get(WebSettings.class).getExecutionType();
        boolean isBrowserStackRun = executionType.equals("browserstack");
        String errorMessage = "Check Report Portal for detailed information.";
        if (e != null) {
            errorMessage = StringEscapeUtils.escapeJava(e.getMessage());
        }

        try {
            if (isBrowserStackRun) {
                var result = app.script().execute("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \""+ errorMessage +"\"}}");
                if (result != null)
                    Log.info((String)result);
            }
        } catch (Exception innerException) {
            innerException.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void afterClassFailed(Exception e) {
        var executionType = ConfigurationService.get(WebSettings.class).getExecutionType();
        boolean isBrowserStackRun = executionType.equals("browserstack");
        String errorMessage = "Check Report Portal for detailed information.";
        if (e != null) {
            errorMessage = StringEscapeUtils.escapeJava(e.getMessage());
        }

        try {
            if (isBrowserStackRun) {
                var result = app.script().execute("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \""+ errorMessage +"\"}}");
                if (result != null)
                    Log.info((String)result);
            }
        } catch (Exception innerException) {
            innerException.printStackTrace();
        }
    }
}