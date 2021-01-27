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

package solutions.bellatrix.configuration;

import lombok.Getter;
import lombok.Setter;

public class TimeoutSettings {
    @Getter @Setter private int waitForAjaxTimeout;
    @Getter @Setter private int waitUntilReadyTimeout;
    @Getter @Setter private int waitForJavaScriptAnimationsTimeout;
    @Getter @Setter private int waitForAngularTimeout;
    @Getter @Setter private int waitForPartialUrl;
    @Getter @Setter private int sleepInterval;
    @Getter @Setter private int elementToBeVisibleTimeout;
    @Getter @Setter private int elementToExistTimeout;
    @Getter @Setter private int elementToNotExistTimeout;
    @Getter @Setter private int elementToBeClickableTimeout;
    @Getter @Setter private int elementNotToBeVisibleTimeout;
    @Getter @Setter private int elementToHaveContentTimeout;
}
