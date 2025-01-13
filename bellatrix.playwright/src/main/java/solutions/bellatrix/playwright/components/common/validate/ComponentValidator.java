/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Miriam Kyoseva
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.playwright.components.common.validate;

import lombok.experimental.UtilityClass;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.common.ComponentActionEventArgs;
import solutions.bellatrix.playwright.utilities.functionalinterfaces.Assertable;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/*
Validator.assertCondition(() -> condition) is used instead of the native Playwright assertion methods
because all the default validation methods here assume that the data to be asserted would be obtained
through the supplier parameter.
Unfortunately, one cannot pass a supplier as a parameter to PlaywrightAssertions, and there is no workaround.

The utility class, ComponentValidator, is designed not only to validate elements' attributes
but also other types of data, such as inner text, inner HTML, etc.

This utility class should not need to 'know' how the data is obtained; it only validates a condition.

Another existing concern is extendability. If, in the future, there is a need to write a custom validation method,
using the native PlaywrightAssertion might prove difficult if Playwright doesn't support such assertions.
 */
@UtilityClass
public class ComponentValidator {
    public final static EventListener<ComponentActionEventArgs> VALIDATING_ATTRIBUTE = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> VALIDATED_ATTRIBUTE = new EventListener<>();

    public static void defaultValidateAttributeIsNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is null", component.getComponentName(), attributeName)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get() == null);
        };
        Validator.validate(assertion, component, attributeName, "null", supplier, "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is null", component.getComponentName(), attributeName)));
    }

    public static void defaultValidateAttributeNotNull(WebComponent component, Supplier<Object> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is NOT null", component.getComponentName(), attributeName)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get() != null);
        };
        Validator.validate(assertion, component, attributeName, "not null", (Supplier<String>) () -> "null", "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is NOT null", component.getComponentName(), attributeName)));
    }

    public static void defaultValidateAttributeIsSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is set", component.getComponentName(), attributeName)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> !supplier.get().isEmpty());
        };
        Validator.validate(assertion, component, attributeName, "set", supplier, "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is set", component.getComponentName(), attributeName)));
    }

    public static void defaultValidateAttributeNotSet(WebComponent component, Supplier<String> supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s's %s is NOT set", component.getComponentName(), attributeName)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get().isEmpty());
        };
        Validator.validate(assertion, component, attributeName, "not set", supplier, "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s's %s is NOT set", component.getComponentName(), attributeName)));
    }

    public static void defaultValidateAttributeIs(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get().equals(value));
        };
        Validator.validate(assertion, component, attributeName, value, supplier, "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public void defaultValidateAttributeIs(WebComponent component, Supplier<Number> supplier, Number value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get().equals(value));
        };
        Validator.validate(assertion, component, attributeName, value, supplier, "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }

    public static void defaultValidateAttributeContains(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s contains '%s'", component.getComponentName(), attributeName, value)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get().contains(value));
        };
        Validator.validate(assertion, component, attributeName, value, supplier, "contain");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s contains '%s'", component.getComponentName(), attributeName, value)));
    }

    public static void defaultValidateAttributeNotContains(WebComponent component, Supplier<String> supplier, String value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validating %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> !supplier.get().contains(value));
        };
        Validator.validate(assertion, component, attributeName, value, supplier, "not contain");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value, String.format("validated %s's %s doesn't contain '%s'", component.getComponentName(), attributeName, value)));
    }

    public static void defaultValidateAttributeTrue(WebComponent component, BooleanSupplier supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is %s", component.getComponentName(), attributeName)));

        Assertable assertion = () -> {
            Validator.validateCondition(supplier::getAsBoolean);
        };
        Validator.validate(assertion, component, attributeName, "true", () -> "false", "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is %s", component.getComponentName(), attributeName)));
    }

    public static void defaultValidateAttributeFalse(WebComponent component, BooleanSupplier supplier, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validating %s is not %s", component.getComponentName(), attributeName)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> !supplier.getAsBoolean());
        };
        Validator.validate(assertion, component, attributeName, "false", () -> "true", "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, null, String.format("validated %s is not %s", component.getComponentName(), attributeName)));
    }

    public static <Entity> void defaultValidateCollectionIs(WebComponent component, Supplier<List<Entity>> supplier, List<Entity> value, String attributeName) {
        VALIDATING_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validating %s's %s is '%s'", component.getComponentName(), attributeName, value)));

        Assertable assertion = () -> {
            Validator.validateCondition(() -> supplier.get().equals(value));
        };
        Validator.validate(assertion, component, attributeName, value, supplier, "be");

        VALIDATED_ATTRIBUTE.broadcast(new ComponentActionEventArgs(component, value.toString(), String.format("validated %s's %s is '%s'", component.getComponentName(), attributeName, value)));
    }
}
