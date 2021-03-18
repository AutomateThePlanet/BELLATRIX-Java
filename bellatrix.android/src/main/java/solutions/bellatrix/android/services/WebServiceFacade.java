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

package solutions.bellatrix.android.services;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.services.*;

public class WebServiceFacade extends MobileService {
    public NavigationService navigate() {
        return SingletonFactory.getInstance(NavigationService.class);
    }

    public BrowserService browser() {
        return SingletonFactory.getInstance(BrowserService.class);
    }

    public CookiesService cookies() {
        return SingletonFactory.getInstance(CookiesService.class);
    }

    public DialogService dialogs() {
        return SingletonFactory.getInstance(DialogService.class);
    }

    public JavaScriptService script() {
        return SingletonFactory.getInstance(JavaScriptService.class);
    }

    public ComponentCreateService create() {
        return SingletonFactory.getInstance(ComponentCreateService.class);
    }

    public solutions.bellatrix.android.services.ComponentWaitService waitFor() {
        return SingletonFactory.getInstance(ComponentWaitService.class);
    }
}
