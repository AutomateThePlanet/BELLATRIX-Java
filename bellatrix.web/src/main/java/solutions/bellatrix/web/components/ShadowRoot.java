/*
 * Copyright 2022 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required createBy applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.components;

import org.openqa.selenium.By;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.findstrategies.FindStrategy;
import solutions.bellatrix.web.findstrategies.ShadowXPathFindStrategy;

import java.util.ArrayList;
import java.util.List;

public class ShadowRoot extends WebComponent implements ComponentHtml {
    /**
     * Returns the innerHTML of the shadowRoot of the shadow host.
     */
    public String getHtml() {
        return javaScriptService.execute("return arguments[0].shadowRoot.innerHTML", getWrappedElement());
    }

    @Override
    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        findElement();
        var component = InstanceFactory.create(componentClass);

        if (findStrategy.convert() instanceof By.ByXPath) {
            component.setFindStrategy(getShadowXpath(findStrategy));
        } else {
            component.setFindStrategy(findStrategy);
        }

        component.setParentComponent(this);
        component.setParentWrappedElement(this.shadowRoot());

        CREATED_ELEMENT.broadcast(new ComponentActionEventArgs(this));
        return component;
    }

    @Override
    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        CREATING_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        findElement();

        List<TComponent> componentList = new ArrayList<>();

        if (findStrategy.convert() instanceof By.ByXPath) {
            var strategies = getShadowXpaths(findStrategy);

            for (var strategy : strategies) {
                var component = InstanceFactory.create(componentClass);
                component.setFindStrategy(strategy);
                component.setParentComponent(this);
                component.setParentWrappedElement(this.shadowRoot());
                componentList.add(component);
            }
        } else {
            var nativeElements = this.shadowRoot().findElements(findStrategy.convert());

            for (int i = 0; i < nativeElements.size(); i++) {
                var component = InstanceFactory.create(componentClass);
                component.setFindStrategy(findStrategy);
                component.setElementIndex(i);
                component.setParentWrappedElement(this.shadowRoot());
                componentList.add(component);
            }
        }

        CREATED_ELEMENTS.broadcast(new ComponentActionEventArgs(this));
        return componentList;
    }

    private ShadowXPathFindStrategy getShadowXpath(FindStrategy findStrategy) {
        var cssLocator = HtmlService.convertXpathToAbsoluteCssLocator(this.getHtml(), findStrategy.getValue());

        return new ShadowXPathFindStrategy(findStrategy.getValue(), cssLocator);
    }

    private List<ShadowXPathFindStrategy> getShadowXpaths(FindStrategy findStrategy) {
        var cssLocators = HtmlService.convertXpathToAbsoluteCssLocators(this.getHtml(), findStrategy.getValue());

        List<ShadowXPathFindStrategy> strategies = new ArrayList<>();

        for (var locator : cssLocators) {
            strategies.add(new ShadowXPathFindStrategy(findStrategy.getValue(), locator));
        }

        return strategies;
    }
}
