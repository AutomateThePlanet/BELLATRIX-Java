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

package solutions.bellatrix.desktop.services;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.desktop.infrastructure.DriverService;
import solutions.bellatrix.desktop.pages.DesktopPage;

public class App implements AutoCloseable {
    private boolean disposed = false;

    public AppService appService() {
        return SingletonFactory.getInstance(AppService.class);
    }

    public ComponentCreateService create() {
        return SingletonFactory.getInstance(ComponentCreateService.class);
    }

    public ComponentWaitService waitFor() {
        return SingletonFactory.getInstance(ComponentWaitService.class);
    }

    public void addDriverOptions(String key, String value) {
        DriverService.addDriverOptions(key, value);
    }

    public <TPage extends DesktopPage> TPage create(Class<TPage> pageOf, Object... args)
    {
        return SingletonFactory.getInstance(pageOf, args);
    }

    @Override
    public void close() {
        if (disposed)
        {
            return;
        }

        DriverService.close();

        disposed = true;
    }
}
