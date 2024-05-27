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
    public static String findCssLocator(String html, String xpath) {
        var absoluteXpath = HtmlService.findElementAbsoluteXpath(html, xpath);

        return HtmlService.convertXpathToCss(absoluteXpath);
    }

    public static String findRelativeCssLocator(Element element, String xpath) {
        var absoluteXpath = HtmlService.findRelativeElementAbsoluteXpath(element, xpath);

        return HtmlService.convertXpathToCss(absoluteXpath);
    }

    public static ArrayList<String> findCssLocators(String html, String xpath) {
        var absoluteXpaths = HtmlService.findElementsAbsoluteXpath(html, xpath);

        return HtmlService.convertXpathToCss(absoluteXpaths);
    }

    public static ArrayList<String> findRelativeCssLocators(Element element, String xpath) {
        var absoluteXpaths = HtmlService.findRelativeElementsAbsoluteXpath(element, xpath);

        return HtmlService.convertXpathToCss(absoluteXpaths);
    }

    public static Element findElement(String html, String cssQuery) {
        var doc = Jsoup.parse(html);

        return doc.select(cssQuery).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No element was found with the css: %s", cssQuery)));
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

    private static String convertXpathToCss(String xpath) {
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

    private static ArrayList<String> convertXpathToCss(ArrayList<String> queries) {
        for (String query : queries) {
            query = convertXpathToCss(query);
        }

        return queries;
    }

    private static String getAbsoluteXPath(Element element) {
        StringBuilder xpath = new StringBuilder("/");

        for (Element el : element.parents()) {

            if (el.tagName().equals("html") || el.tagName().equals("body")) {
                // ignore the <html> and <body>, because jsoup added them to the html fragment
                continue;
            }

            int index = 1;
            for (Element sibling : el.siblingElements()) {
                if (sibling.tagName().equals(el.tagName())) {
                    index++;
                }
            }
            xpath.insert(0, "/" + el.tagName() + "[" + index + "]");
        }

        xpath.append(element.tagName());

        return xpath.toString();
    }
}
