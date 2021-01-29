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

package solutions.bellatrix.services;

import solutions.bellatrix.components.WebComponent;
import solutions.bellatrix.findstrategies.*;
import solutions.bellatrix.infrastructure.DriverService;
import solutions.bellatrix.utilities.InstanceFactory;

import java.util.ArrayList;
import java.util.List;

public class ComponentCreateService extends WebService {
    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return create(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return createAll(componentClass, findStrategy);
    }

    public <TComponent extends WebComponent> TComponent byId(Class<TComponent> componentClass, String id) {
        return create(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> TComponent byCss(Class<TComponent> componentClass, String css) {
        return create(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> TComponent byClass(Class<TComponent> componentClass, String cclass) {
        return create(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends WebComponent> TComponent byXPath(Class<TComponent> componentClass, String xpath) {
        return create(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent byLinkText(Class<TComponent> componentClass, String linkText) {
        return create(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> TComponent byTag(Class<TComponent> componentClass, String tag) {
        return create(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent byIdContaining(Class<TComponent> componentClass, String idContaining) {
        return create(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> TComponent byInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return create(componentClass, new InnerTextContainsFindStrategy(innerText));
    }

    public <TComponent extends WebComponent> List<TComponent> allById(Class<TComponent> componentClass, String id) {
        return createAll(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends WebComponent> List<TComponent> allByCss(Class<TComponent> componentClass, String css) {
        return createAll(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> allByClass(Class<TComponent> componentClass, String cclass) {
        return createAll(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends WebComponent> List<TComponent> allByXPath(Class<TComponent> componentClass, String xpath) {
        return createAll(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> allByLinkText(Class<TComponent> componentClass, String linkText) {
        return createAll(componentClass, new LinkTextFindStrategy(linkText));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTag(Class<TComponent> componentClass, String tag) {
        return createAll(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> allByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return createAll(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends WebComponent> List<TComponent> allByInnerTextContaining(Class<TComponent> componentClass, String innerText) {
        return createAll(componentClass, new InnerTextContainsFindStrategy(innerText));
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        return component;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAll(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var nativeElements = DriverService.getWrappedDriver().findElements(findStrategy.convert());
        List<TComponent> componentList = new ArrayList<>();
        for (int i = 0; i < nativeElements.stream().count(); i++) {
            var component = InstanceFactory.create(componentClass);
            component.setFindStrategy(findStrategy);
            component.setElementIndex(i);
            componentList.add(component);
        }

        return componentList;
    }
}
