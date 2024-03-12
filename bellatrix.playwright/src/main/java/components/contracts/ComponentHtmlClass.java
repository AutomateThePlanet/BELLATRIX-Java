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

package components.contracts;

import components.WebComponent;
import components.common.validate.ComponentValidator;

public interface ComponentHtmlClass extends Component {
    String getHtmlClass();

    default void validateClassIs(String value) {
        ComponentValidator.defaultValidateAttributeIs((WebComponent)this, this::getHtmlClass, value, "class");
    }

    default void validateClassIsSet() {
        ComponentValidator.defaultValidateAttributeIsSet((WebComponent)this, this::getHtmlClass, "class");
    }

    default void validateClassNotSet() {
        ComponentValidator.defaultValidateAttributeNotSet((WebComponent)this, this::getHtmlClass, "class");
    }

    default void validateClassContains(String value) {
        ComponentValidator.defaultValidateAttributeContains((WebComponent)this, this::getHtmlClass, value, "class");
    }

    default void validateClassNotContains(String value) {
        ComponentValidator.defaultValidateAttributeNotContains((WebComponent)this, this::getHtmlClass, value, "class");
    }
}
