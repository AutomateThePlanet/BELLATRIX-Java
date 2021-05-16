package solutions.bellatrix.core.plugins.junit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import solutions.bellatrix.core.plugins.TestResult;

public class TestResultListener implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS);
        TestWatcher.super.testSuccessful(context);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE);
        TestWatcher.super.testFailed(context, cause);
    }
}
