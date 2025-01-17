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

package solutions.bellatrix.web.services;

import org.openqa.selenium.*;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.web.components.WebComponent;

public class JavaScriptService extends WebService {
    public JavaScriptService() {
        super();
    }

    public JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor)getWrappedDriver();
    }

    public <T> T genericExecute(String script, Object... args) {
        try {
            T result = (T)getJavascriptExecutor().executeScript(script, args);
            return result;
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            return null;
        }
    }

    public Object execute(String script) {
        try {
            var result = getJavascriptExecutor().executeScript(script);
            return result;
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            return "";
        }
    }

    public String execute(String frameName, String script) {
        getWrappedDriver().switchTo().frame(frameName);
        var result = (String)execute(script);
        getWrappedDriver().switchTo().defaultContent();
        return result;
    }

    public String execute(String script, Object... args) {
        try {
            var result = (String)getJavascriptExecutor().executeScript(script, args);
            return result;
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            return "";
        }
    }

    public <TComponent extends WebComponent> String execute(String script, TComponent component) {
        var result = execute(script, component.findElement());
        return result;
    }

    public String execute(String script, WebElement nativeElement) {
        try {
            var result = (String)getJavascriptExecutor().executeScript(script, nativeElement);
            return result;
        } catch (NoSuchSessionException | NoSuchWindowException ex) {
            throw ex;
        } catch (StaleElementReferenceException | NoSuchElementException ex) {
            return "";
        } catch (Exception ex) {
            DebugInformation.printStackTrace(ex);
            return "";
        }
    }
}
