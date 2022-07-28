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
import java.util.function.BooleanSupplier;

public interface ComponentSpellCheck extends Component {
    boolean isSpellCheck();

    @SneakyThrows
    default void validateSpellCheckOn() {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", WebComponent.class, BooleanSupplier.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (WebComponent)this, (BooleanSupplier)this::isSpellCheck, "spellcheck");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @SneakyThrows
    default void validateSpellCheckOff() {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeFalse", WebComponent.class, BooleanSupplier.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (WebComponent)this, (BooleanSupplier)this::isSpellCheck, "spellcheck");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
