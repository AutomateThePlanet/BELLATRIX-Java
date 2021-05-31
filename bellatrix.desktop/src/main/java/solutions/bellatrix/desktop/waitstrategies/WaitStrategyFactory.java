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

package solutions.bellatrix.desktop.waitstrategies;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.desktop.configuration.DesktopSettings;
import solutions.bellatrix.desktop.configuration.TimeoutSettings;

public class WaitStrategyFactory {
    private final TimeoutSettings timeoutSettings;

    public WaitStrategyFactory() {
        timeoutSettings = ConfigurationService.get(DesktopSettings.class).getTimeoutSettings();
    }

    public ToExistWaitStrategy exist() {
        return new ToExistWaitStrategy(timeoutSettings.getElementToExistTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToExistWaitStrategy exist(long timeoutInterval, long sleepInterval) {
        return new ToExistWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToBeVisibleWaitStrategy beVisible(long timeoutInterval, long sleepInterval) {
        return new ToBeVisibleWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToBeVisibleWaitStrategy beVisible() {
        return new ToBeVisibleWaitStrategy(timeoutSettings.getElementToBeVisibleTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToBeClickableWaitStrategy beClickable(long timeoutInterval, long sleepInterval) {
        return new ToBeClickableWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToBeClickableWaitStrategy beClickable() {
        return new ToBeClickableWaitStrategy(timeoutSettings.getElementToBeClickableTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToNotBeVisibleWaitStrategy notBeVisible(long timeoutInterval, long sleepInterval) {
        return new ToNotBeVisibleWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToNotBeVisibleWaitStrategy notBeVisible() {
        return new ToNotBeVisibleWaitStrategy(timeoutSettings.getElementNotToBeVisibleTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToNotExistWaitStrategy notExist(long timeoutInterval, long sleepInterval) {
        return new ToNotExistWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToNotExistWaitStrategy notExist() {
        return new ToNotExistWaitStrategy(timeoutSettings.getElementToNotExistTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToHaveContentWaitStrategy haveContent(long timeoutInterval, long sleepInterval) {
        return new ToHaveContentWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToHaveContentWaitStrategy haveContent() {
        return new ToHaveContentWaitStrategy(timeoutSettings.getElementToHaveContentTimeout(), timeoutSettings.getSleepInterval());
    }
}
