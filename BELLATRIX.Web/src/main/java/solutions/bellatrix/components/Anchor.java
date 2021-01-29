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

package solutions.bellatrix.components;

import solutions.bellatrix.plugins.EventListener;

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

    public void validateHrefIs(String value) {
        defaultValidateHrefIs(value);
    }

    public void validateHrefIsSet() {
        defaultValidateHrefIsSet();
    }
}