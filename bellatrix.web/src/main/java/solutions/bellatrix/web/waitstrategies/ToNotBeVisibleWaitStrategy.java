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

package solutions.bellatrix.web.waitstrategies;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.WebSettings;

public class ToNotBeVisibleWaitStrategy extends WaitStrategy {
    public ToNotBeVisibleWaitStrategy() {
        timeoutInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getElementToBeVisibleTimeout();
        sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
    }

    public ToNotBeVisibleWaitStrategy(long timeoutIntervalSeconds, long sleepIntervalSeconds) {
        super(timeoutIntervalSeconds, sleepIntervalSeconds);
    }

    public static ToNotBeVisibleWaitStrategy of() {
        return new ToNotBeVisibleWaitStrategy();
    }

    @Override
    public void waitUntil(SearchContext searchContext, By by) {
        waitUntil((x) -> elementIsInvisible(searchContext, by));
    }

    private boolean elementIsInvisible(SearchContext searchContext, By by) {
        var element = findElement(searchContext, by);
        try {
            return element != null && !element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return true;
        }
    }
}
