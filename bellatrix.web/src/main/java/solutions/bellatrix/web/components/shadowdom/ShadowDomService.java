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

package solutions.bellatrix.web.components.shadowdom;

import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import solutions.bellatrix.core.utilities.HtmlService;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.Ref;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.findstrategies.CssFindStrategy;
import solutions.bellatrix.web.findstrategies.FindStrategy;
import solutions.bellatrix.web.findstrategies.ShadowXPathFindStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@UtilityClass
public class ShadowDomService {
    private static final String CHILD_COMBINATOR = " > ";
    private static final String SHADOW_ROOT_TAG = "shadow-root";

    public static String getShadowHtml(ShadowRoot shadowRoot) {
        var function = String.format("""
                return (function(element) {
                           function clone(element, tag) {
                               let cloneElement;
                             	if (element instanceof ShadowRoot && !tag) {
                                 cloneElement = new DocumentFragment();
                               } else if (tag) {
                                 cloneElement = document.createElement(tag);
                               }
                               else {
                                 cloneElement = element.cloneNode();
                                 if (element.firstChild && element.firstChild.nodeType === 3) {
                                   cloneElement.appendChild(element.firstChild.cloneNode());
                                 }
                               }

                               if (element.shadowRoot) {
                                   cloneElement.appendChild(clone(element.shadowRoot, "%s"));
                               }

                               if (element.children) {
                                   for (const child of element.children) {
                                       cloneElement.appendChild(clone(child));
                                   }
                               }

                               return cloneElement;
                           }
                       
                        		var temporaryDiv = document.createElement("div");
                         	temporaryDiv.appendChild(clone(element.shadowRoot, undefined));
                           return temporaryDiv.innerHTML;
                       })(arguments[0]);
                """, SHADOW_ROOT_TAG);

        return shadowRoot.getJavaScriptService().execute(function, shadowRoot);
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

        // passed as a reference, the inside value will change:
        Ref<ShadowRoot> shadowRoot = new Ref<>(initialShadowRoot);

        // create missing shadow roots between the element and the initial shadow root;
        // chain them;
        // return the element's css relative to the innermost shadow root
        var jsoupNodeCss = buildMissingShadowRootsAndReturnElementRelativeCss(shadowRoot, jsoupNode);

        if (findStrategy.convert() instanceof By.ByXPath) {
            component.setFindStrategy(new ShadowXPathFindStrategy(findStrategy.getValue(), jsoupNodeCss));
        } else {
            component.setFindStrategy(new CssFindStrategy(jsoupNodeCss));
        }

        component.setParentComponent(shadowRoot.value);
        component.setParentWrappedElement(shadowRoot.value.getWrappedElement());

        return component;
    }

    private static String buildMissingShadowRootsAndReturnElementRelativeCss(Ref<ShadowRoot> shadowRoot, Element jsoupNode) {
        var nestedShadowRootStack = new Stack<Element>();

        // initial absolute xpath, relative to the outermost shadow root element
        var jsoupNodeCss = HtmlService.convertAbsoluteXpathToCss(HtmlService.getAbsoluteXpath(jsoupNode)).split(CHILD_COMBINATOR);

        // if there are <shadow-root> elements found between the outermost shadow root and the element
        // populate the Stack<Element>
        if (tryFindNestedShadowRoots(jsoupNode, nestedShadowRootStack)) {
            String[] previousCss = null;

            while (!nestedShadowRootStack.isEmpty()) {
                var parent = nestedShadowRootStack.pop();
                var css = HtmlService.convertAbsoluteXpathToCss(HtmlService.getAbsoluteXpath(parent)).split(CHILD_COMBINATOR);
                if (previousCss != null) {
                    css = removeRedundantSteps(css, previousCss);
                }


                shadowRoot.value = createNestedShadowRoot(shadowRoot.value, cleanFromShadowRootTags(css));

                jsoupNodeCss = removeRedundantSteps(jsoupNodeCss, css);

                previousCss = HtmlService.convertAbsoluteXpathToCss(HtmlService.getAbsoluteXpath(parent)).split(CHILD_COMBINATOR);
            }
        }

        return String.join(CHILD_COMBINATOR, cleanFromShadowRootTags(jsoupNodeCss));
    }

    private static String[] removeRedundantSteps(String[] elementCss, String[] currentCss) {
        return Arrays.stream(elementCss)
                .skip(currentCss.length)
                .toArray(String[]::new);
    }

    private static String[] cleanFromShadowRootTags(String[] css) {
        return Arrays.stream(css)
                .filter(x -> !x.contains(SHADOW_ROOT_TAG))
                .toArray(String[]::new);
    }

    private static ShadowRoot createNestedShadowRoot(ShadowRoot parent, String[] locator) {
        var finalLocator = String.join(CHILD_COMBINATOR, cleanFromShadowRootTags(locator));

        ShadowRoot nestedShadowRoot = InstanceFactory.create(ShadowRoot.class);
        nestedShadowRoot.setFindStrategy(new CssFindStrategy(finalLocator));
        nestedShadowRoot.setParentComponent(parent);
        nestedShadowRoot.setParentWrappedElement(parent.shadowRoot());

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

    /**
     * Find from root element in shadow root's html.
     */
    private static Element getElement(ShadowRoot component, FindStrategy findStrategy) {
        var doc = Jsoup.parse(component.getHtml());

        return getElement(doc, findStrategy);
    }

    /**
     * Find relatively from element.
     */
    private static Element getElement(Element element, FindStrategy findStrategy) {
        return getElements(element, findStrategy).get(0);
    }

    /**
     * Find from root element in shadow root's html.
     */
    private static List<Element> getElements(ShadowRoot component, FindStrategy findStrategy) {
        var doc = Jsoup.parse(component.getHtml());

        return getElements(doc, findStrategy);
    }

    /**
     * Find relatively from element.
     */
    private static List<Element> getElements(Element element, FindStrategy findStrategy) {
        Elements foundElements = null;
        var strategyType = findStrategy.convert();
        var strategyValue = findStrategy.getValue();

        if (strategyType instanceof By.ByCssSelector) {
            foundElements = element.select(strategyValue);
        }
        if (findStrategy.convert() instanceof By.ByXPath) {
            foundElements = element.selectXpath(strategyValue);
        }
        if (findStrategy.convert() instanceof By.ById) {
            foundElements = element.select(String.format("[id='%s']", strategyValue));
        }
        if (strategyType instanceof By.ByName) {
            foundElements = element.select(String.format("[name='%s']", strategyValue));
        }
        if (strategyType instanceof By.ByClassName) {
            foundElements = element.select(String.format("[class='%s']", strategyValue));
        }
        if (strategyType instanceof By.ByLinkText) {
            foundElements = element.selectXpath(String.format("//a[text()='%s']", strategyValue));
        }
        if (strategyType instanceof By.ByTagName) {
            foundElements = element.select(strategyValue);
        }
        if (strategyType instanceof By.ByPartialLinkText) {
            foundElements = element.selectXpath(String.format("//a[contains(text(), '%s')]", strategyValue));
        }

        return foundElements;
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
