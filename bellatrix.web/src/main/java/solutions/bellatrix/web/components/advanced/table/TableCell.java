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

package solutions.bellatrix.web.components.advanced.table;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.components.contracts.ComponentText;

@Getter @Setter
public class TableCell extends WebComponent implements ComponentHtml, ComponentText {
    private int column;
    private int row;

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }
}
