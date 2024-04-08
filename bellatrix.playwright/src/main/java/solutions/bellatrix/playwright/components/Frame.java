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

package solutions.bellatrix.playwright.components;

import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.playwright.findstrategies.FindStrategy;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;

public class Frame extends WebComponent {
    public Frame() {
        super();
    }

    public String getName() {
        return defaultGetName();
    }

    @Override
    /**
     * Convert this component to another type of component.
     * @param componentClass type of component
     */
    public <ComponentT extends WebComponent> ComponentT as(Class<ComponentT> componentClass) {
        if (componentClass == Frame.class) return (ComponentT)this;

        var component = InstanceFactory.create(componentClass);
        var element = new WebElement(this.wrappedElement.getWrappedLocator());
        component.setWrappedElement(element);

        var findStrategy = (FindStrategy)this.findStrategy.clone();
        findStrategy.setWebElement(element);
        component.setFindStrategy(findStrategy);

        component.setParentWrappedComponent(getParentWrappedComponent());
        component.setElementIndex(getElementIndex());

        return component;
    }
}
