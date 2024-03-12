/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package components.common.validate;

import components.WebComponent;
import configuration.TimeoutSettings;
import configuration.WebSettings;
import utilities.functionalinterfaces.AssertionMethod;
import utilities.functionalinterfaces.ComparatorMethod;
import lombok.experimental.UtilityClass;
import org.opentest4j.AssertionFailedError;
import utilities.DebugLogger;
import services.WebService;
import solutions.bellatrix.core.configuration.ConfigurationService;

import java.util.function.Supplier;

@UtilityClass
public class Validator extends WebService {
    private static final TimeoutSettings timeoutSettings = ConfigurationService.get(WebSettings.class).getTimeoutSettings();

    public static void validateCondition(ComparatorMethod condition) {
        boolean isConditionMet = false;

        long pollingInterval = timeoutSettings.inMilliseconds().getSleepInterval();
        long timeout = timeoutSettings.inMilliseconds().getValidationsTimeout();

        long startTime = System.currentTimeMillis();

        while (!isConditionMet && (System.currentTimeMillis() - startTime) <= timeout) {
            try {
                if (condition.evaluate()) {
                    isConditionMet = true;
                } else {
                    Thread.sleep(pollingInterval);
                }
            } catch (Exception e) {
                // ignore
            }
        }

        if (!isConditionMet) {
            throw new AssertionFailedError(String.format("Assertion failed.\n%s", condition.toString()));
        }
    }

    public static void validateCondition(ComparatorMethod condition, long timeoutInMilliseconds, long pollingIntervalInMilliseconds) {
        boolean isConditionMet = false;

        long startTime = System.currentTimeMillis();

        while (!isConditionMet && (System.currentTimeMillis() - startTime) <= (timeoutInMilliseconds)) {
            try {
                if (condition.evaluate()) {
                    isConditionMet = true;
                } else {
                    Thread.sleep(pollingIntervalInMilliseconds);
                }
            } catch (Exception e) {
                // ignore
            }
        }

        if (!isConditionMet) {
            throw new AssertionFailedError(String.format("Assertion failed.\n%s", condition.toString()));
        }
    }

    public static <T, V> void validate(AssertionMethod assertion, WebComponent component, String attributeName, V value, Supplier<T> supplier, String prefix) {
        var error = String.format("\u001B[0mThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
                        "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
                        "  %" + prefix.length() + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
                        "Test failed on URL: \u001B[1m%s\u001B[0m",
                attributeName, component.getComponentClass().getSimpleName(), component.findStrategy(),
                prefix, value, "", supplier.get().toString().replaceAll("%n", "%n" + String.format("%" + (prefix.length() + 12) + "s", " ")),
                wrappedBrowser().currentPage().url());

        DebugLogger.assertAndLogOnFail(assertion, error);
    }
}
