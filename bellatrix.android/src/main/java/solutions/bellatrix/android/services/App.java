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

import solutions.bellatrix.android.infrastructure.DriverService;
import solutions.bellatrix.core.infrastructure.BellatrixApp;

public class App implements BellatrixApp {
    private boolean disposed = false;

    public AppService appService() {
        return new AppService();
    }

    public ComponentCreateService create() {
        return new ComponentCreateService();
    }

    public ComponentWaitService waitFor() {
        return new ComponentWaitService();
    }

    public DeviceService device() {
        return new DeviceService();
    }

    public FileSystemService fileSystem() {
        return new FileSystemService();
    }

    public KeyboardService keyboard() {
        return new KeyboardService();
    }

    public TouchActionsService touch() {
        return new TouchActionsService();
    }

    public WebServiceFacade web() {
        return new WebServiceFacade();
    }

    @Override
    public void addDriverOptions(String key, String value) {
        DriverService.addDriverConfigOptions(key, value);
    }

    @Override
    public void close() {
        if (disposed) {
            return;
        }

        DriverService.close();

        disposed = true;
    }
}
