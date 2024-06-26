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

import lombok.SneakyThrows;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.validations.ComponentValidator;

public interface ComponentDisabled extends Component {
    boolean isDisabled();

    @SneakyThrows
    default void validateIsDisabled() {
        ComponentValidator.defaultValidateAttributeTrue((WebComponent)this, this::isDisabled, "disabled");
    }

    @SneakyThrows
    default void validateNotDisabled() {
        ComponentValidator.defaultValidateAttributeFalse((WebComponent)this, this::isDisabled, "disabled");
    }
}
