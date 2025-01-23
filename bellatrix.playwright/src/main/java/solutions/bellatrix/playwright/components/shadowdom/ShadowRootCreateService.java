/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.playwright.components.shadowdom;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.playwright.components.common.create.RelativeCreateService;
import solutions.bellatrix.playwright.components.shadowdom.ShadowRoot;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.findstrategies.FindStrategy;
import solutions.bellatrix.playwright.findstrategies.ShadowXpathFindStrategy;
import solutions.bellatrix.playwright.findstrategies.XpathFindStrategy;

import java.util.ArrayList;
import java.util.List;

public class ShadowRootCreateService extends RelativeCreateService {
    public ShadowRootCreateService(ShadowRoot baseComponent, EventListener<ComponentActionEventArgs> creating, EventListener<ComponentActionEventArgs> created) {
        super(baseComponent, creating, created);
    }

    @Override
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        wrappedBrowser().getCurrentPage().waitForLoadState();

        TComponent newComponent = ShadowDomService.createFromShadowRoot(componentClass, (ShadowRoot)baseComponent, findStrategy);

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return newComponent;
    }

    @Override
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        wrappedBrowser().getCurrentPage().waitForLoadState();

        List<TComponent> componentList = ShadowDomService.createAllFromShadowRoot(componentClass, (ShadowRoot)baseComponent, findStrategy);

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return componentList;
    }
}
