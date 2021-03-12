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

package solutions.bellatrix.desktop.components.validators;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.DebugInformation;
import solutions.bellatrix.desktop.components.ComponentActionEventArgs;
import solutions.bellatrix.desktop.components.DesktopComponent;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.infrastructure.DriverService;

import java.util.function.Function;

public class DesktopValidator {
    private final DesktopSettings desktopSettings = ConfigurationService.get(DesktopSettings.class);
    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    protected void defaultValidateAttributeIsSet(DesktopComponent component, String property, String attributeName) {
        waitUntil((d) -> !StringUtils.isEmpty(property), String.format("The control's %s shouldn't be empty but was.", attributeName));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is empty", attributeName)));
    }

    protected void defaultValidateAttributeNotSet(DesktopComponent component, String property, String attributeName) {
        waitUntil((d) -> StringUtils.isEmpty(property), String.format("The control's %s should be null but was '%s'.", attributeName, property));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validate %s is null", attributeName)));
    }

    protected void defaultValidateAttributeIs(DesktopComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().equals(value), String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, property));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s is %s", attributeName, value)));
    }

    protected void defaultValidateAttributeContains(DesktopComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> property.strip().contains(value), String.format("The control's %s should contain '%s' but was '%s'.", attributeName, value, property));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s contains %s", attributeName, value)));
    }

    protected void defaultValidateAttributeNotContains(DesktopComponent component, String property, String value, String attributeName) {
        waitUntil((d) -> !property.strip().contains(value), String.format("The control's %s shouldn't contain '%s' but was '%s'.", attributeName, value, property));
        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validate %s doesn't contain %s", attributeName, value)));
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
