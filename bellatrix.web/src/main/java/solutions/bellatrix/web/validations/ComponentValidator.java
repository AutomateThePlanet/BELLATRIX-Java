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
import solutions.bellatrix.core.utilities.DebugInformation;
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
        waitUntil((d) -> (property == null), String.format("The control's %s should be null but was '%s'.", attributeName, property));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is null", attributeName)));
    }

    public void defaultValidateAttributeNotNull(WebComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property != null), String.format("The control's %s shouldn't be null but was.", attributeName));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is set", attributeName)));
    }

    public void defaultValidateAttributeIsSet(WebComponent component, String property, String attributeName) {
        waitUntil((d) -> !StringUtils.isEmpty(property), String.format("The control's %s shouldn't be empty but was.", attributeName));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is set", attributeName)));
    }

    public void defaultValidateAttributeNotSet(WebComponent component, String property, String attributeName) {
        waitUntil((d) -> StringUtils.isEmpty(property), String.format("The control's %s should be null but was '%s'.", attributeName, property));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is null", attributeName)));
    }

    public void defaultValidateAttributeIs(WebComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().equals(value), String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, property));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s is '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Number property, Number value, String attributeName) {
        waitUntil((d) -> property.equals(value), String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, property));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validate %s is '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeContains(WebComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().contains(value), String.format("The control's %s should contain '%s' but was '%s'.", attributeName, value, property));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s contains '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(WebComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> !property.strip().contains(value), String.format("The control's %s shouldn't contain '%s' but was '%s'.", attributeName, value, property));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s doesn't contain '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeTrue(WebComponent component, boolean property, String attributeName) {
        waitUntil((d) -> property, String.format("The control should be '%s' but wasn't.", attributeName));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate is %s", attributeName)));
    }

    public void defaultValidateAttributeFalse(WebComponent component, boolean property, String attributeName) {
        waitUntil((d) -> !property, String.format("The control should be '%s' but was.", attributeName));
        WebComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate not %s", attributeName)));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), webSettings.getTimeoutSettings().getValidationsTimeout(), webSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            DebugInformation.printStackTrace(ex);
            var validationExceptionMessage = String.format("%s The test failed on URL: %s", exceptionMessage, browserService.getUrl());
            throw new TimeoutException(validationExceptionMessage, ex);
        }
    }
}
