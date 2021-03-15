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

package solutions.bellatrix.android.infrastructure;

import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.utilities.FileDownloader;
import solutions.bellatrix.android.configuration.AndroidSettings;

public class DownloadDemoAppsPlugin extends Plugin {
    private static ThreadLocal<Boolean> areDemoAppsDownloaded;

    static {
        areDemoAppsDownloaded = new ThreadLocal<>();
    }

    public static DownloadDemoAppsPlugin of() {
        return new DownloadDemoAppsPlugin();
    }

    @Override
    public void preBeforeClass(Class type) {
        super.preBeforeClass(type);
        if (areDemoAppsDownloaded.get())
            return;
        var shouldDownloadDemoApps = ConfigurationService.get(AndroidSettings.class).getDownloadDemoApps();
        if (shouldDownloadDemoApps) {
            FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.2/selendroid-test-app-0.10.0.apk");
            FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.2/ApiDemos.apk");
        }
    }
}
