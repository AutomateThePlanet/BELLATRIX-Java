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

package solutions.bellatrix.ios.configuration;

import lombok.Getter;
import lombok.Setter;

public class TimeoutSettings {
    @Getter @Setter private long implicitWaitTimeout;
    @Getter @Setter private long elementWaitTimeout;
    @Getter @Setter private long sleepInterval;
    @Getter @Setter private long validationsTimeout;
    @Getter @Setter private long webviewConnectTimeout;
    @Getter @Setter private long elementToBeVisibleTimeout;
    @Getter @Setter private long elementToExistTimeout;
    @Getter @Setter private long elementToNotExistTimeout;
    @Getter @Setter private long elementToBeClickableTimeout;
    @Getter @Setter private long elementNotToBeVisibleTimeout;
    @Getter @Setter private long elementToHaveContentTimeout;
}
