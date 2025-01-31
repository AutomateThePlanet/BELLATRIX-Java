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

package solutions.bellatrix.playwright.services;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.playwright.components.Frame;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.findstrategies.*;
import solutions.bellatrix.playwright.findstrategies.options.*;
import solutions.bellatrix.playwright.components.common.webelement.FrameElement;
import solutions.bellatrix.playwright.components.common.webelement.WebElement;
import solutions.bellatrix.plugins.opencv.Base64Encodable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("DataFlowIssue")
public class ComponentCreateService extends WebService {
    public <TComponent extends WebComponent> TComponent byName(Class<TComponent> componentClass, String name) {
        return by(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> List<TComponent> allByName(Class<TComponent> componentClass, String name) {
        return allBy(componentClass, new NameFindStrategy(name));
    }

    public <TComponent extends WebComponent> TComponent byNameEndingWith(Class<TComponent> componentClass, String value) {
        return by(componentClass, new NameEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByNameEndingWith(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new NameEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byTag(Class<TComponent> componentClass, String tag) {
        return by(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTag(Class<TComponent> componentClass, String tag) {
        return allBy(componentClass, new TagFindStrategy(tag));
    }

    public <TComponent extends WebComponent> TComponent byValueContaining(Class<TComponent> componentClass, String value) {
        return by(componentClass, new ValueContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByValueContaining(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new ValueContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byLinkText(Class<TComponent> componentClass, String text) {
        return by(componentClass, new LinkTextFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> allByLinkText(Class<TComponent> componentClass, String text) {
        return allBy(componentClass, new LinkTextFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent byLinkTextContaining(Class<TComponent> componentClass, String text) {
        return by(componentClass, new LinkTextContainingFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> allByLinkTextContaining(Class<TComponent> componentClass, String text) {
        return allBy(componentClass, new LinkTextContainingFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent byIdEndingWith(Class<TComponent> componentClass, String value) {
        return by(componentClass, new IdEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByIdEndingWith(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new IdEndingWithFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byPlaceholder(Class<TComponent> componentClass, String text) {
        return byPlaceholder(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByPlaceholder(Class<TComponent> componentClass, String text) {
        return allByPlaceholder(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent byPlaceholder(Class<TComponent> componentClass, Pattern pattern) {
        return byPlaceholder(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByPlaceholder(Class<TComponent> componentClass, Pattern pattern) {
        return allByPlaceholder(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> TComponent byRole(Class<TComponent> componentClass, AriaRole role) {
        return byRole(componentClass, role, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByRole(Class<TComponent> componentClass, AriaRole role) {
        return allByRole(componentClass, role, null);
    }

    public <TComponent extends WebComponent> TComponent byAltText(Class<TComponent> componentClass, String text) {
        return byAltText(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByAltText(Class<TComponent> componentClass, String text) {
        return allByAltText(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent byAltText(Class<TComponent> componentClass, Pattern pattern) {
        return byAltText(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByAltText(Class<TComponent> componentClass, Pattern pattern) {
        return allByAltText(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> TComponent byInnerTextContaining(Class<TComponent> componentClass, String text) {
        return by(componentClass, new InnerTextContainingFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> allByInnerTextContaining(Class<TComponent> componentClass, String text) {
        return allBy(componentClass, new InnerTextContainingFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent byTitle(Class<TComponent> componentClass, String text) {
        return byTitle(componentClass, text, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByTitle(Class<TComponent> componentClass, String text) {
        return allByTitle(componentClass, text, null);
    }

    public <TComponent extends WebComponent> TComponent byTitle(Class<TComponent> componentClass, Pattern pattern) {
        return byTitle(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> List<TComponent> allByTitle(Class<TComponent> componentClass, Pattern pattern) {
        return allByTitle(componentClass, pattern, null);
    }

    public <TComponent extends WebComponent> TComponent byIdContaining(Class<TComponent> componentClass, String value) {
        return by(componentClass, new IdContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByIdContaining(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new IdContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byId(Class<TComponent> componentClass, String value) {
        return by(componentClass, new IdFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allById(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new IdFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byImage(Class<TComponent> componentClass, Base64Encodable encodedImage) {
        return by(componentClass, new ImageBase64FindStrategy(encodedImage));
    }

    public <TComponent extends WebComponent> List<TComponent> allByImage(Class<TComponent> componentClass, Base64Encodable encodedImage) {
        return allBy(componentClass, new ImageBase64FindStrategy(encodedImage));
    }

    public <TComponent extends WebComponent> TComponent byClass(Class<TComponent> componentClass, String value) {
        return by(componentClass, new ClassFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByClass(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new ClassFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byClassContaining(Class<TComponent> componentClass, String value) {
        return by(componentClass, new ClassContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByClassContaining(Class<TComponent> componentClass, String value) {
        return allBy(componentClass, new ClassContainingFindStrategy(value));
    }

    public <TComponent extends WebComponent> TComponent byAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return by(componentClass, new AttributeContainingFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> List<TComponent> allByAttributeContaining(Class<TComponent> componentClass, String attributeName, String value) {
        return allBy(componentClass, new AttributeContainingFindStrategy(attributeName, value));
    }

    public <TComponent extends WebComponent> TComponent byPlaceholder(Class<TComponent> componentClass, String text, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return by(componentClass, new PlaceholderFindStrategy(text, placeholderOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByPlaceholder(Class<TComponent> componentClass, String text, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return allBy(componentClass, new PlaceholderFindStrategy(text, placeholderOptions));
    }

    public <TComponent extends WebComponent> TComponent byPlaceholder(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return by(componentClass, new PlaceholderFindStrategy(pattern, placeholderOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByPlaceholder(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByPlaceholderOptions, Page.GetByPlaceholderOptions> options) {
        var placeholderOptions = PlaceholderOptions.createAbsolute(options);
        return allBy(componentClass, new PlaceholderFindStrategy(pattern, placeholderOptions));
    }

    public <TComponent extends WebComponent> TComponent byTestId(Class<TComponent> componentClass, String text) {
        return by(componentClass, new TestIdFindStrategy(text));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTestId(Class<TComponent> componentClass, String text) {
        return allBy(componentClass, new TestIdFindStrategy(text));
    }

    public <TComponent extends WebComponent> TComponent byTestId(Class<TComponent> componentClass, Pattern pattern) {
        return by(componentClass, new TestIdFindStrategy(pattern));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTestId(Class<TComponent> componentClass, Pattern pattern) {
        return allBy(componentClass, new TestIdFindStrategy(pattern));
    }

    public <TComponent extends WebComponent> TComponent byRole(Class<TComponent> componentClass, AriaRole role, Function<Page.GetByRoleOptions, Page.GetByRoleOptions> options) {
        var roleOptions = RoleOptions.createAbsolute(options);
        return by(componentClass, new AriaRoleFindStrategy(role, roleOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByRole(Class<TComponent> componentClass, AriaRole role, Function<Page.GetByRoleOptions, Page.GetByRoleOptions> options) {
        var roleOptions = RoleOptions.createAbsolute(options);
        return allBy(componentClass, new AriaRoleFindStrategy(role, roleOptions));
    }

    public <TComponent extends WebComponent> TComponent byAltText(Class<TComponent> componentClass, String text, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return by(componentClass, new AltTextFindStrategy(text, altTextOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByAltText(Class<TComponent> componentClass, String text, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return allBy(componentClass, new AltTextFindStrategy(text, altTextOptions));
    }

    public <TComponent extends WebComponent> TComponent byAltText(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return by(componentClass, new AltTextFindStrategy(pattern, altTextOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByAltText(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByAltTextOptions, Page.GetByAltTextOptions> options) {
        var altTextOptions = AltTextOptions.createAbsolute(options);
        return allBy(componentClass, new AltTextFindStrategy(pattern, altTextOptions));
    }

    public <TComponent extends WebComponent> TComponent byTitle(Class<TComponent> componentClass, String text, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return by(componentClass, new TitleFindStrategy(text, titleOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTitle(Class<TComponent> componentClass, String text, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return allBy(componentClass, new TitleFindStrategy(text, titleOptions));
    }

    public <TComponent extends WebComponent> TComponent byTitle(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return by(componentClass, new TitleFindStrategy(pattern, titleOptions));
    }

    public <TComponent extends WebComponent> List<TComponent> allByTitle(Class<TComponent> componentClass, Pattern pattern, Function<Page.GetByTitleOptions, Page.GetByTitleOptions> options) {
        var titleOptions = TitleOptions.createAbsolute(options);
        return allBy(componentClass, new TitleFindStrategy(pattern, titleOptions));
    }

    public <TComponent extends WebComponent> TComponent byXpath(Class<TComponent> componentClass, String xpath) {
        return by(componentClass, new XpathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> List<TComponent> allByXpath(Class<TComponent> componentClass, String xpath) {
        return allBy(componentClass, new XpathFindStrategy(xpath));
    }

    public <TComponent extends WebComponent> TComponent byCss(Class<TComponent> componentClass, String css) {
        return by(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent> List<TComponent> allByCss(Class<TComponent> componentClass, String css) {
        return allBy(componentClass, new CssFindStrategy(css));
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent by(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        wrappedBrowser().getCurrentPage().waitForLoadState();

        var element = findStrategy.convert(wrappedBrowser().getCurrentPage()).first();

        return createInstance(componentClass, findStrategy, element);
    }

    public <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> allBy(Class<TComponent> componentClass, TFindStrategy findStrategy) {
        wrappedBrowser().getCurrentPage().waitForLoadState();

        var elements = findStrategy.convert(wrappedBrowser().getCurrentPage()).all();
        List<TComponent> componentList = new ArrayList<>();
        for (var element : elements) {
            var component = createInstance(componentClass, findStrategy, element);

            componentList.add(component);
        }

        return componentList;
    }

    protected <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createInstance(Class<TComponent> componentClass, TFindStrategy findStrategy, WebElement element) {
        var component = InstanceFactory.create(componentClass);

        if (componentClass == Frame.class && element.getClass() != FrameElement.class) {
            component.setWrappedElement(new FrameElement(element));
        } else if (componentClass != Frame.class && element.getClass() == FrameElement.class) {
            component.setWrappedElement(new WebElement(element.getWrappedLocator()));
        } else {
            component.setWrappedElement(element);
        }

        component.setFindStrategy(findStrategy);

        return component;
    }
}
