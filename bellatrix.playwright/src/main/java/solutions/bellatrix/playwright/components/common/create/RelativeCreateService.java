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
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.playwright.components.contracts.Component;
import solutions.bellatrix.playwright.findstrategies.FindStrategy;
import solutions.bellatrix.playwright.findstrategies.ShadowXpathFindStrategy;
import solutions.bellatrix.playwright.findstrategies.XpathFindStrategy;
import solutions.bellatrix.playwright.services.ComponentCreateService;

import java.util.ArrayList;
import java.util.List;

public class RelativeCreateService extends ComponentCreateService {
    public final EventListener<ComponentActionEventArgs> CREATING;
    public final EventListener<ComponentActionEventArgs> CREATED;

    protected final Component baseComponent;

    public RelativeCreateService(Component baseComponent, EventListener<ComponentActionEventArgs> creating, EventListener<ComponentActionEventArgs> created) {
        this.baseComponent = baseComponent;
        CREATING = creating;
        CREATED = created;
    }

    @Override
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        wrappedBrowser().getCurrentPage().waitForLoadState();

        TComponent newComponent;

        var ancestor = getAncestor();

        if (ancestor instanceof ShadowRoot && findStrategy instanceof XpathFindStrategy) {
            newComponent = createFromAncestorShadowRoot(componentClass, getShadowXpath((ShadowRoot)ancestor, findStrategy), (ShadowRoot)ancestor);
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

        var ancestor = getAncestor();

        List<TComponent> componentList = new ArrayList<>();

        if (ancestor instanceof ShadowRoot && findStrategy instanceof XpathFindStrategy) {
            var shadowXpathStrategies = getShadowXpathList((ShadowRoot)ancestor, findStrategy);

            for (var strategy : shadowXpathStrategies) {
                var component = createFromAncestorShadowRoot(componentClass, strategy, (ShadowRoot)ancestor);

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

    /**
     * @return Last ancestor or first ShadowRoot ancestor.
     */
    private WebComponent getAncestor() {
        var component = (WebComponent)baseComponent;

        while (component != null) {
            if (component instanceof ShadowRoot) {
                return component;
            }
            component = component.getParentComponent();
        }

        return component;
    }

    private ShadowXpathFindStrategy getShadowXpath(ShadowRoot ancestor, FindStrategy findStrategy) {
        // First, we find the absolute location of baseComponent inside the shadow DOM
        var searchContext = HtmlService.findElement(ancestor.getHtml(), baseComponent.getFindStrategy().getValue());

        // Then we use its absolute location as a start point to navigate inside the shadow DOM (XPath Axes is allowed)
        // We get the absolute xpath of the new component, and finally we convert it to css locator
        var cssLocator = HtmlService.convertXpathToCssLocator(searchContext, findStrategy.getValue());

        return new ShadowXpathFindStrategy(findStrategy.getValue(), cssLocator);
    }

    private List<ShadowXpathFindStrategy> getShadowXpathList(ShadowRoot ancestor, FindStrategy findStrategy) {
        // First, we find the absolute location of baseComponent inside the shadow DOM
        var searchContext = HtmlService.findElement(ancestor.getHtml(), baseComponent.getFindStrategy().getValue());

        // Then we use its absolute location as a start point to navigate inside the shadow DOM (XPath Axes is allowed)
        // We get the absolute xpath of the new components, and finally we convert them to css locators
        var cssLocators = HtmlService.convertXpathToCssLocators(searchContext, findStrategy.getValue());

        List<ShadowXpathFindStrategy> strategies = new ArrayList<>();

        for (var locator : cssLocators) {
            strategies.add(new ShadowXpathFindStrategy(findStrategy.getValue(), locator));
        }

        return strategies;
    }

    /**
     * Creates from the shadowRoot itself as to allow for XPath Axes queries.
     */
    private <TComponent extends WebComponent> TComponent createFromAncestorShadowRoot(Class<TComponent> componentClass, ShadowXpathFindStrategy shadowXpathStrategy, ShadowRoot ancestor) {
        var element = shadowXpathStrategy.convert(ancestor.getWrappedElement()).first();
        var newComponent = createInstance(componentClass, shadowXpathStrategy, element);
        newComponent.setParentComponent(ancestor);

        return newComponent;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createFromParentComponent(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var element = findStrategy.convert(baseComponent.getWrappedElement()).first();
        var newComponent = createInstance(componentClass, findStrategy, element);
        newComponent.setParentComponent((WebComponent)baseComponent);

        return newComponent;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createFromParentComponent(Class<TComponent> componentClass, TFindStrategy findStrategy, WebElement element) {
        var newComponent = createInstance(componentClass, findStrategy, element);
        newComponent.setParentComponent((WebComponent)baseComponent);

        return newComponent;
    }
}
