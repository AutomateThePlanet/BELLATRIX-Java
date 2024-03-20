/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.playwright.services;

import com.microsoft.playwright.Locator;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.components.Frame;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.utilities.functionalinterfaces.EvaluationMethod;


@SuppressWarnings("resource")
public class JavaScriptService extends WebService {
    public Object execute(String script) {
        return performEvaluation(() -> wrappedBrowser().currentPage().evaluate(script));

    }

    public String execute(String frameName, String script) {
        return (String)execute(script, SingletonFactory.getInstance(ComponentCreateService.class).byName(Frame.class, frameName));
    }

    public String execute(Frame frame, String script) {
        return (String)execute(script, frame);
    }

    public String execute(String script, Object... args) {
        return (String) performEvaluation(() -> wrappedBrowser().currentPage().evaluate(script, args));
    }

    public <TComponent extends WebComponent> String execute(String script, TComponent component) {
        return execute(script, component.wrappedElement());
    }

    public String execute(String script, Locator nativeLocator) {
        return (String) performEvaluation(() -> nativeLocator.evaluate(script));
    }

    private Object performEvaluation(EvaluationMethod method) {
        try {
            return method.evaluate();
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            return "";
        }
    }
}
