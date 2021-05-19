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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.WebSettings;

public class ToExistWaitStrategy extends WaitStrategy {
    public ToExistWaitStrategy() {
        timeoutInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getElementToExistTimeout();
        sleepInterval = ConfigurationService.get(WebSettings.class).getTimeoutSettings().getSleepInterval();
    }

    public ToExistWaitStrategy(long timeoutIntervalSeconds, long sleepIntervalSeconds) {
        super(timeoutIntervalSeconds, sleepIntervalSeconds);
    }

    public static ToExistWaitStrategy of() {
        return new ToExistWaitStrategy();
    }

    @Override
    public void waitUntil(SearchContext searchContext, By by) {
        waitUntil((x) -> elementExists(searchContext, by));
    }

    private boolean elementExists(SearchContext searchContext, By by) {
        try {
            var element = findElement(searchContext, by);
            return element != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
