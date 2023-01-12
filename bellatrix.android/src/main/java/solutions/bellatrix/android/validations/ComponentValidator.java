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

package solutions.bellatrix.android.validations;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.android.components.AndroidComponent;
import solutions.bellatrix.android.components.ComponentActionEventArgs;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.android.infrastructure.DriverService;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.Log;

import java.time.Duration;
import java.util.function.Function;

public class ComponentValidator {
    private final AndroidSettings androidSettings = ConfigurationService.get(AndroidSettings.class);

    public void defaultValidateAttributeIsNull(AndroidComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property == null), String.format("The %s's %s should be null but was '%s'.", component.getComponentName(), attributeName, property), component, attributeName, "null", property.toString(), "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotNull(AndroidComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property != null), String.format("The %s's %s shouldn't be null but was.", component.getComponentName(), attributeName), component, attributeName, "not null", "null", "not be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIsSet(AndroidComponent component, String property, String attributeName) {
        waitUntil((d) -> !StringUtils.isEmpty(property), String.format("The %s's %s shouldn't be empty but was.", component.getComponentName(), attributeName), component, attributeName, "set", "not set", "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeNotSet(AndroidComponent component, String property, String attributeName) {
        waitUntil((d) -> StringUtils.isEmpty(property), String.format("The %s's %s should be null but was '%s'.", component.getComponentName(), attributeName, property), component, attributeName, "not set", property, "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeIs(AndroidComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, property), component, attributeName, value, property, "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(AndroidComponent component, Number property, Number value, String attributeName) {
        waitUntil((d) -> property.equals(value), String.format("The %s's %s should be '%s' but was '%s'.", component.getComponentName(), attributeName, value, property), component, attributeName, value.toString(), property.toString(), "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeContains(AndroidComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().contains(value), String.format("The %s's %s should contain '%s' but was '%s'.", component.getComponentName(), attributeName, value, property), component, attributeName, value, property, "contain");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(AndroidComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> !property.strip().contains(value), String.format("The %s's %s shouldn't contain '%s' but was '%s'.", component.getComponentName(), attributeName, value, property), component, attributeName, value, property, "not contain");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeTrue(AndroidComponent component, boolean property, String attributeName) {
        waitUntil((d) -> property, String.format("The %s should be '%s' but wasn't.", component.getComponentName(), attributeName), component, attributeName, "true", "false", "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is %s", component.getComponentName(), attributeName)));
    }

    public void defaultValidateAttributeFalse(AndroidComponent component, boolean property, String attributeName) {
        waitUntil((d) -> !property, String.format("The %s should be '%s' but was.", component.getComponentName(), attributeName), component, attributeName, "false", "true", "be");
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is not %s", component.getComponentName(), attributeName)));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage, AndroidComponent component, String attributeName, String value, String property, String prefix) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedAndroidDriver(), Duration.ofSeconds(androidSettings.getTimeoutSettings().getValidationsTimeout()), Duration.ofSeconds(androidSettings.getTimeoutSettings().getSleepInterval()));
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            Log.error("%n%nThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
                            "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
                            "  %" + prefix.length() + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
                            "Test failed on activity: \u001B[1m%s\u001B[0m%n%n",
                    attributeName, component.getComponentClass().getSimpleName(), component.getFindStrategy(),
                    prefix, value, "", property.replaceAll("\n", "\n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
                    DriverService.getWrappedAndroidDriver().currentActivity());
            var validationExceptionMessage = String.format("%s The test failed on activity: %s", exceptionMessage, component.getAppService().getCurrentActivity());
            throw new AssertionError(validationExceptionMessage, new TimeoutException(validationExceptionMessage, ex));
        }
    }
}
