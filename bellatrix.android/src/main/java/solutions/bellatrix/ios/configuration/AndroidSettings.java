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

package solutions.bellatrix.ios.configuration;/*
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

import java.util.List;

public class AndroidSettings {
    @Getter @Setter private String executionType;
    @Getter @Setter private String defaultLifeCycle;
    @Getter @Setter private String defaultAppPath;
    @Getter @Setter private String defaultAppPackage;
    @Getter @Setter private String defaultAppActivity;
    @Getter @Setter private String defaultBrowser;
    @Getter @Setter private String defaultDeviceName;
    @Getter @Setter private String defaultAndroidVersion;
    @Getter @Setter private Boolean downloadDemoApps;
    @Getter @Setter private String serviceUrl;

    @Getter @Setter private List<GridSettings> gridSettings;

    @Getter @Setter private Boolean automaticallyScrollToVisible;
    @Getter @Setter private int artificialDelayBeforeAction;
    @Getter @Setter private TimeoutSettings timeoutSettings;

    @Getter @Setter private Boolean screenshotsOnFailEnabled;
    @Getter @Setter private String screenshotsSaveLocation;

    @Getter @Setter private Boolean videosOnFailEnabled;
    @Getter @Setter private String videosSaveLocation;
}
