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

package solutions.bellatrix.mobile.services;

import solutions.bellatrix.mobile.components.MobileComponent;
import solutions.bellatrix.mobile.waitstrategies.WaitStrategy;

public class ComponentWaitService extends MobileService {
    public void wait(MobileComponent component, WaitStrategy waitStrategy) {
        if (component.getParentWrappedElement() == null) {
            waitStrategy.waitUntil(component.getFindStrategy());
        } else {
            // TODO: should be fixed to pass parent Wrapped Element?
            waitStrategy.waitUntil(component.getFindStrategy());
        }
    }
}
