/*
 * Copyright 2021 Automate The Planet Ltd.
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
import solutions.bellatrix.web.components.validations.WebValidator;

import java.lang.reflect.Method;

public interface ComponentRequired extends Component {
    boolean isRequired();

    @SneakyThrows
    default void validateIsRequired() {
        Method method = WebValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", WebComponent.class, boolean.class, String.class);
        method.invoke(SingletonFactory.getInstance(WebValidator.class), (WebComponent)this, isRequired(), "required");
    }

    @SneakyThrows
    default void validateNotRequired() {
        Method method = WebValidator.class.getDeclaredMethod("defaultValidateAttributeFalse", WebComponent.class, boolean.class, String.class);
        method.invoke(SingletonFactory.getInstance(WebValidator.class), (WebComponent)this, isRequired(), "required");
    }
}