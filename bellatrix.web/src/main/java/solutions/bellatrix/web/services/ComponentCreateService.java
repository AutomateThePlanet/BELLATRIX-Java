/*
 * Copyright 2022 Automate The Planet Ltd.
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

package solutions.bellatrix.web.services;

import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.findstrategies.*;
import solutions.bellatrix.web.infrastructure.DriverService;

import java.util.ArrayList;
import java.util.List;

public class ComponentCreateService extends WebService {
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return by(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return allBy(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent> TComponent byId(Class<TComponent> componentClass, String id) {
        return by(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> TComponent byAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return by(componentClass, new AttributeContainingWithFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> TComponent byIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return by(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    public <TComponent extends WebComponent> TComponent byCss(Class<TComponent> componentClass, String css) {
        return by(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> TComponent byClass(Class<TComponent> componentClass, String className) {
        return by(componentClass, new ClassFindStrategy(className));
    }

    public <TComponent extends WebComponent> TComponent byName(Class<TComponent> componentClass, String name) {
        return by(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> TComponent byNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return by(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    public <TComponent extends WebComponent> TComponent byValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return by(componentClass, new ValueContainingWithFindStrategy(valueContaining));
    }

    public <TComponent extends WebComponent> TComponent byClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return by(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    public <TComponent extends WebComponent> TComponent byXPath(Class<TComponent> componentClass, String xpath) {
        return by(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent byLinkText(Class<TComponent> componentClass, String linkText) {
        return by(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> TComponent byLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return by(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    public <TComponent extends WebComponent> TComponent byTag(Class<TComponent> componentClass, String tag) {
        return by(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent byIdContaining(Class<TComponent> componentClass, String idContaining) {
        return by(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> TComponent byInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return by(componentClass, new InnerTextContainingFindStrategy(innerText));
    }

    public <TComponent extends WebComponent> List<TComponent> allById(Class<TComponent> componentClass, String id) {
        return allBy(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> List<TComponent> allByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return allBy(componentClass, new AttributeContainingWithFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByIdEndingWith(Class<TComponent> componentClass, String idEnding) {
        return allBy(componentClass, new IdEndingWithFindStrategy(idEnding));
    }

    public <TComponent extends WebComponent> List<TComponent> allByCss(Class<TComponent> componentClass, String css) {
        return allBy(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> allByClass(Class<TComponent> componentClass, String className) {
        return allBy(componentClass, new ClassFindStrategy(className));
    }

    public <TComponent extends WebComponent> List<TComponent> allByName(Class<TComponent> componentClass, String name) {
        return allBy(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> List<TComponent> allByNameEnding(Class<TComponent> componentClass, String nameEnding) {
        return allBy(componentClass, new NameEndingWithFindStrategy(nameEnding));
    }

    public <TComponent extends WebComponent> List<TComponent> allByValueContaining(Class<TComponent> componentClass, String valueContaining) {
        return allBy(componentClass, new ValueContainingWithFindStrategy(valueContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> allByClassContaining(Class<TComponent> componentClass, String classNameContaining) {
        return allBy(componentClass, new ClassContainingFindStrategy(classNameContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> allByXPath(Class<TComponent> componentClass, String xpath) {
        return allBy(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> allByLinkText(Class<TComponent> componentClass, String linkText) {
        return allBy(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> List<TComponent> allByLinkTextContaining(Class<TComponent> componentClass, String linkTextContaining) {
        return allBy(componentClass, new LinkTextContainingFindStrategy(linkTextContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTag(Class<TComponent> componentClass, String tag) {
        return allBy(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> allByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return allBy(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> allByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return allBy(componentClass, new InnerTextContainingFindStrategy(innerText));
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        return component;
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var nativeElements = getWrappedDriver().findElements(findStrategy.convert());
        List<TComponent> componentList = new ArrayList<>();
        for (int i = 0; i < nativeElements.size(); i++) {
            var component = InstanceFactory.create(componentClass);
            component.setFindStrategy(findStrategy);
            component.setElementIndex(i);
            componentList.add(component);
        }

        return componentList;
    }
}
