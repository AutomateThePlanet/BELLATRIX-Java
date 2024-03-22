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
import solutions.bellatrix.web.components.enums.CssStyle;
import solutions.bellatrix.web.services.JavaScriptService;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public interface ComponentStyle extends Component {
    String getStyle();

    @SneakyThrows
    default void validateStyleIs(String value) {
        ComponentValidator.defaultValidateAttributeIs((WebComponent)this, this::getStyle, value, "style");
    }

    @SneakyThrows
    default void validateStyleIsSet() {
        ComponentValidator.defaultValidateAttributeIsSet((WebComponent)this, this::getStyle, "style");
    }

    @SneakyThrows
    default void validateStyleNotSet() {
        ComponentValidator.defaultValidateAttributeNotSet((WebComponent)this, this::getStyle, "style");
    }

    @SneakyThrows
    default void validateStyleContains(String value) {
        ComponentValidator.defaultValidateAttributeContains((WebComponent)this, this::getStyle, value, "style");
    }

    @SneakyThrows
    default void validateStyleNotContains(String value) {
        ComponentValidator.defaultValidateAttributeNotContains((WebComponent)this, this::getStyle, value, "style");
    }

    default String getStyle(CssStyle style) {
        var script = String.format("return window.getComputedStyle(arguments[0],null).getPropertyValue('%s');", style);
        var result = new JavaScriptService().execute(script, (WebComponent) this);

        return result;
    }

    @SneakyThrows
    default void validateStyle(CssStyle style, String expectedValue) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeIs", WebComponent.class, Supplier.class, java.lang.String.class, java.lang.String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (WebComponent) this, (Supplier<Object>) () -> this.getStyle(style), expectedValue, java.lang.String.format("expected color should be %s", expectedValue));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
