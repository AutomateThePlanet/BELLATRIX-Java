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
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.core.utilities.Ref;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.findstrategies.*;
import solutions.bellatrix.playwright.services.JavaScriptService;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@UtilityClass
public class ShadowDomService {
    private static final String CHILD_COMBINATOR = " > ";
    private static final String SHADOW_ROOT_TAG = "shadow-root";

    public static String getShadowHtml(WebComponent shadowComponent, boolean isShadowRoot) {
        return (String)shadowComponent.evaluate(String.format("(el, [isShadowRoot]) => (%s)(el, isShadowRoot)", getInnerHtmlScript), new Object[] { isShadowRoot });
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createFromShadowRoot(Class<TComponent> componentClass, ShadowRoot parentComponent, TFindStrategy findStrategy) {
        return createAllFromShadowRoot(componentClass, parentComponent, findStrategy).get(0);
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAllFromShadowRoot(Class<TComponent> componentClass, ShadowRoot parentComponent, TFindStrategy findStrategy) {
        var locator = findStrategy.getValue();

        var foundLocators = getAbsoluteCss(parentComponent, locator);

        List<TComponent> componentList = new ArrayList<>(foundLocators.length);
        for (var i = 0; i < foundLocators.length; i++) {
            Ref<String> currentCss = new Ref<>(foundLocators[i]);
            var component = buildMissingShadowRootsAndCreate(componentClass, parentComponent, currentCss);

            if (findStrategy instanceof XpathFindStrategy) {
                component.setFindStrategy(new ShadowXpathFindStrategy(findStrategy.getValue(), currentCss.value));
            } else {
                component.setFindStrategy(new CssFindStrategy(currentCss.value));
            }

            component.setWrappedElement(component.getFindStrategy().convert(component.getParentComponent().getWrappedElement()).nth(i));

            componentList.add(component);
        }

        return componentList;
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> TComponent createInShadowContext(Class<TComponent> componentClass, WebComponent parentComponent, TFindStrategy findStrategy) {
        return createAllInShadowContext(componentClass, parentComponent, findStrategy).get(0);
    }

    public static <TComponent extends WebComponent, TFindStrategy extends FindStrategy> List<TComponent> createAllInShadowContext(Class<TComponent> componentClass, WebComponent parentComponent, TFindStrategy findStrategy) {
        var locator = findStrategy.getValue();
        var parentLocator = retraceParentShadowRoots(parentComponent);

        var outermostShadowRoot = getOutermostShadowRoot(parentComponent);
        var foundLocators = getRelativeCss(outermostShadowRoot, locator, parentLocator);

        List<TComponent> componentList = new ArrayList<>(foundLocators.length);
        for (var i = 0; i < foundLocators.length; i++) {
            Ref<String> currentCss = new Ref<>(foundLocators[i]);
            var component = buildMissingShadowRootsAndCreate(componentClass, outermostShadowRoot, currentCss);

            if (findStrategy instanceof ShadowXpathFindStrategy) {
                component.setFindStrategy(new ShadowXpathFindStrategy(findStrategy.getValue(), currentCss.value));
            } else {
                component.setFindStrategy(new CssFindStrategy(currentCss.value));
            }

            component.setWrappedElement(component.getFindStrategy().convert(component.getParentComponent().getWrappedElement()).nth(i));

            componentList.add(component);
        }

        return componentList;
    }

    private static String[] getAbsoluteCss(ShadowRoot shadowRoot, String locator) {
        return ((ArrayList<String>)shadowRoot
                .evaluate(String.format("(el, [locator]) => (%s)(el, locator)", javaScript), new Object[] { locator }))
                .toArray(String[]::new);
    }

    private static String[] getRelativeCss(ShadowRoot shadowRoot, String locator, String parentLocator) {
        return ((ArrayList<String>)shadowRoot
                .evaluate(String.format("(el, [locator, parentLocator]) => (%s)(el, locator, parentLocator)", javaScript), new Object[] { locator, parentLocator }))
                .toArray(String[]::new);
    }

    private static <TComponent extends WebComponent> TComponent buildMissingShadowRootsAndCreate(Class<TComponent> clazz, ShadowRoot parentComponent, Ref<String> fullCss) {
        var component = InstanceFactory.create(clazz);

        String[] fullCssArray = fullCss.value.split(CHILD_COMBINATOR + SHADOW_ROOT_TAG + CHILD_COMBINATOR);

        ShadowRoot parent = parentComponent;

        // we don't need the last String in the array when building the missing shadow root parents,
        // as it is the local css relative to the innermost shadow root parent
        for (var i = 0; i < fullCssArray.length - 1; i++) {
            var nestedShadowRoot = InstanceFactory.create(ShadowRoot.class);
            nestedShadowRoot.setFindStrategy(new CssFindStrategy(fullCssArray[i]));
            nestedShadowRoot.setParentComponent(parent);

            parent = nestedShadowRoot;
        }

        fullCss.value = fullCssArray[fullCssArray.length - 1];

        component.setParentComponent(parent);
        return component;
    }


    private static ShadowRoot getOutermostShadowRoot(WebComponent component) {
        var parent = component.getParentComponent();
        ShadowRoot outermostShadowRoot = null;

        while (parent instanceof ShadowRoot) {
            outermostShadowRoot = (ShadowRoot)parent;
            parent = parent.getParentComponent();
        }

        return outermostShadowRoot;
    }

    private static int getNestedLevel(WebComponent component) {
        var parent = component.getParentComponent();

        var count = 0;
        while (parent instanceof ShadowRoot) {
            count++;

            parent = parent.getParentComponent();
        }

        return count;
    }

    private static String retraceParentShadowRoots(WebComponent component) {
        if (getNestedLevel(component) > 1) {
            var parent = component.getParentComponent();

            Stack<String> findStrategies = new Stack<>();

            checkIfCss(component.getFindStrategy());

            findStrategies.push(component.getFindStrategy().getValue());

            while (parent instanceof ShadowRoot) {
                checkIfCss(parent.getFindStrategy());

                findStrategies.push(CHILD_COMBINATOR + SHADOW_ROOT_TAG + CHILD_COMBINATOR);
                findStrategies.push(parent.getFindStrategy().getValue());

                parent = parent.getParentComponent();
            }

            StringBuilder finalCss = new StringBuilder();
            while(!findStrategies.isEmpty()) {
                finalCss.append(findStrategies.pop());
            }

            return finalCss.toString();
        } else {
            return component.getFindStrategy().getValue();
        }
    }

    private static void checkIfCss(FindStrategy findStrategy) {
        if (findStrategy instanceof XpathFindStrategy) {
            throw new IllegalArgumentException("Inside Shadow DOM, there cannot be anything different than CSS locator.");
        }
    }

    private static final String javaScript = /* lang=js */ """
            function (element, strategy, relativeElementCss) {
                const child_combinator = " > ";
                const node = "/";
                        
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
                    cloneElement.appendChild(clone(element.shadowRoot, "shadow-root"));
                  }
                        
                  if (element.children) {
                    for (const child of element.children) {
                      cloneElement.appendChild(clone(child, undefined));
                    }
                  }
                        
                  return cloneElement;
                }
                        
                function getAbsoluteXpath(element) {
                  function indexElement(el) {
                    let index = 1;
                        
                    let previousSibling = el.previousElementSibling;
                    while (previousSibling) {
                      if (previousSibling.nodeName.toLowerCase() === el.nodeName.toLowerCase()) {
                        index++;
                      }
                      previousSibling = previousSibling.previousElementSibling;
                    }
                        
                    if (el.tagName.toLowerCase() === "shadow-root") {
                      return node + el.tagName.toLowerCase();
                    } else {
                      return node + el.tagName.toLowerCase() + "[" + index + "]";
                    }
                  }
                        
                  let xpath = [];
                        
                  let currentElement = element;
                  while (currentElement) {
                    if (currentElement.tagName.toLowerCase() === 'html' || currentElement.tagName.toLowerCase() === 'body' || currentElement.tagName.startsWith() === '#' || currentElement.tagName.toLowerCase() === "temporary-div") {
                      break;
                    }
                        
                    xpath.unshift(indexElement(currentElement));
                        
                    currentElement = currentElement.parentElement;
                  }
                  return xpath.join("");
                }
                        
                function getAbsoluteCss(xpath) {
                    let regex = new RegExp(node, 'g');
                    let cssSelector = xpath.replace(regex, child_combinator);
                    cssSelector = cssSelector.replace(/\\[(\\d+)\\]/g, ':nth-of-type($1)');
                    if (cssSelector.startsWith(child_combinator)) {
                        cssSelector = cssSelector.substring(child_combinator.length);
                    }
                    return cssSelector;
                }
                        
                const temporaryDiv = document.createElement("temporary-div");
                if (element.shadowRoot) {
                      temporaryDiv.appendChild(clone(element.shadowRoot, undefined));
                } else {
                  temporaryDiv.appendChild(clone(element, "shadow-root"));
                }
                
                let startPoint = temporaryDiv;
    
                if (relativeElementCss) {
                    startPoint = temporaryDiv.querySelector(relativeElementCss);
                }
    
                let elements;
                if (strategy.startsWith("/") || strategy.startsWith("./") || strategy.startsWith("(")) {
                  let result = document.evaluate(strategy, startPoint, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
                  elements = [];
                  let node;
                  while ((node = result.iterateNext())) {
                    elements.push(node);
                  }
                } else {
                  elements = Array.from(startPoint.querySelectorAll(strategy));
                }
    
                let finalLocators = [];
                elements.forEach((el) => {
                  finalLocators.push(getAbsoluteCss(getAbsoluteXpath(el)));
                });
    
                return finalLocators;
            }""";

    private static final String getInnerHtmlScript = """
            function (element, isShadowRoot) {
                const child_combinator = " > ";
                const node = "/";
            
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
                    cloneElement.appendChild(clone(element.shadowRoot, "shadow-root"));
                  }
            
                  if (element.children) {
                    for (const child of element.children) {
                      cloneElement.appendChild(clone(child, undefined));
                    }
                  }
            
                  return cloneElement;
                }
            
                let temporaryDiv = document.createElement("temporary-div");
                if (element.shadowRoot) {
                      temporaryDiv.appendChild(clone(element.shadowRoot, undefined));
                } else if (isShadowRoot) {
                  temporaryDiv.appendChild(clone(element, "shadow-root"));
                } else {
                    temporaryDiv.appendChild(clone(element, "redundant-el"));
                    temporaryDiv = temporaryDiv.querySelector("redundant-el");
                }
            
                return temporaryDiv.innerHTML;
            }
            """;
}
