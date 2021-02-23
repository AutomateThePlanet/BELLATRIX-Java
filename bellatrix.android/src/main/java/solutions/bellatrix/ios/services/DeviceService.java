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

package solutions.bellatrix.ios.services;

import org.openqa.selenium.Rotatable;
import org.openqa.selenium.ScreenOrientation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DeviceService extends MobileService {
    public String getCurrentActivity() {
        return getWrappedAndroidDriver().currentActivity();
    }

    public LocalDateTime getDeviceTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(getWrappedAndroidDriver().getDeviceTime(), formatter);
        return dateTime;
    }

    public ScreenOrientation getScreenOrientation() {
        return getWrappedAndroidDriver().getOrientation();
    }

    public void rotate(ScreenOrientation newOrientation) {
        ((Rotatable)getWrappedAndroidDriver()).rotate(newOrientation);
    }

    public Boolean isLocked() {
        return getWrappedAndroidDriver().isDeviceLocked();
    }

    public void lock() {
        getWrappedAndroidDriver().lockDevice();
    }

    public void unlock() {
        getWrappedAndroidDriver().unlockDevice();
    }

    public void turnOnLocationService() {
        getWrappedAndroidDriver().toggleLocationServices();
    }

    public void openNotifications() {
        getWrappedAndroidDriver().openNotifications();
    }

    public void setSetting(String setting, Object value) {
        getWrappedAndroidDriver().setSetting(setting, value);
    }

    public Map<String, Object> getSetting() {
        return getWrappedAndroidDriver().getSettings();
    }
}
