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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Web Utility Class
 */
@UtilityClass
public class HtmlService {
    public static String convertXpathToAbsoluteCssLocator(String html, String xpath) {
        var absoluteXpath = HtmlService.findElementAbsoluteXpath(html, xpath);

        return HtmlService.convertAbsoluteXpathToCss(absoluteXpath);
    }

    public static String convertXpathToAbsoluteCssLocator(Element element, String xpath) {
        var absoluteXpath = HtmlService.findRelativeElementAbsoluteXpath(element, xpath);

        return HtmlService.convertAbsoluteXpathToCss(absoluteXpath);
    }

    public static ArrayList<String> convertXpathToAbsoluteCssLocators(String html, String xpath) {
        var absoluteXpaths = HtmlService.findElementsAbsoluteXpath(html, xpath);

        return HtmlService.convertAbsoluteXpathToCss(absoluteXpaths);
    }

    public static ArrayList<String> convertXpathToAbsoluteCssLocators(Element element, String xpath) {
        var absoluteXpaths = HtmlService.findRelativeElementsAbsoluteXpath(element, xpath);

        return HtmlService.convertAbsoluteXpathToCss(absoluteXpaths);
    }

    public static Element findElement(String html, String cssQuery) {
        var doc = Jsoup.parse(html);

        return doc.select(cssQuery).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No element was found with the css: %s", cssQuery)));
    }

    public static String getAbsoluteXPath(Element element) {
        StringBuilder xpath = new StringBuilder();

        Element currentElement = element;
        while (currentElement != null) {
            if (currentElement.tagName().equals("html") || currentElement.tagName().equals("body") || currentElement.tagName().startsWith("#")) {
                // ignore the <html> and <body>, because jsoup added them to the html fragment
                break;
            }

            xpath.insert(0, indexElement(currentElement));

            currentElement = currentElement.parent();
        }

        return xpath.toString();
    }

    private String indexElement(Element element) {
        int index = 1;

        Element previousSibling = element.previousElementSibling();
        while (previousSibling != null) {
            if (previousSibling.tagName().equals(element.tagName())) {
                index++;
            }
            previousSibling = previousSibling.previousElementSibling();
        }

        if (index == 1) {
            return "/" + element.tagName();
        } else {
            return "/" + element.tagName() + "[" + index + "]";
        }
    }

    private static String findElementAbsoluteXpath(String html, String xpath) {
        var doc = Jsoup.parse(html);
        var el = doc.selectXpath(xpath);

        if (el.isEmpty()) {
            throw new IllegalArgumentException(String.format("No element was found with the xpath: %s", xpath));
        }

        return getAbsoluteXPath(el.first());
    }

    private static ArrayList<String> findElementsAbsoluteXpath(String html, String xpath) {
        var doc = Jsoup.parse(html);
        var elements = doc.selectXpath(xpath).stream().toList();

        if (elements.isEmpty()) {
            throw new IllegalArgumentException(String.format("No elements were found with the xpath: %s", xpath));
        }

        ArrayList<String> individualQueries = new ArrayList<>();

        for (Element el : elements) {
            individualQueries.add(getAbsoluteXPath(el));
        }

        return individualQueries;
    }

    private static String findRelativeElementAbsoluteXpath(Element baseElement, String xpath) {
        var el = baseElement.selectXpath(xpath);

        if (el.isEmpty()) {
            throw new IllegalArgumentException(String.format("No element was found with the xpath: %s", xpath));
        }

        return getAbsoluteXPath(el.first());
    }

    private static ArrayList<String> findRelativeElementsAbsoluteXpath(Element baseElement, String xpath) {
        var elements = baseElement.selectXpath(xpath).stream().toList();

        if (elements.isEmpty()) {
            throw new IllegalArgumentException(String.format("No elements were found with the xpath: %s", xpath));
        }

        ArrayList<String> queries = new ArrayList<String>();

        for (Element el : elements) {
            queries.add(getAbsoluteXPath(el));
        }

        return queries;
    }

    private static String convertAbsoluteXpathToCss(String xpath) {
        String cssSelector = xpath.replace("/", " > ");

        // Use regular expression to replace [number] with :nth-child(number)
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
        Matcher matcher = pattern.matcher(cssSelector);
        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(builder, ":nth-child(" + matcher.group(1) + ")");
        }
        matcher.appendTail(builder);

        var semiFinalLocator = builder.toString();

        if (semiFinalLocator.startsWith(" > ")) {
            semiFinalLocator = semiFinalLocator.substring(2);
        }

        return semiFinalLocator;
    }

    private static ArrayList<String> convertAbsoluteXpathToCss(ArrayList<String> queries) {
        for (String query : queries) {
            query = convertAbsoluteXpathToCss(query);
        }

        return queries;
    }

}
