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

package pages;

import solutions.bellatrix.components.Anchor;
import solutions.bellatrix.components.WebComponent;
import solutions.bellatrix.pages.WebSection;

public class BreadcrumbSection extends WebSection {

    private WebComponent breadcrumb() {
        return create().byCss(WebComponent.class, "woocommerce-breadcrumb");
    }

    // TODO: add create methods to component
    public void openBreadcrumbItem(String itemToOpen) {
        breadcrumb().createByLinkText(Anchor.class, itemToOpen).click();
    }
}
