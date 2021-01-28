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

package solutions.bellatrix.configuration;/*
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

import lombok.Getter;
import lombok.Setter;

public class WebSettings {
    private @Getter @Setter String baseUrl;
    private @Getter @Setter BrowserSettings chrome;
    private @Getter @Setter BrowserSettings firefox;
    private @Getter @Setter BrowserSettings edge;
    private @Getter @Setter TimeoutSettings timeoutSettings;
    private @Getter @Setter int elementWaitTimeout;
    private @Getter @Setter Boolean automaticallyScrollToVisible;
    private @Getter @Setter Boolean waitUntilReadyOnElementFound;
    private @Getter @Setter Boolean waitForAngular;
    private @Getter @Setter int artificialDelayBeforeAction;
}
