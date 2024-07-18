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
import java.util.Objects;
import java.util.function.Supplier;

public interface ComponentStyle extends Component {
    default String getStyle() {
        return getAttribute("style");
    }

    default String getStyle(CssStyle style) {
        return getWrappedElement().getCssValue(style.toString());
    }

    default void validateStyleIs(String value) {
        ComponentValidator.defaultValidateAttributeIs((WebComponent)this, this::getStyle, value, "style");
    }

    default void validateStyleIsSet() {
        ComponentValidator.defaultValidateAttributeIsSet((WebComponent)this, this::getStyle, "style");
    }

    default void validateStyleNotSet() {
        ComponentValidator.defaultValidateAttributeNotSet((WebComponent)this, this::getStyle, "style");
    }

    default void validateStyleContains(String value) {
        ComponentValidator.defaultValidateAttributeContains((WebComponent)this, this::getStyle, value, "style");
    }

    default void validateStyleNotContains(String value) {
        ComponentValidator.defaultValidateAttributeNotContains((WebComponent)this, this::getStyle, value, "style");
    }

    default void validateStyle(CssStyle style, String expectedValue) {
        ComponentValidator.defaultValidateAttributeIs((WebComponent)this, () -> this.getStyle(style), expectedValue, style.toString());
    }
}
