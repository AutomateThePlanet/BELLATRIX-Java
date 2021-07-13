package solutions.bellatrix.core.plugins.junit;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import solutions.bellatrix.core.plugins.TestResult;

public class TestResultListener implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        if (extensionContext.getExecutionException().isPresent()) {
            BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE);
        } else {
            BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS);
        }
    }
}
