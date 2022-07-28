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

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import solutions.bellatrix.android.findstrategies.FindStrategy;
import solutions.bellatrix.android.infrastructure.DriverService;

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

    public abstract <TFindStrategy extends FindStrategy> void waitUntil(TFindStrategy findStrategy);

    protected void waitUntil(Function<WebDriver, Boolean> waitCondition) {
        var webDriverWait = new WebDriverWait(DriverService.getWrappedAndroidDriver(), timeoutInterval, sleepInterval);
        webDriverWait.until(waitCondition);
    }

    protected WebElement findElement(SearchContext searchContext, By by) {
        var element = searchContext.findElement(by);
        return element;
    }
}
