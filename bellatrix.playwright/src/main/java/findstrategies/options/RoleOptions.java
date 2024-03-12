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

package findstrategies.options;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.function.Function;

public class RoleOptions extends Options<Page.GetByRoleOptions, Locator.GetByRoleOptions> {
    private void setAbsolute(Function<Page.GetByRoleOptions, Page.GetByRoleOptions> method) {
        invokeAbsolute(method);
    }

    public static RoleOptions createAbsolute(Function<Page.GetByRoleOptions, Page.GetByRoleOptions> method) {
        var options = new RoleOptions();
        options.setAbsolute(method);
        return options;
    }

    private void setRelative(Function<Locator.GetByRoleOptions, Locator.GetByRoleOptions> method) {
        invokeRelative(method);
    }

    public static RoleOptions createRelative(Function<Locator.GetByRoleOptions, Locator.GetByRoleOptions> method) {
        var options = new RoleOptions();
        options.setRelative(method);

        return options;
    }
}
