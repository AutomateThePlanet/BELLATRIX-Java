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

package solutions.bellatrix.android.services;

import solutions.bellatrix.android.components.AndroidComponent;
import solutions.bellatrix.android.findstrategies.*;
import solutions.bellatrix.android.infrastructure.DriverService;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.plugins.opencv.Base64Encodable;

import java.util.ArrayList;
import java.util.List;

public class ComponentCreateService extends MobileService {
    public <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> TComponent create(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return by(componentClass, findStrategy);
    }

    public <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TFindStrategy> findStrategyClass, Class<TComponent> componentClass, Object... args) {
        var findStrategy = InstanceFactory.create(findStrategyClass, args);
        return allBy(componentClass, findStrategy);
    }

    public <TComponent extends AndroidComponent> TComponent byImage(Class<TComponent> componentClass, Base64Encodable encodedImage) {
        return by(componentClass, new ImageBase64FindStrategy(encodedImage));
    }

    public <TComponent extends AndroidComponent> TComponent byId(Class<TComponent> componentClass, String id) {
        return by(componentClass, new IdFindStrategy(id));
    }

    public <TComponent extends AndroidComponent> TComponent byName(Class<TComponent> componentClass, String name) {
        return by(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends AndroidComponent> TComponent byClass(Class<TComponent> componentClass, String cclass) {
        return by(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends AndroidComponent> TComponent byXPath(Class<TComponent> componentClass, String xpath) {
        return by(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends AndroidComponent> TComponent byAndroidUIAutomator(Class<TComponent> componentClass, String androidUIAutomator) {
        return by(componentClass, new AndroidUIAutomatorFindStrategy(androidUIAutomator));
    }

    public <TComponent extends AndroidComponent> TComponent byText(Class<TComponent> componentClass, String text) {
        return by(componentClass, new TextFindStrategy(text));
    }

    public <TComponent extends AndroidComponent> TComponent byTextContaining(Class<TComponent> componentClass, String text) {
        return by(componentClass, new TextContainingFindStrategy(text));
    }

    public <TComponent extends AndroidComponent> TComponent byDescription(Class<TComponent> componentClass, String description) {
        return by(componentClass, new DescriptionFindStrategy(description));
    }

    public <TComponent extends AndroidComponent> TComponent byDescriptionContaining(Class<TComponent> componentClass, String description) {
        return by(componentClass, new DescriptionContainingFindStrategy(description));
    }

    public <TComponent extends AndroidComponent> TComponent byTag(Class<TComponent> componentClass, String tag) {
        return by(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends AndroidComponent> TComponent byIdContaining(Class<TComponent> componentClass, String idContaining) {
        return by(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByImage(Class<TComponent> componentClass, Base64Encodable encodedImage) {
        return allBy(componentClass, new ImageBase64FindStrategy(encodedImage));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allById(Class<TComponent> componentClass, String automationId) {
        return allBy(componentClass, new IdFindStrategy(automationId));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByName(Class<TComponent> componentClass, String name) {
        return allBy(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByClass(Class<TComponent> componentClass, String cclass) {
        return allBy(componentClass, new ClassFindStrategy(cclass));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByXPath(Class<TComponent> componentClass, String xpath) {
        return allBy(componentClass, new XPathFindStrategy(xpath));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByAndroidUIAutomator(Class<TComponent> componentClass, String androidUIAutomator) {
        return allBy(componentClass, new AndroidUIAutomatorFindStrategy(androidUIAutomator));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByTag(Class<TComponent> componentClass, String tag) {
        return allBy(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByText(Class<TComponent> componentClass, String text) {
        return allBy(componentClass, new TextFindStrategy(text));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByTextContaining(Class<TComponent> componentClass, String text) {
        return allBy(componentClass, new TextContainingFindStrategy(text));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByDescription(Class<TComponent> componentClass, String description) {
        return allBy(componentClass, new DescriptionFindStrategy(description));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByDescriptionContaining(Class<TComponent> componentClass, String description) {
        return allBy(componentClass, new DescriptionContainingFindStrategy(description));
    }

    public <TComponent extends AndroidComponent> List<TComponent> allByIdContaining(Class<TComponent> componentClass, String idContaining) {
        return allBy(componentClass, new IdContainingFindStrategy(idContaining));
    }

    public <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var component = InstanceFactory.create(componentClass);
        component.setFindStrategy(findStrategy);
        return component;
    }

    public <TComponent extends AndroidComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var nativeElements = findStrategy.findAllElements(DriverService.getWrappedAndroidDriver());
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
