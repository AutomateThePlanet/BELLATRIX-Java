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
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public interface ComponentPlaceholder extends Component {
    String getPlaceholder();

    @SneakyThrows
    default void validatePlaceholderIs(String value) {
        ComponentValidator.defaultValidateAttributeIs((WebComponent)this, this::getPlaceholder, value, "placeholder");
    }

    @SneakyThrows
    default void validatePlaceholderIsSet() {
        ComponentValidator.defaultValidateAttributeIsSet((WebComponent)this, this::getPlaceholder, "placeholder");
    }

    @SneakyThrows
    default void validatePlaceholderNotSet() {
        ComponentValidator.defaultValidateAttributeNotSet((WebComponent)this, this::getPlaceholder, "placeholder");
    }

    @SneakyThrows
    default void validatePlaceholderContains(String value) {
        ComponentValidator.defaultValidateAttributeContains((WebComponent)this, this::getPlaceholder, value, "placeholder");
    }

    @SneakyThrows
    default void validatePlaceholderNotContains(String value) {
        ComponentValidator.defaultValidateAttributeNotContains((WebComponent)this, this::getPlaceholder, value, "placeholder");
    }
}
