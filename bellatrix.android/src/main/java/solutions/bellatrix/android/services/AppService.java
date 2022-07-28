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

package solutions.bellatrix.android.services;

import io.appium.java_client.android.Activity;
import solutions.bellatrix.core.utilities.RuntimeInformation;

import java.time.Duration;

public class AppService extends MobileService {
    public String getCurrentActivity() {
        return getWrappedAndroidDriver().currentActivity();
    }

    public void startActivity(
            String appPackage,
            String appActivity,
            String appWaitPackage,
            String appWaitActivity,
            boolean stopApp) {
        try {
            getWrappedAndroidDriver().hideKeyboard();
        } catch (Exception ignore) {}
        var activity = new Activity(appPackage, appActivity);
        activity.setAppWaitActivity(appWaitActivity);
        activity.setAppWaitPackage(appWaitPackage);
        activity.setStopApp(stopApp);
        getWrappedAndroidDriver().startActivity(activity);
    }

    public void startActivityWithIntent(String appPackage, String appActivity, String intentAction, String appWaitPackage, String appWaitActivity, String intentCategory, String intentFlags, String intentOptionalArgs, boolean stopApp) {
        try {
            getWrappedAndroidDriver().hideKeyboard();
        } catch (Exception ignore) {}
        var activity = new Activity(appPackage, appActivity);
        activity.setAppWaitActivity(appWaitActivity);
        activity.setAppWaitPackage(appWaitPackage);
        activity.setStopApp(stopApp);
        activity.setIntentCategory(intentCategory);
        activity.setIntentFlags(intentFlags);
        activity.setOptionalIntentArguments(intentOptionalArgs);
        getWrappedAndroidDriver().startActivity(activity);
    }

    public String getContext() {
        return getWrappedAndroidDriver().getContext();
    }

    public void setContext(String name) {
        getWrappedAndroidDriver().context(name);
    }

    public void backgroundApp(int seconds) {
        getWrappedAndroidDriver().runAppInBackground(Duration.ofSeconds(seconds));
    }

    public void closeApp() {
        getWrappedAndroidDriver().closeApp();
    }

    public void launchApp() {
        getWrappedAndroidDriver().launchApp();
    }

    public void resetApp() {
        getWrappedAndroidDriver().resetApp();
    }

    public void installApp(String appPath) {
        if (RuntimeInformation.IS_MAC) {
            appPath = appPath.replace('\\', '/');
        }

        getWrappedAndroidDriver().installApp(appPath);
    }

    public void removeApp(String appId) {
        getWrappedAndroidDriver().removeApp(appId);
    }

    public boolean isAppInstalled(String bundleId) {
        try {
            return getWrappedAndroidDriver().isAppInstalled(bundleId);
        } catch (Exception e) {
            return false;
        }
    }
}
