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
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.infrastructure.DriverService;
import solutions.bellatrix.web.services.BrowserService;

import java.util.function.Function;
import java.util.function.Supplier;

public class ComponentValidator {
    private final WebSettings webSettings = ConfigurationService.get(WebSettings.class);
    private final BrowserService browserService = new BrowserService();
    public final static EventListener<ComponentActionEventArgs> VALIDATING_ATTRIBUTE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    public void defaultValidateAttributeIsNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
        waitUntil((d) -> (supplier.get() == null), String.format("The %s's %s should be null but was '%s'.", component.getComponentName(), attributeName, supplier.get()), component, attributeName, "null", supplier.get().toString(), "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
        waitUntil((d) -> (supplier.get() != null), String.format("The %s's %s shouldn't be null but was.", component.getComponentName(), attributeName), component, attributeName, "not null", "null", "not be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIsSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
        waitUntil((d) -> !StringUtils.isEmpty(supplier.get()), String.format("The %s's %s shouldn't be empty but was.", component.getComponentName(), attributeName), component, attributeName, "set", "not set", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
        waitUntil((d) -> StringUtils.isEmpty(supplier.get()), String.format("The %s's %s should be null but was '%s'.", component.getComponentName(), attributeName, supplier.get()), component, attributeName, "not set", supplier.get(), "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil((d) -> supplier.get().strip().equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier), component, attributeName, value, supplier.get(), "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<Number>supplier, Number value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil((d) -> supplier.get().equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier.get()), component, attributeName, value.toString(), supplier.get().toString(), "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeContains(WebComponent component, Supplier<String>supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
        waitUntil((d) -> supplier.get().strip().contains(value), String.format("The %s's %s should contain '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier.get()), component, attributeName, value, supplier.get(), "contain");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(WebComponent component, Supplier<String>supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
        waitUntil((d) -> !supplier.get().strip().contains(value), String.format("The %s's %s shouldn't contain '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier.get()), component, attributeName, value, supplier.get(), "not contain");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeTrue(WebComponent component, Supplier<Boolean>supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is %s", component.getComponentName(), attributeName)));
        waitUntil((d) -> supplier.get(), String.format("The %s should be '%s' but wasn't.", component.getComponentName(), attributeName), component, attributeName, "true", "false", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is %s", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeFalse(WebComponent component, Supplier<Boolean>supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is not %s", component.getComponentName(), attributeName)));
        waitUntil((d) -> !supplier.get(), String.format("The %s should be '%s' but was.", component.getComponentName(), attributeName), component, attributeName, "false", "true", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is not %s", component.getComponentName(), attributeName)));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage, WebComponent component, String attributeName, String value, String property, String prefix) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), webSettings.getTimeoutSettings().getValidationsTimeout(), webSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            Log.error("%n%nThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
                            "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
                            "  %" + prefix.length() + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
                            "Test failed on URL: \u001B[1m%s\u001B[0m%n%n",
                    attributeName, component.getComponentClass().getSimpleName(), component.getFindStrategy(),
                    prefix, value, "", property.replaceAll("%n", "%n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
                    browserService.getUrl());
            var validationExceptionMessage = String.format("%s The test failed on URL: %s", exceptionMessage, browserService.getUrl());
            throw new AssertionError(validationExceptionMessage, new TimeoutException(validationExceptionMessage, ex));
        }
    }
}
