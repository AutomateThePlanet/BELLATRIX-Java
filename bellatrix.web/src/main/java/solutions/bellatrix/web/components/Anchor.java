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

package solutions.bellatrix.web.components;

import solutions.bellatrix.core.plugins.EventListener;

public class Anchor extends WebComponent {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    public String getHref() {
        return defaultGetHref();
    }

    public String getText() {
        return defaultGetText();
    }

    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    public String getTarget() {
        return defaultGetTargetAttribute();
    }

    public String getRel() {
        return defaultGetRelAttribute();
    }

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }

    // validate inner HTML
    public void validateHtmlIs(String value) {
        defaultValidateAttributeIs(this::getHtml, value, "inner HTML");
    }

    public void validateHtmlContains(String value) {
        defaultValidateAttributeContains(this::getHtml, value, "inner HTML");
    }

    public void validateHtmlNotContains(String value) {
        defaultValidateAttributeNotContains(this::getHtml, value, "inner HTML");
    }

    // validate inner text
    public void validateTextIs(String value) {
        defaultValidateAttributeIs(this::getText, value, "inner text");
    }

    public void validateTextIsSet() {
        defaultValidateAttributeSet(this::getText, "inner text");
    }

    public void validateTextContains(String value) {
        defaultValidateAttributeContains(this::getText, value, "inner text");
    }

    public void validateTextNotContains(String value) {
        defaultValidateAttributeNotContains(this::getText, value, "inner text");
    }

    // validate HREF
    public void validateHrefIs(String value) {
        defaultValidateAttributeIs(this::getHref, value, "href");
    }

    public void validateHrefIsSet() {
        defaultValidateAttributeSet(this::getHref, "href");
    }

    public void validateHrefNotSet() {
        defaultValidateAttributeNotSet(this::getHref, "href");
    }

    // validate Target
    public void validateTargetIs(String value) {
        defaultValidateAttributeIs(this::getTarget, value, "target");
    }

    public void validateTargetSet() {
        defaultValidateAttributeSet(this::getTarget, "target");
    }

    public void validateTargetNotSet() {
        defaultValidateAttributeNotSet(this::getTarget, "target");
    }

    // validate Rel
    public void validateRelIs(String value) {
        defaultValidateAttributeIs(this::getRel, value, "rel");
    }

    public void validateRelSet() {
        defaultValidateAttributeSet(this::getRel, "rel");
    }

    public void validateRelNotSet() {
        defaultValidateAttributeNotSet(this::getRel, "rel");
    }
}