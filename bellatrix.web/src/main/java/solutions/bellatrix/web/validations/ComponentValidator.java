/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Teodor Nikolov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.validations;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.infrastructure.DriverService;
import solutions.bellatrix.web.services.BrowserService;

import java.util.function.Function;

public class ComponentValidator {
    private final WebSettings webSettings = ConfigurationService.get(WebSettings.class);
    private final BrowserService browserService = new BrowserService();

    public void defaultValidateAttributeIsNull(WebComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property == null), String.format("The %s (%s)'s %s should be null but was '%s'.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, property), component, attributeName, "null", property.toString(), "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s (%s) %s is null", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName)));
    }

    public void defaultValidateAttributeNotNull(WebComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property != null), String.format("The %s (%s)'s %s shouldn't be null but was.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName), component, attributeName, "not null", "null", "not be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s (%s) %s is set", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName)));
    }

    public void defaultValidateAttributeIsSet(WebComponent component, String property, String attributeName) {
        waitUntil((d) -> !StringUtils.isEmpty(property), String.format("The %s (%s)'s %s shouldn't be empty but was.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName), component, attributeName, "set", "not set", "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s (%s) %s is set", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName)));
    }

    public void defaultValidateAttributeNotSet(WebComponent component, String property, String attributeName) {
        waitUntil((d) -> StringUtils.isEmpty(property), String.format("The %s (%s)'s %s should be null but was '%s'.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, property), component, attributeName, "not set", property, "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s (%s) %s is null", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName)));
    }

    public void defaultValidateAttributeIs(WebComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().equals(value), String.format("The %s (%s)'s %s should be '%s' but was '%s'.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value, property), component, attributeName, value, property, "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s (%s) %s is '%s'", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Number property, Number value, String attributeName) {
        waitUntil((d) -> property.equals(value), String.format("The %s (%s)'s %s should be '%s' but was '%s'.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value, property), component, attributeName, value.toString(), property.toString(), "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validate %s (%s) %s is '%s'", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value)));
    }

    public void defaultValidateAttributeContains(WebComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().contains(value), String.format("The %s (%s)'s %s should contain '%s' but was '%s'.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value, property), component, attributeName,  value, property, "contain");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s (%s) %s contains '%s'", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(WebComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> !property.strip().contains(value), String.format("The %s (%s)'s %s shouldn't contain '%s' but was '%s'.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value, property), component, attributeName,  value, property, "not contain");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s (%s) %s doesn't contain '%s'", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName, value)));
    }

    public void defaultValidateAttributeTrue(WebComponent component, boolean property, String attributeName) {
        waitUntil((d) -> property, String.format("The %s (%s) should be '%s' but wasn't.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName), component, attributeName,  "true", "false", "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s (%s) is %s", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName)));
    }

    public void defaultValidateAttributeFalse(WebComponent component, boolean property, String attributeName) {
        waitUntil((d) -> !property, String.format("The %s (%s) should be '%s' but was.", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName), component, attributeName,  "false", "true", "be");
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s (%s) not %s", component.getComponentClass().getSimpleName(), component.getFindStrategy(), attributeName)));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), webSettings.getTimeoutSettings().getValidationsTimeout(), webSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            var validationExceptionMessage = String.format("%s The test failed on URL: %s", exceptionMessage, browserService.getUrl());
            AssertionError error = new AssertionError(validationExceptionMessage, new TimeoutException(validationExceptionMessage, ex));
            throw error;
        }
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage, WebComponent component, String attributeName, String value, String property, String prefix) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), webSettings.getTimeoutSettings().getValidationsTimeout(), webSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            Log.error("%n%nThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
                            "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
                            "  %" + prefix.length() + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
                            "Test Failed on URL: \u001B[1m%s\u001B[0m%n%n",
                    attributeName, component.getComponentClass().getSimpleName(), component.getFindStrategy(),
                    prefix, value, "", property.replaceAll("\n", "\n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
                    browserService.getUrl());
            var validationExceptionMessage = String.format("%s The test failed on URL: %s", exceptionMessage, browserService.getUrl());
            throw new AssertionError(validationExceptionMessage, new TimeoutException(validationExceptionMessage, ex));
        }
    }
}
