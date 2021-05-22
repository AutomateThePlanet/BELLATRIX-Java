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

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.configuration.TimeoutSettings;
import solutions.bellatrix.web.configuration.WebSettings;
import solutions.bellatrix.web.infrastructure.DriverService;
import solutions.bellatrix.web.services.BrowserService;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComponentValidator {
    private final TimeoutSettings timeoutSettings = ConfigurationService.get(WebSettings.class).getTimeoutSettings();
    private final BrowserService browserService = new BrowserService();
    public final static EventListener<ComponentActionEventArgs> VALIDATING_ATTRIBUTE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    public void defaultValidateAttributeIsNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
        waitUntil(() -> (supplier.get() == null), String.format("The %s's %s should be null but was '%s'.", component.getComponentName(), attributeName, supplier), component, attributeName, "null", supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
        waitUntil(() -> (supplier.get() != null), String.format("The %s's %s shouldn't be null but was.", component.getComponentName(), attributeName), component, attributeName, "not null", (Supplier<String>)() -> "null", "not be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIsSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
        waitUntil(() -> !StringUtils.isEmpty(supplier.get()), String.format("The %s's %s shouldn't be empty but was.", component.getComponentName(), attributeName), component, attributeName, "set", (Supplier<String>)() -> "not set", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
        waitUntil(() -> StringUtils.isEmpty(supplier.get()), String.format("The %s's %s should be null but was '%s'.", component.getComponentName(), attributeName, supplier), component, attributeName, "not set", supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIsOld(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().strip().equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier), component, attributeName, value, supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().strip().equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier), component, attributeName, value, supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<Number> supplier, Number value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier), component, attributeName, value.toString(), supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeContains(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().strip().contains(value), String.format("The %s's %s should contain '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier), component, attributeName, value, supplier, "contain");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> !supplier.get().strip().contains(value), String.format("The %s's %s shouldn't contain '%s' but was '%s'.", component.getComponentName(), attributeName, value, supplier), component, attributeName, value, supplier, "not contain");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeTrue(WebComponent component, BooleanSupplier supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is %s", component.getComponentName(), attributeName)));
        waitUntil(supplier, String.format("The %s should be '%s' but wasn't.", component.getComponentName(), attributeName), component, attributeName, "true", (Supplier<String>)() -> "false", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is %s", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeFalse(WebComponent component, BooleanSupplier supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is not %s", component.getComponentName(), attributeName)));
        waitUntil(() -> !supplier.getAsBoolean(), String.format("The %s should be '%s' but was.", component.getComponentName(), attributeName), component, attributeName, "false", (Supplier<String>)() -> "true", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is not %s", component.getComponentName(), attributeName)));
    }

    private <T> void waitUntil(BooleanSupplier condition, String exceptionMessage, WebComponent component, String attributeName, String value, Supplier<T> supplier, String prefix) {
        try {
            var validationTimeout = timeoutSettings.getValidationsTimeout() > 0 ? timeoutSettings.getValidationsTimeout() : 1;
            var sleepInterval = timeoutSettings.getSleepInterval() > 0 ? timeoutSettings.getSleepInterval() : 1;
            long start = System.currentTimeMillis();
            while (!condition.getAsBoolean()) {
                if (System.currentTimeMillis() - start > validationTimeout * 1000) {
                    throw new TimeoutException("Validation failed: tried for " + validationTimeout + (validationTimeout > 1 ? " seconds" : " second") + " checking every " + sleepInterval + (sleepInterval > 1 ? " seconds" : " second"));
                }
                retryFind(component, Duration.ofSeconds(sleepInterval));
            }
        } catch (TimeoutException ex) {
            var error = String.format("\u001B[0mThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
                            "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
                            "  %" + prefix.length() + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
                            "Test failed on URL: \u001B[1m%s\u001B[0m",
                    attributeName, component.getComponentClass().getSimpleName(), component.getFindStrategy(),
                    prefix, value, "", supplier.get().toString().replaceAll("%n", "%n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
                    browserService.getUrl());
            Log.error("%n%n%s%n%n", error);
            throw new AssertionError(error, new TimeoutException(ex));
        }
    }

    @SneakyThrows
    private void retryFind(WebComponent component, Duration pollingEvery) {
        Thread.sleep(pollingEvery.toMillis());
        component.findElement();
    }
}
