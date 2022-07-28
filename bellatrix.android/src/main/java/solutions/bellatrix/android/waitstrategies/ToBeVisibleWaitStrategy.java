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

package solutions.bellatrix.android.waitstrategies;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import solutions.bellatrix.android.configuration.AndroidSettings;
import solutions.bellatrix.android.findstrategies.FindStrategy;
import solutions.bellatrix.android.infrastructure.DriverService;
import solutions.bellatrix.core.configuration.ConfigurationService;

import java.util.function.Function;

public class ToBeVisibleWaitStrategy extends WaitStrategy {
    public ToBeVisibleWaitStrategy() {
        timeoutInterval = ConfigurationService.get(AndroidSettings.class).getTimeoutSettings().getElementToBeVisibleTimeout();
        sleepInterval = ConfigurationService.get(AndroidSettings.class).getTimeoutSettings().getSleepInterval();
    }

    public ToBeVisibleWaitStrategy(long timeoutIntervalSeconds, long sleepIntervalSeconds) {
        super(timeoutIntervalSeconds, sleepIntervalSeconds);
    }

    public static ToBeVisibleWaitStrategy of() {
        return new ToBeVisibleWaitStrategy();
    }

    @Override
    public <TFindStrategy extends FindStrategy> void waitUntil(TFindStrategy findStrategy) {
        Function<WebDriver, Boolean> func = (w) -> elementIsVisible(DriverService.getWrappedAndroidDriver(), findStrategy);
        waitUntil(func);
    }

    private <TFindStrategy extends FindStrategy> Boolean elementIsVisible(AndroidDriver<MobileElement> searchContext, TFindStrategy findStrategy) {
        var element = findStrategy.findElement(searchContext);
        try {
            return element != null && element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return false;
        }
    }
}
