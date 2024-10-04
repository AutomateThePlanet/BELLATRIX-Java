package solutions.bellatrix.core.plugins.junit;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import solutions.bellatrix.core.plugins.PluginExecutionEngine;

public class GlobalTestListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        PluginExecutionEngine.postAfterAll();
    }
}