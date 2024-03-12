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

package components.common;

import components.WebComponent;
import lombok.Getter;

@Getter
public class ComponentActionEventArgs {
    private final WebComponent component;
    private final String actionValue;
    private final String message;

    public ComponentActionEventArgs(WebComponent component, String actionValue, String message) {
        this.component = component;
        this.actionValue = actionValue;
        this.message = message;
    }

    public ComponentActionEventArgs(WebComponent component, String actionValue) {
        this.component = component;
        this.actionValue = actionValue;
        message = "";
    }

    public ComponentActionEventArgs(WebComponent component) {
        this.component = component;
        this.actionValue = "";
        message = "";
    }
}
