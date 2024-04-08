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

package solutions.bellatrix.playwright.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

@Getter @Setter
public class TimeoutSettings {
    private long connectToRemoteGridTimeout;
    private long pageLoadTimeout;
    private long scriptTimeout;
    private long elementWaitTimeout;
    private long waitForAjaxTimeout;
    private long waitUntilReadyTimeout;
    private long waitForJavaScriptAnimationsTimeout;
    private long waitForAngularTimeout;
    private long waitForPartialUrl;
    private long sleepInterval;
    private long validationsTimeout;
    private long elementToBeVisibleTimeout;
    private long elementToExistTimeout;
    private long elementToNotExistTimeout;
    private long elementToBeClickableTimeout;
    private long elementNotToBeVisibleTimeout;
    private long elementToHaveContentTimeout;

    public TimeoutSettings inMilliseconds() {
        return copyAndMultiplyBy1000();
    }

    @SneakyThrows
    private TimeoutSettings copyAndMultiplyBy1000() {
        TimeoutSettings clonedSettings = new TimeoutSettings();

        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == long.class) {
                field.setAccessible(true);
                long originalValue = (long) field.get(this);
                field.set(clonedSettings, originalValue * 1000);
            }
        }

        return clonedSettings;
    }
}
