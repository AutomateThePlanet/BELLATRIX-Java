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

package solutions.bellatrix.waitstrategies;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.infrastructure.DriverService;

import java.util.function.Function;

public abstract class WaitStrategy {
    @Getter protected long timeoutInterval;
    @Getter protected long sleepInterval;

    public WaitStrategy() {
    }

    public WaitStrategy(long timeoutInterval, long sleepInterval) {
        this.timeoutInterval = timeoutInterval;
        this.sleepInterval = sleepInterval;
    }

    public abstract void waitUntil(SearchContext searchContext, By by);

    protected void waitUntil(Function<SearchContext, Boolean> waitCondition)
    {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedDriver(), timeoutInterval, sleepInterval);
        webDriverWait.until(waitCondition);
    }

    protected WebElement findElement(SearchContext searchContext, By by)
    {
        var element = searchContext.findElement(by);
        return element;
    }
}
