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

package solutions.bellatrix.ios.components.contracts;

import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.ios.components.IOSComponent;
import solutions.bellatrix.ios.components.validations.IOSValidator;

import java.lang.reflect.Method;

public interface ComponentSelected extends Component {
    boolean isSelected();

    @SneakyThrows
    default void validateIsSelected() {
        Method method = IOSValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", IOSComponent.class, boolean.class, String.class);
        method.invoke(SingletonFactory.getInstance(IOSValidator.class), (IOSComponent)this, isSelected(), "selected");
    }

    @SneakyThrows
    default void validateNotSelected() {
        Method method = IOSValidator.class.getDeclaredMethod("defaultValidateAttributeFalse", IOSComponent.class, boolean.class, String.class);
        method.invoke(SingletonFactory.getInstance(IOSValidator.class), (IOSComponent)this, isSelected(), "selected");
    }
}
