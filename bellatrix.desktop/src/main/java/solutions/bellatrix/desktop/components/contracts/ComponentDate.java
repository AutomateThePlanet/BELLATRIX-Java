/*
 * Copyright 2022 Automate The Planet Ltd.
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

package solutions.bellatrix.desktop.components.contracts;

import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.desktop.components.DesktopComponent;
import solutions.bellatrix.desktop.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface ComponentDate extends Component {
    String getDate();

    @SneakyThrows
    default void validateDateIs(String value) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeIs", DesktopComponent.class, String.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (DesktopComponent)this, getDate(), value, "date");
        } catch (
                InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @SneakyThrows
    default void validateDateContains(String value) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeContains", DesktopComponent.class, String.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (DesktopComponent)this, getDate(), value, "date");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @SneakyThrows
    default void validateDateNotContains(String value) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeNotContains", DesktopComponent.class, String.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (DesktopComponent)this, getDate(), value, "date");
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
