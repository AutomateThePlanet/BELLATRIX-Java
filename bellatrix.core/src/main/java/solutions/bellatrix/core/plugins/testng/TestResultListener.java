package solutions.bellatrix.core.plugins.testng;

import org.testng.ITestListener;
import org.testng.ITestResult;
import solutions.bellatrix.core.plugins.TestResult;

public class TestResultListener implements ITestListener {
    @Override
    public void onTestSuccess(ITestResult result) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE);
    }
}
