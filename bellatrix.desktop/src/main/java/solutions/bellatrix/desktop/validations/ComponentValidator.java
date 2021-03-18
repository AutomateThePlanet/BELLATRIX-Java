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

package solutions.bellatrix.desktop.validations;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.desktop.components.ComponentActionEventArgs;
import solutions.bellatrix.desktop.components.DesktopComponent;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.infrastructure.DriverService;

import java.util.function.Function;

public class ComponentValidator {
    private final DesktopSettings desktopSettings = ConfigurationService.get(DesktopSettings.class);

    public void defaultValidateAttributeIsNull(DesktopComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property == null), String.format("The control's %s should be null but was '%s'.", attributeName, property));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is null", attributeName)));
    }

    public void defaultValidateAttributeNotNull(DesktopComponent component, Object property, String attributeName) {
        waitUntil((d) -> (property != null), String.format("The control's %s shouldn't be null but was.", attributeName));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is set", attributeName)));
    }

    public void defaultValidateAttributeIsSet(DesktopComponent component, String property, String attributeName) {
        waitUntil((d) -> !StringUtils.isEmpty(property), String.format("The control's %s shouldn't be empty but was.", attributeName));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is set", attributeName)));
    }

    public void defaultValidateAttributeNotSet(DesktopComponent component, String property, String attributeName) {
        waitUntil((d) -> StringUtils.isEmpty(property), String.format("The control's %s should be null but was '%s'.", attributeName, property));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is null", attributeName)));
    }

    public void defaultValidateAttributeIs(DesktopComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().equals(value), String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, property));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s is '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeIs(DesktopComponent component, Number property, Number value, String attributeName) {
        waitUntil((d) -> property.equals(value), String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, property));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validate %s is '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeContains(DesktopComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().contains(value), String.format("The control's %s should contain '%s' but was '%s'.", attributeName, value, property));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s contains '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeNotContains(DesktopComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> !property.strip().contains(value), String.format("The control's %s shouldn't contain '%s' but was '%s'.", attributeName, value, property));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s doesn't contain '%s'", attributeName, value)));
    }

    public void defaultValidateAttributeTrue(DesktopComponent component, boolean property, String attributeName) {
        waitUntil((d) -> property, String.format("The control should be '%s' but wasn't.", attributeName));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate is %s", attributeName)));
    }

    public void defaultValidateAttributeFalse(DesktopComponent component, boolean property, String attributeName) {
        waitUntil((d) -> !property, String.format("The control should be '%s' but was.", attributeName));
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate not %s", attributeName)));
    }

    private void waitUntil(Function<SearchContext, Boolean> waitCondition, String exceptionMessage) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), desktopSettings.getTimeoutSettings().getValidationsTimeout(), desktopSettings.getTimeoutSettings().getSleepInterval());
        try {
            webDriverWait.until(waitCondition);
        } catch (TimeoutException ex) {
            DebugInformation.printStackTrace(ex);
            throw ex;
        }
    }
}
