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

package solutions.bellatrix.core.utilities;

import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import solutions.bellatrix.core.utilities.parsing.TypeParser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Web Utility Class
 */
@UtilityClass
public class HtmlService {
    private static final String CHILD_COMBINATOR = " > ";
    private static final String NODE = "/";
    private static final String NODE_OR_SELF = "//";
    private static final String ROOT_ELEMENT_TAG = "bellatrix-root";

    public static Document addRootElementIfNeeded(Document doc) {
        boolean hasMultipleTopLevelElements = doc.childNodes().stream()
                .filter(node -> node instanceof Element).count() > 1;

        if (hasMultipleTopLevelElements) {
            var root = new Element(ROOT_ELEMENT_TAG);

            for (Node node : doc.childNodes()) {
                root.appendChild(node.clone());
            }

            doc.empty();
            doc.appendChild(root);
        }

        return doc;
    }

    public static String getAbsoluteXpath(Element element) {
        StringBuilder xpath = new StringBuilder();

        Element currentElement = element;
        while (currentElement != null) {
            if (currentElement.tagName().equals("html") || currentElement.tagName().equals("body") || currentElement.tagName().startsWith("#") || currentElement.tagName().equals(ROOT_ELEMENT_TAG)) {
                // ignore the <html> and <body>, because jsoup added them to the html fragment
                // ignore added bellatrix root element
                // ignore invalid element tags
                break;
            }

            xpath.insert(0, indexElement(currentElement));

            currentElement = currentElement.parent();
        }

        return xpath.toString();
    }

    private String indexElement(Element element) {
        int index = 1;

        Node previousSibling = element.previousSibling();
        while (previousSibling != null) {
            if (previousSibling.nodeName().equals(element.nodeName())) {
                index++;
            }
            previousSibling = previousSibling.previousSibling();
        }

        return NODE + element.tagName() + "[" + index + "]";
    }

    public <T> T getAttribute(Element element, String attributeName, Class<T> clazz) {
        if (element.attribute(attributeName) == null || element.attribute(attributeName).getValue() == null || element.attribute(attributeName).getValue().isBlank()) {
            return null;
        } else {
            return TypeParser.parse(element.attribute(attributeName).getValue(), clazz);
        }
    }

    public static String convertAbsoluteXpathToCss(String xpath) {
        String cssSelector = xpath.replace(NODE, CHILD_COMBINATOR);

        // Use regular expression to replace [number] with :nth-of-type(number)
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
        Matcher matcher = pattern.matcher(cssSelector);
        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(builder, ":nth-of-type(" + matcher.group(1) + ")");
        }
        matcher.appendTail(builder);

        var semiFinalLocator = builder.toString();

        if (semiFinalLocator.startsWith(CHILD_COMBINATOR)) {
            semiFinalLocator = semiFinalLocator.substring(2);
        }

        return semiFinalLocator;
    }

    public static String removeDanglingChildCombinatorsFromCss(String css) {
        // convert to array by splitting the css by the child combinator
        // and remove from that array empty steps
        var steps = Arrays.stream(css.split(CHILD_COMBINATOR))
                .filter(x -> !x.isBlank())
                .toArray(String[]::new);

        // join the remaining steps with child combinator operators
        return String.join(CHILD_COMBINATOR, steps);
    }
}
