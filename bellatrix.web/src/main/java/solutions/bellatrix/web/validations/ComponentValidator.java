/*
 * Copyright 2022 Automate The Planet Ltd.
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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
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
import java.util.function.Supplier;

public class ComponentValidator {
    private final TimeoutSettings timeoutSettings = ConfigurationService.get(WebSettings.class).getTimeoutSettings();
    private final BrowserService browserService = new BrowserService();
    public final static EventListener<ComponentActionEventArgs> VALIDATING_ATTRIBUTE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    public void defaultValidateAttributeIsNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
        waitUntil(() -> (supplier.get() == null), component, attributeName, "null", supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
        waitUntil(() -> (supplier.get() != null), component, attributeName, "not null", (Supplier<String>)() -> "null", "not be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIsSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
        waitUntil(() -> !StringUtils.isEmpty(supplier.get()), component, attributeName, "set", (Supplier<String>)() -> "not set", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
        waitUntil(() -> StringUtils.isEmpty(supplier.get()), component, attributeName, "not set", supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().strip().equals(value), component, attributeName, value, supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<Number> supplier, Number value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().equals(value), component, attributeName, value.toString(), supplier, "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeContains(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> supplier.get().strip().contains(value), component, attributeName, value, supplier, "contain");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
        waitUntil(() -> !supplier.get().strip().contains(value), component, attributeName, value, supplier, "not contain");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeTrue(WebComponent component, BooleanSupplier supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is %s", component.getComponentName(), attributeName)));
        waitUntil(supplier, component, attributeName, "true", (Supplier<String>)() -> "false", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is %s", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeFalse(WebComponent component, BooleanSupplier supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is not %s", component.getComponentName(), attributeName)));
        waitUntil(() -> !supplier.getAsBoolean(), component, attributeName, "false", (Supplier<String>)() -> "true", "be");
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is not %s", component.getComponentName(), attributeName)));
    }

    private <T> void waitUntil(BooleanSupplier condition, WebComponent component, String attributeName, String value, Supplier<T> supplier, String prefix) {
        var validationTimeout = timeoutSettings.getValidationsTimeout();
        var sleepInterval = timeoutSettings.getSleepInterval();

        FluentWait<WebDriver> wait = new FluentWait<>(DriverService.getWrappedDriver())
                .withTimeout(Duration.ofSeconds(validationTimeout))
                .pollingEvery(Duration.ofSeconds(sleepInterval > 0 ? sleepInterval : 1));

        try {
            wait.until(x -> {
                component.findElement();
                return condition.getAsBoolean();
            });
        } catch (TimeoutException ex) {
            var error = String.format("The %s of %s (%s)%n" +
                            "  Should %s: \"%s\"%n" +
                            "  %" + prefix.length() + "sBut was: \"%s\"%n" +
                            "Test failed on URL: %s",
                    attributeName, component.getComponentClass().getSimpleName(), component.getFindStrategy(),
                    prefix, value, "", supplier.get().toString().replaceAll("%n", "%n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
                    browserService.getUrl());
//            var colorFormattedError = String.format("\u001B[0mThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
//                            "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
//                            "  %" + prefix.length() + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
//                            "Test failed on URL: \u001B[1m%s\u001B[0m",
//                    attributeName, component.getComponentClass().getSimpleName(), component.getFindStrategy(),
//                    prefix, value, "", supplier.get().toString().replaceAll("%n", "%n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
//                    browserService.getUrl());
            Log.error("%n%n%s%n%n", error);
            throw new AssertionError(error, ex);
        }
    }
}
