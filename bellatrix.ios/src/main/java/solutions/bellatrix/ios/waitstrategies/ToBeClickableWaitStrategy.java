/*
 * Copyright 2021 Automate The Planet Ltd.
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

package solutions.bellatrix.ios.waitstrategies;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.ios.configuration.IOSSettings;
import solutions.bellatrix.ios.findstrategies.FindStrategy;
import solutions.bellatrix.ios.infrastructure.DriverService;

import java.util.function.Function;

public class ToBeClickableWaitStrategy extends WaitStrategy {
    public ToBeClickableWaitStrategy() {
        timeoutInterval = ConfigurationService.get(IOSSettings.class).getTimeoutSettings().getElementToBeClickableTimeout();
        sleepInterval = ConfigurationService.get(IOSSettings.class).getTimeoutSettings().getSleepInterval();
    }

    public static ToBeClickableWaitStrategy of() {
        return new ToBeClickableWaitStrategy();
    }

    public ToBeClickableWaitStrategy(long timeoutIntervalSeconds, long sleepIntervalSeconds) {
       super(timeoutIntervalSeconds, sleepIntervalSeconds);
    }

    @Override
    public <TFindStrategy extends FindStrategy> void waitUntil(TFindStrategy findStrategy) {
        Function<WebDriver, Boolean> func = (w) -> elementIsClickable(DriverService.getWrappedIOSDriver(), findStrategy);
        waitUntil(func);
    }

    private <TFindStrategy extends FindStrategy> Boolean elementIsClickable(IOSDriver<MobileElement> searchContext, TFindStrategy findStrategy)
    {
        var element = findStrategy.findElement(searchContext);
        try
        {
            return element != null && element.isEnabled();
        }
        catch (StaleElementReferenceException e)
        {
            return false;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }
}
