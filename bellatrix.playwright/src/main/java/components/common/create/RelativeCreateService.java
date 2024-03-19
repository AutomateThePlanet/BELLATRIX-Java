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

package components.common.create;

import components.common.ComponentActionEventArgs;
import components.WebComponent;
import components.contracts.Component;
import findstrategies.*;
import services.ComponentCreateService;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.InstanceFactory;

import java.util.ArrayList;
import java.util.List;

public class RelativeCreateService extends ComponentCreateService {
    private final EventListener<ComponentActionEventArgs> CREATING;
    private final EventListener<ComponentActionEventArgs> CREATED;

    private final Component baseComponent;

    public RelativeCreateService(Component baseComponent, EventListener<ComponentActionEventArgs> creating, EventListener<ComponentActionEventArgs> created) {
        this.baseComponent = baseComponent;
        CREATING = creating;
        CREATED = created;
    }

    @Override
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        wrappedBrowser().currentPage().waitForLoadState();
        var newComponent = InstanceFactory.create(componentClass);
        newComponent.wrappedElement(findStrategy.convert(baseComponent.wrappedElement()).first());
        newComponent.findStrategy(findStrategy);
        newComponent.parentWrappedComponent((WebComponent)baseComponent);

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return newComponent;
    }

    @Override
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        wrappedBrowser().currentPage().waitForLoadState();
        var locators = findStrategy.convert(baseComponent.wrappedElement()).all();
        List<TComponent> componentList = new ArrayList<>();

        for (var locator : locators) {
            var component = InstanceFactory.create(componentClass);
            component.wrappedElement(locator);
            component.findStrategy(findStrategy);

            componentList.add(component);
        }

        for (var component : componentList) {
            component.parentWrappedComponent((WebComponent)baseComponent);
        }

        CREATED.broadcast(new ComponentActionEventArgs((WebComponent)baseComponent));

        return componentList;
    }
}