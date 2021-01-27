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

package solutions.bellatrix.components;

import lombok.experimental.ExtensionMethod;
import solutions.bellatrix.components.contracts.ComponentHref;
import solutions.bellatrix.findstrategies.FindStrategy;
import solutions.bellatrix.plugins.EventListener;
import solutions.bellatrix.waitstrategies.WaitStrategyElementsExtensions;

public class Anchor extends WebComponent implements ComponentHref {
    private static EventListener<ComponentActionEventArgs> CLICKING;
    private static EventListener<ComponentActionEventArgs> CLICKED;

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getHref() {
        return null;
    }

    public void click() {
        click(CLICKING, CLICKED);
    }
}
