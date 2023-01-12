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

package solutions.bellatrix.desktop.waitstrategies;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.findstrategies.FindStrategy;
import solutions.bellatrix.desktop.infrastructure.DriverService;

import java.util.function.Function;

public class ToBeDisabledWaitStrategy extends WaitStrategy {
    public ToBeDisabledWaitStrategy() {
        timeoutInterval = ConfigurationService.get(DesktopSettings.class).getTimeoutSettings().getElementToBeVisibleTimeout();
        sleepInterval = ConfigurationService.get(DesktopSettings.class).getTimeoutSettings().getSleepInterval();
    }

    public static ToBeDisabledWaitStrategy of() {
        return new ToBeDisabledWaitStrategy();
    }

    public ToBeDisabledWaitStrategy(long timeoutIntervalSeconds, long sleepIntervalSeconds) {
        super(timeoutIntervalSeconds, sleepIntervalSeconds);
    }

    @Override
    public <TFindStrategy extends FindStrategy> void waitUntil(TFindStrategy findStrategy) {
        Function<WebDriver, Boolean> func = (w) -> elementIsDisabled(DriverService.getWrappedDriver(), findStrategy);
        waitUntil(func);
    }

    private <TFindStrategy extends FindStrategy> boolean elementIsDisabled(WindowsDriver searchContext, TFindStrategy findStrategy) {
        var element = findStrategy.findElement(searchContext);
        try {
            return element != null && !element.isEnabled();
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return false;
        }
    }
}
