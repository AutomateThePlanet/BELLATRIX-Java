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

package solutions.bellatrix.web.configuration;/*
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

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter @Setter
public class WebSettings {
    private String baseUrl;
    private String executionType;
    private String defaultLifeCycle;
    private String defaultBrowser;
    private Integer defaultBrowserWidth = 0;
    private Integer defaultBrowserHeight = 0;
    private List<GridSettings> gridSettings;

    private int artificialDelayBeforeAction;
    private TimeoutSettings timeoutSettings;

    private Boolean automaticallyScrollToVisible;
    private Boolean waitUntilReadyOnElementFound;
    private Boolean waitForAngular;
    private Boolean shouldHighlightElements;
    private Boolean shouldCaptureHttpTraffic;
    private Boolean toastNotificationBddLogging;
    private long notificationToastTimeout;

    private ArrayList<String> consoleErrorsWhitelist;

    private Boolean screenshotsOnFailEnabled;
    private String screenshotsSaveLocation;

    private Boolean videosOnFailEnabled;
    private String videosSaveLocation;
}
