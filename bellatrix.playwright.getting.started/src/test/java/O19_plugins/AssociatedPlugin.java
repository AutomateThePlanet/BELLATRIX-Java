package O19_plugins;

import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import java.lang.reflect.Method;

public class AssociatedPlugin extends Plugin {
    // The test workflow plugins are way to execute your logic before each test. For example, taking screenshots or videos
    // on test failures, creating custom logs and so on. In the example you can find a plugin that integrates a ManualTestCase
    // annotation for the automated tests, containing the ID to the corresponding manual test case. The main purpose of
    // the test workflow is to fail the test if the ManualTestCase attribute is not set.

    @Override
    public void preBeforeTest(TestResult result, Method memberInfo) {
        validateManualTestCaseAttribute(memberInfo);
    }

    // You can override all mentioned test workflow method hooks in your custom handlers. The method uses reflection to
    // find out if the ManualTestCase annotation is set to the run test. If the attribute is not set or is set more than
    // once an exception is thrown. The logic executes before the actual test run, during the preBeforeTest phase.
    @Override
    public void beforeTestFailed(Exception e) {
        e.printStackTrace();
    }

    private void validateManualTestCaseAttribute(Method memberInfo) {
        var manualTestCase = memberInfo.getDeclaredAnnotation(ManualTestCase.class);
        if (manualTestCase == null) {
            throw new NullPointerException("No manual test case is associated with the BELLATRIX test.");
        }
        if (manualTestCase.id() <= 0) {
            throw new IllegalArgumentException("The associated manual test case ID cannot be <= 0.");
        }
    }

}