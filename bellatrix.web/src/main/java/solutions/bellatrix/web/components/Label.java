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

package solutions.bellatrix.web.components;

import solutions.bellatrix.web.components.contracts.ComponentFor;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.components.contracts.ComponentText;

public class Label extends WebComponent implements ComponentText, ComponentHtml, ComponentFor {
    @Override
    public Class<?> getComponentClass() {
        return getClass();
    }

    @Override
    public String getFor() {
        return defaultGetForAttribute();
    }

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    @Override
    public String getText() {
        return defaultGetText();
    }
}
