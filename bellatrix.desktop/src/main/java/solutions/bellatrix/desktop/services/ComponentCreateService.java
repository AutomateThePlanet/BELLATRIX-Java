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

package solutions.bellatrix.desktop.services;

import solutions.bellatrix.web.core.utilities.InstanceFactory;
import solutions.bellatrix.desktop.components.DesktopComponent;
import solutions.bellatrix.desktop.findstrategies.*;
import solutions.bellatrix.desktop.infrastructure.DriverService;

import java.util.ArrayList;
import java.util.List;

public class ComponentCreateService extends DesktopService {
    public <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return by(componentClass, findStrategy);
    }

    public <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return allBy(componentClass, findStrategy);
    }

    public <TComponent extends DesktopComponent> TComponent byAutomationId(Class<TComponent> componentClass, String id) {
        return by(componentClass, new AutomationIdFindStrategy(id));
    }

    public <TComponent extends DesktopComponent> TComponent byName(Class<TComponent> componentClass, String name) {
        return by(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends DesktopComponent> TComponent byClass(Class<TComponent> componentClass, String cclass) {
        return by(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends DesktopComponent> TComponent byXPath(Class<TComponent> componentClass, String xpath) {
        return by(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends DesktopComponent> TComponent byAccessibilityId(Class<TComponent> componentClass, String accessibilityId) {
        return by(componentClass, new AccessibilityIdFindStrategy(accessibilityId));
    }

    public <TComponent extends DesktopComponent> TComponent byTag(Class<TComponent> componentClass, String tag) {
        return by(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends DesktopComponent> TComponent byIdContaining(Class<TComponent> componentClass, String idContaining) {
        return by(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByAutomationId(Class<TComponent> componentClass, String automationId) {
        return allBy(componentClass, new AutomationIdFindStrategy(automationId));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByName(Class<TComponent> componentClass, String name) {
        return allBy(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByClass(Class<TComponent> componentClass, String cclass) {
        return allBy(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByXPath(Class<TComponent> componentClass, String xpath) {
        return allBy(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByAccessibilityId(Class<TComponent> componentClass, String accessibilityId) {
        return allBy(componentClass, new AccessibilityIdFindStrategy(accessibilityId));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByTag(Class<TComponent> componentClass, String tag) {
        return allBy(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends DesktopComponent> List<TComponent> allByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return allBy(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        return component;
    }

    public <TComponent extends DesktopComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var nativeElements = findStrategy.findAllElements(DriverService.getWrappedDriver());
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
