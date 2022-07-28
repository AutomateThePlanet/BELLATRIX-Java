/*
 * Copyright 2022 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package testng;

import org.testng.Assert;
import org.testng.annotations.Test;
import solutions.bellatrix.desktop.components.Button;
import solutions.bellatrix.desktop.components.TextField;
import solutions.bellatrix.desktop.infrastructure.ExecutionApp;
import solutions.bellatrix.desktop.infrastructure.Lifecycle;
import solutions.bellatrix.desktop.infrastructure.testng.DesktopTest;

@ExecutionApp(appPath = "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App", lifecycle = Lifecycle.RESTART_ON_FAIL)
public class CalculatorTests extends DesktopTest {
    @Test
    public void addition() {
        app().create().byName(Button.class, "Five").click();
        app().create().byName(Button.class, "Plus").click();
        app().create().byName(Button.class, "Seven").click();
        app().create().byName(Button.class, "Equals").click();

        var calculatorResult = getCalculatorResultText();
        Assert.assertEquals(calculatorResult, "12");
    }

    @Test
    public void division() {
        app().create().byAccessibilityId(Button.class, "num8Button").click();
        app().create().byAccessibilityId(Button.class, "num8Button").click();
        app().create().byAccessibilityId(Button.class, "divideButton").click();
        app().create().byAccessibilityId(Button.class, "num1Button").click();
        app().create().byAccessibilityId(Button.class, "num1Button").click();
        app().create().byAccessibilityId(Button.class, "equalButton").click();

        var calculatorResult = getCalculatorResultText();
        Assert.assertEquals(calculatorResult, "8");
    }

    @Test
    public void multiplication() {
        app().create().byXPath(Button.class, "//Button[@Name='Nine']").click();
        app().create().byXPath(Button.class, "//Button[@Name='Multiply by']").click();
        app().create().byXPath(Button.class, "//Button[@Name='Nine']").click();
        app().create().byXPath(Button.class, "//Button[@Name='Equals']").click();

        var calculatorResult = getCalculatorResultText();
        Assert.assertEquals(calculatorResult, "81");
    }

    @Test
    public void subtraction() {
        app().create().byXPath(Button.class, "//Button[@AutomationId='num9Button']").click();
        app().create().byXPath(Button.class, "//Button[@AutomationId='minusButton']").click();
        app().create().byXPath(Button.class, "//Button[@AutomationId='num1Button']").click();
        app().create().byXPath(Button.class, "//Button[@AutomationId='equalButton']").click();

        var calculatorResult = getCalculatorResultText();
        Assert.assertEquals(calculatorResult, "8");
    }

    private String getCalculatorResultText() {
        return app().create().byAccessibilityId(TextField.class, "CalculatorResults").getText().replace("Display is", "").trim();
    }
}