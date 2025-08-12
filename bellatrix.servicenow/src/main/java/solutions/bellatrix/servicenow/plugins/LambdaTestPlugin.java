package solutions.bellatrix.servicenow.plugins;

import java.lang.reflect.Method;
import lombok.SneakyThrows;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.services.App;

public class LambdaTestPlugin extends Plugin {

    private App app() {
        return new App();
    }

    @Override
    @SneakyThrows
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        var executionType = ConfigurationService.get(WebSettings.class).getExecutionType();
        boolean isLambdaTestRun = executionType.equals("lambdatest");

//        String errorMessage = "Check Report Portal for detailed information.";
//        if (failedTestException != null) {
//            errorMessage = StringEscapeUtils.escapeJava(failedTestException.getMessage());
//        }
        try {
            if (isLambdaTestRun && testResult == TestResult.SUCCESS) {
                var result = app().script().execute("lambda-status=passed");
                if (result != null) {
                    Log.info(result.toString());
                }
            } else if (isLambdaTestRun) {
                app().script().execute("lambda-exceptions", failedTestException);
                var result = app().script().execute("lambda-status=failed");
                if (result != null) {
                    Log.info(result.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void beforeTestFailed(Exception e) throws Exception {
        var executionType = ConfigurationService.get(WebSettings.class).getExecutionType();
        boolean isLambdaTestRun = executionType.equals("lambdatest");
//        String errorMessage = "Check Report Portal for detailed information.";
//        if (e != null) {
//            errorMessage = StringEscapeUtils.escapeJava(e.getMessage());
//        }

        try {
            if (isLambdaTestRun) {
                app().script().execute("lambda-exceptions", e);
                var result = app().script().execute("lambda-status=failed");
                if (result != null) {
                    Log.info(result.toString());
                }
            }
        } catch (Exception innerException) {
            innerException.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void afterClassFailed(Exception e) {
        var executionType = ConfigurationService.get(WebSettings.class).getExecutionType();
        boolean isLambdaTestRun = executionType.equals("lambdatest");
//        String errorMessage = "Check Report Portal for detailed information.";
//        if (e != null) {
//            errorMessage = StringEscapeUtils.escapeJava(e.getMessage());
//        }

        try {
            if (isLambdaTestRun) {
                app().script().execute("lambda-exceptions", e);
                var result = app().script().execute("lambda-status=failed");
                if (result != null) {
                    Log.info(result.toString());
                }
            }
        } catch (Exception innerException) {
            innerException.printStackTrace();
        }
    }
}