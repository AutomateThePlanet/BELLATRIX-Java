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
import solutions.bellatrix.playwright.components.shadowdom.ShadowDomService;
import solutions.bellatrix.playwright.components.shadowdom.ShadowRoot;
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

        if (inShadowContext()) {
            newComponent = ShadowDomService.createInShadowContext(componentClass, (WebComponent)baseComponent, findStrategy);
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

        if (inShadowContext()) {
            componentList = ShadowDomService.createAllInShadowContext(componentClass, (WebComponent)baseComponent, findStrategy);
        } else {
            var elements = findStrategy.convert(baseComponent.getWrappedElement()).all();

            for (var element : elements) {
                var component = createFromParentComponent(componentClass, findStrategy, element);

                componentList.add(component);
            }
        }

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return componentList;
    }

    private boolean inShadowContext() {
        var component = (WebComponent)baseComponent;

        while (component != null) {
            if (component instanceof ShadowRoot) return true;
            component = component.getParentComponent();
        }

        return false;
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
