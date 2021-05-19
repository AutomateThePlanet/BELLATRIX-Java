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

package solutions.bellatrix.web.waitstrategies;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.TimeoutSettings;
import solutions.bellatrix.web.configuration.WebSettings;

public class WaitStrategyFactory {
    private final TimeoutSettings timeoutSettings;

    public WaitStrategyFactory() {
        timeoutSettings = ConfigurationService.get(WebSettings.class).getTimeoutSettings();
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

    public ToBeVisibleWaitStrategy notBeVisible(long timeoutInterval, long sleepInterval) {
        return new ToBeVisibleWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToBeVisibleWaitStrategy notBeVisible() {
        return new ToBeVisibleWaitStrategy(timeoutSettings.getElementNotToBeVisibleTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToBeClickableWaitStrategy notExist(long timeoutInterval, long sleepInterval) {
        return new ToBeClickableWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToBeClickableWaitStrategy notExist() {
        return new ToBeClickableWaitStrategy(timeoutSettings.getElementToNotExistTimeout(), timeoutSettings.getSleepInterval());
    }

    public ToBeClickableWaitStrategy haveContent(long timeoutInterval, long sleepInterval) {
        return new ToBeClickableWaitStrategy(timeoutInterval, sleepInterval);
    }

    public ToBeClickableWaitStrategy haveContent() {
        return new ToBeClickableWaitStrategy(timeoutSettings.getElementToHaveContentTimeout(), timeoutSettings.getSleepInterval());
    }
}
