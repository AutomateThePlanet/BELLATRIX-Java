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

import java.util.List;

public class WebSettings {
    @Getter @Setter private String baseUrl;
    @Getter @Setter private String executionType;
    @Getter @Setter private String defaultLifeCycle;
    @Getter @Setter private String defaultBrowser;

    @Getter @Setter private List<GridSettings> gridSettings;

    @Getter @Setter private int artificialDelayBeforeAction;
    @Getter @Setter private TimeoutSettings timeoutSettings;

    @Getter @Setter private Boolean automaticallyScrollToVisible;
    @Getter @Setter private Boolean waitUntilReadyOnElementFound;
    @Getter @Setter private Boolean waitForAngular;
    @Getter @Setter private Boolean shouldHighlightElements;
    @Getter @Setter private Boolean shouldCaptureHttpTraffic;

    @Getter @Setter private Boolean screenshotsOnFailEnabled;
    @Getter @Setter private String screenshotsSaveLocation;

    @Getter @Setter private Boolean videosOnFailEnabled;
    @Getter @Setter private String videosSaveLocation;
}
