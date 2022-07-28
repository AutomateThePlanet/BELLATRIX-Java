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

package solutions.bellatrix.ios.services;

import solutions.bellatrix.core.utilities.RuntimeInformation;

import java.time.Duration;

public class AppService extends MobileService {
    public String getContext() {
        return getWrappedIOSDriver().getContext();
    }

    public void setContext(String name) {
        getWrappedIOSDriver().context(name);
    }

    public void backgroundApp(int seconds) {
        getWrappedIOSDriver().runAppInBackground(Duration.ofSeconds(seconds));
    }

    public void closeApp() {
        getWrappedIOSDriver().closeApp();
    }

    public void launchApp() {
        getWrappedIOSDriver().launchApp();
    }

    public void resetApp() {
        getWrappedIOSDriver().resetApp();
    }

    public void installApp(String appPath) {
        if (RuntimeInformation.IS_MAC) {
            appPath = appPath.replace('\\', '/');
        }

        getWrappedIOSDriver().installApp(appPath);
    }

    public void removeApp(String appId) {
        getWrappedIOSDriver().removeApp(appId);
    }

    public boolean isAppInstalled(String bundleId) {
        try {
            return getWrappedIOSDriver().isAppInstalled(bundleId);
        } catch (Exception e) {
            return false;
        }
    }
}
