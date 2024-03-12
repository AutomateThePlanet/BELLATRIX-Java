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

package components;

import components.contracts.ComponentFor;
import components.contracts.ComponentHtml;
import components.contracts.ComponentText;

public class Output extends WebComponent implements ComponentHtml, ComponentText, ComponentFor {
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
