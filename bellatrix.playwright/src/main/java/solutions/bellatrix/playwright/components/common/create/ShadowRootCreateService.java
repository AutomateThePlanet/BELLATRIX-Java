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

package solutions.bellatrix.playwright.components.common.create;

import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.playwright.components.ShadowRoot;
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

        TComponent newComponent;

        if (findStrategy instanceof XpathFindStrategy) {
            var shadowXpathStrategy = getShadowXpath(findStrategy);

            newComponent = createFromParentComponent(componentClass, shadowXpathStrategy);

        } else {
            newComponent = createFromParentComponent(componentClass, findStrategy);
        }

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return newComponent;
    }

    @Override
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        wrappedBrowser().getCurrentPage().waitForLoadState();

        List<TComponent> componentList = new ArrayList<>();

        if (findStrategy instanceof XpathFindStrategy) {
            var shadowXpathStrategies = getShadowXpathList(findStrategy);

            for (var strategy : shadowXpathStrategies) {
                var component = createFromParentComponent(componentClass, strategy);

                componentList.add(component);
            }
        }
        else {
            var elements = findStrategy.convert(baseComponent.getWrappedElement()).all();

            for (var element : elements) {
                var component = createFromParentComponent(componentClass, findStrategy, element);

                componentList.add(component);
            }
        }

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return componentList;
    }

    private ShadowXpathFindStrategy getShadowXpath(FindStrategy findStrategy) {
        // We get the absolute xpath of the new component, and we convert it to css locator
        var cssLocator = HtmlService.convertXpathToAbsoluteCssLocator(((ShadowRoot)baseComponent).getHtml(), findStrategy.getValue());

        return new ShadowXpathFindStrategy(findStrategy.getValue(), cssLocator);
    }

    private List<ShadowXpathFindStrategy> getShadowXpathList(FindStrategy findStrategy) {
        // We get the absolute xpath of the new components, and we convert them to css locators
        var cssLocators = HtmlService.convertXpathToAbsoluteCssLocators(((ShadowRoot)baseComponent).getHtml(), findStrategy.getValue());

        List<ShadowXpathFindStrategy> strategies = new ArrayList<>();

        for (var locator : cssLocators) {
            strategies.add(new ShadowXpathFindStrategy(findStrategy.getValue(), locator));
        }

        return strategies;
    }
}
