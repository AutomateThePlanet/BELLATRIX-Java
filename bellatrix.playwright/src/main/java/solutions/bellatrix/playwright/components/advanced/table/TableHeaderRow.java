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

package solutions.bellatrix.playwright.components.advanced.table;

import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.advanced.services.TableLocators;
import solutions.bellatrix.playwright.components.contracts.ComponentHtml;

import java.util.List;

public class TableHeaderRow extends WebComponent implements ComponentHtml {
    public List<TableCell> getHeaderCells() {
        return this.createAllByTag(TableCell.class, locators().getHeaderTag());
    }

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    private TableLocators locators() {
        return SingletonFactory.getInstance(TableLocators.class);
    }
}
