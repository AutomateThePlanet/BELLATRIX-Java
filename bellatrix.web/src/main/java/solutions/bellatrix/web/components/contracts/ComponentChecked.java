/*
 * Copyright 2022 Automate The Planet Ltd.
 * Author: Teodor Nikolov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.components.contracts;

import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.validations.ComponentValidator;

public interface ComponentChecked extends Component {
    boolean isChecked();

    default void validateIsChecked() {
        ComponentValidator.defaultValidateAttributeTrue((WebComponent)this, this::isChecked, "checked");
    }

    default void validateIsChecked(boolean value) {
        if (value){
            validateIsChecked();
        }
        else {
            validateIsUnchecked();
        }
    }

    default void validateIsUnchecked() {
        ComponentValidator.defaultValidateAttributeTrue((WebComponent)this, () -> !isChecked(), "unchecked");
    }
}
