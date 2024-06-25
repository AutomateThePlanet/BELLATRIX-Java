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

package solutions.bellatrix.playwright.components.shadowdom;

import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.findstrategies.CssFindStrategy;
import solutions.bellatrix.playwright.findstrategies.FindStrategy;
import solutions.bellatrix.playwright.findstrategies.ShadowXpathFindStrategy;
import solutions.bellatrix.playwright.findstrategies.XpathFindStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@UtilityClass
public class ShadowDomService {
    private static final String CHILD_COMBINATOR = " > ";
    private static final String SHADOW_ROOT_TAG = "shadow-root";

    public static String getShadowHtml(ShadowRoot shadowRoot) {
        var function = String.format("""
                el => {
                           function clone(element, tag) {
                               let cloneElement;
                             	if (element instanceof ShadowRoot && !tag) {
                                 cloneElement = new DocumentFragment();
                               } else if (tag) {
                                 cloneElement = document.createElement(tag);
                               }
                               else {
                                 cloneElement = element.cloneNode(false);
                                 if (element.firstChild && element.firstChild.nodeType === 3) {
                                   cloneElement.appendChild(element.firstChild.cloneNode(false));
                                 }
                               }

                               if (element.shadowRoot) {
                                   cloneElement.appendChild(clone(element.shadowRoot, "%s"));
                               }

                               if (element.children) {
                                   for (const child of element.children) {
                                       cloneElement.appendChild(clone(child, undefined));
                                   }
                               }

                               return cloneElement;
                           }
                       
                        	var temporaryDiv = document.createElement("div");
                         	temporaryDiv.appendChild(clone(el.shadowRoot, undefined));
                           return temporaryDiv.innerHTML;
                };
                """, SHADOW_ROOT_TAG);

        return (String)shadowRoot.evaluate(function);
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createInShadowContext(WebComponent parentComponent, Class<TComponent> componentClass, TFindStrategy findStrategy) {
        var shadowRoots = getShadowRootAncestors(parentComponent);

        ShadowRoot initialShadowRoot = shadowRoots.pop();

        var locatorBuilder = new StringBuilder();
        while (!shadowRoots.isEmpty()) {
            locatorBuilder.append(shadowRoots.pop().getFindStrategy().getValue()).append(CHILD_COMBINATOR + SHADOW_ROOT_TAG);
        }
        CssFindStrategy fullLocator = new CssFindStrategy(locatorBuilder.toString() + parentComponent.getFindStrategy().getValue());

        var parentElement = getElement(initialShadowRoot, fullLocator);

        var newElement = getElement(parentElement, findStrategy);

        return createComponent(componentClass, initialShadowRoot, newElement, findStrategy);
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAllInShadowContext(WebComponent parentComponent, Class<TComponent> componentClass, TFindStrategy findStrategy) {
        List<TComponent> componentList = new ArrayList<>();

        var shadowRoots = getShadowRootAncestors(parentComponent);

        ShadowRoot initialShadowRoot = shadowRoots.pop();

        var locatorBuilder = new StringBuilder();
        while (!shadowRoots.isEmpty()) {
            locatorBuilder.append(shadowRoots.pop().getFindStrategy().getValue()).append(CHILD_COMBINATOR + SHADOW_ROOT_TAG);
        }
        CssFindStrategy fullLocator = new CssFindStrategy(locatorBuilder.toString() + parentComponent.getFindStrategy().getValue());

        var parentElement = getElement(initialShadowRoot, fullLocator);

        for (var element : getElements(parentElement, findStrategy)) {
            componentList.add(createComponent(componentClass, initialShadowRoot, element, findStrategy));
        }

        return componentList;
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createFromShadowRoot(ShadowRoot shadowRoot, Class<TComponent> componentClass, TFindStrategy findStrategy) {
        return createComponent(componentClass, shadowRoot, getElement(shadowRoot, findStrategy), findStrategy);
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAllFromShadowRoot(ShadowRoot shadowRoot, Class<TComponent> componentClass, TFindStrategy findStrategy) {
        List<TComponent> componentList = new ArrayList<>();

        for (var element : getElements(shadowRoot, findStrategy)) {
            componentList.add(createComponent(componentClass, shadowRoot, element, findStrategy));
        }

        return componentList;
    }

    private static <TComponent extends WebComponent> TComponent createComponent(Class<TComponent> clazz, ShadowRoot initialShadowRoot, Element jsoupNode, FindStrategy findStrategy) {
        var component = InstanceFactory.create(clazz);

        var shadowRoot = initialShadowRoot;
        var nestedShadowRootStack = new Stack<Element>();

        var jsoupNodeCss = HtmlService.convertAbsoluteXpathToCss(HtmlService.getAbsoluteXpath(jsoupNode));

        if (tryFindNestedShadowRoots(jsoupNode, nestedShadowRootStack)) {
            while (!nestedShadowRootStack.isEmpty()) {
                var parent = nestedShadowRootStack.pop();
                var css = HtmlService.convertAbsoluteXpathToCss(HtmlService.getAbsoluteXpath(parent));

                shadowRoot = createNestedShadowRoot(shadowRoot, HtmlService.removeDanglingChildCombinatorsFromCss(cleanFromShadowRootTags(css)));

                jsoupNodeCss = jsoupNodeCss.replace(css, "");
                jsoupNodeCss = HtmlService.removeDanglingChildCombinatorsFromCss(jsoupNodeCss);
            }
        }

        jsoupNodeCss = cleanFromShadowRootTags(jsoupNodeCss);

        if (findStrategy instanceof XpathFindStrategy) {
            component.setFindStrategy(new ShadowXpathFindStrategy(findStrategy.getValue(), jsoupNodeCss));
        } else {
            component.setFindStrategy(new CssFindStrategy(jsoupNodeCss));
        }

        component.setParentComponent(shadowRoot);
        component.setWrappedElement(component.getFindStrategy().convert(shadowRoot.getWrappedElement()).first());

        return component;
    }

    private static String cleanFromShadowRootTags(String css) {
        css = css.replace(SHADOW_ROOT_TAG + CHILD_COMBINATOR, "");
        css = css.replace(CHILD_COMBINATOR + SHADOW_ROOT_TAG + ":nth-of-type(1)", "");
        return css;
    }

    private static ShadowRoot createNestedShadowRoot(ShadowRoot parent, String locator) {
        ShadowRoot nestedShadowRoot = InstanceFactory.create(ShadowRoot.class);
        nestedShadowRoot.setFindStrategy(new CssFindStrategy(locator));
        nestedShadowRoot.setParentComponent(parent);

        return nestedShadowRoot;
    }

    private static boolean tryFindNestedShadowRoots(Element jsoupElement, Stack<Element> parentShadowRootStack) {
        var parent = jsoupElement.parent();

        while (parent != null) {
            if (parent.tagName().equals(SHADOW_ROOT_TAG)) {
                parentShadowRootStack.push(parent);
            }

            parent = parent.parent();
        }

        return !parentShadowRootStack.isEmpty();
    }

    private static Element getElement(ShadowRoot component, FindStrategy findStrategy) {
        var doc = Jsoup.parse(component.getHtml());

        var value = findStrategy.getValue();

        if (findStrategy instanceof XpathFindStrategy) {
            return doc.selectXpath(findStrategy.getValue()).get(0);
        } else {
            return doc.select(findStrategy.getValue()).get(0);
        }
    }

    private static Element getElement(Element element, FindStrategy findStrategy) {
        if (findStrategy instanceof XpathFindStrategy) {
            return element.selectXpath(findStrategy.getValue()).get(0);
        } else {
            return element.select(findStrategy.getValue()).get(0);
        }
    }

    private static List<Element> getElements(ShadowRoot component, FindStrategy findStrategy) {
        var doc = Jsoup.parse(component.getHtml());

        if (findStrategy instanceof XpathFindStrategy) {
            return doc.selectXpath(findStrategy.getValue());
        } else {
            return doc.select(findStrategy.getValue());
        }
    }

    private static List<Element> getElements(Element element, FindStrategy findStrategy) {
        if (findStrategy instanceof XpathFindStrategy) {
            return element.selectXpath(findStrategy.getValue());
        } else {
            return element.select(findStrategy.getValue());
        }
    }

    private static Stack<ShadowRoot> getShadowRootAncestors(WebComponent initialComponent) {
        var component = initialComponent.getParentComponent();

        var shadowRoots = new Stack<ShadowRoot>();

        while (component != null) {
            if (component instanceof ShadowRoot) {
                shadowRoots.push((ShadowRoot)component);
            }
            component = component.getParentComponent();
        }

        return shadowRoots;
    }
}
