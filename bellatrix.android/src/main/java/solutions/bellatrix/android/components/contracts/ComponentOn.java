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

package solutions.bellatrix.android.components.contracts;

import lombok.SneakyThrows;
import solutions.bellatrix.android.components.AndroidComponent;
import solutions.bellatrix.android.components.validations.AndroidValidator;
import solutions.bellatrix.core.utilities.SingletonFactory;

import java.lang.reflect.Method;

public interface ComponentOn extends Component {
    boolean isOn();

    @SneakyThrows
    default void validateIsOn() {
        Method method = AndroidValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", AndroidComponent.class, boolean.class, String.class);
        method.invoke(SingletonFactory.getInstance(AndroidValidator.class), (AndroidComponent)this, isOn(), "on");
    }

    @SneakyThrows
    default void validateIsOff() {
        Method method = AndroidValidator.class.getDeclaredMethod("defaultValidateAttributeTrue", AndroidComponent.class, boolean.class, String.class);
        method.invoke(SingletonFactory.getInstance(AndroidValidator.class), (AndroidComponent)this, !isOn(), "off");
    }
}
