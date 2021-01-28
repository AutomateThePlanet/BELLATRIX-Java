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

package solutions.bellatrix.pages;

import lombok.Getter;
import solutions.bellatrix.services.NavigationService;

public abstract class NavigatableAssertableWebPage<ElementsT extends BaseElements, AssertionsT extends BaseAssertions<ElementsT>> extends AssertableWebPage<ElementsT, AssertionsT> {
    @Getter
    protected final NavigationService navigationService;

    public NavigatableAssertableWebPage() {
        this.navigationService = new NavigationService();
    }

    protected abstract String getUrl();

    public void open() {
        getNavigationService().open(getUrl());
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
    }
}
