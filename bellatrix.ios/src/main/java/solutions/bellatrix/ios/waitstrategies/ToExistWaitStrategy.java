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

package solutions.bellatrix.ios.waitstrategies;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.ios.configuration.IOSSettings;
import solutions.bellatrix.ios.findstrategies.FindStrategy;
import solutions.bellatrix.ios.infrastructure.DriverService;

import java.util.function.Function;

public class ToExistWaitStrategy extends WaitStrategy {
    public ToExistWaitStrategy() {
        timeoutInterval = ConfigurationService.get(IOSSettings.class).getTimeoutSettings().getElementToExistTimeout();
        sleepInterval = ConfigurationService.get(IOSSettings.class).getTimeoutSettings().getSleepInterval();
    }

    public ToExistWaitStrategy(long timeoutIntervalSeconds, long sleepIntervalSeconds) {
        super(timeoutIntervalSeconds, sleepIntervalSeconds);
    }

    public static ToExistWaitStrategy of() {
        return new ToExistWaitStrategy();
    }

    @Override
    public <TFindStrategy extends FindStrategy> void waitUntil(TFindStrategy findStrategy) {
        Function<WebDriver, Boolean> func = (w) -> elementExists(DriverService.getWrappedIOSDriver(), findStrategy);
        waitUntil(func);
    }

    private <TFindStrategy extends FindStrategy> Boolean elementExists(IOSDriver<MobileElement> searchContext, TFindStrategy findStrategy) {
        try {
            var element = findStrategy.findElement(searchContext);
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }
}
