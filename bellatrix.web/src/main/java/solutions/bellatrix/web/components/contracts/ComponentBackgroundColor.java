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

import org.openqa.selenium.support.Colors;
import solutions.bellatrix.core.utilities.SingletonFactory;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.services.JavaScriptService;
import solutions.bellatrix.web.validations.ComponentValidator;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public interface ComponentBackgroundColor extends Component {

    default String getBackgroundColor(){
        var script = "return arguments[0].style.background";

        return new JavaScriptService().execute(script, (WebComponent)this);
    }

    default void validateBackgroundColor(Colors expectedColor) {
        try {
            Method method = ComponentValidator.class.getDeclaredMethod("defaultValidateAttributeIs", WebComponent.class, Supplier.class, String.class, String.class);
            method.invoke(SingletonFactory.getInstance(ComponentValidator.class), (WebComponent) this, (Supplier<String>)this::getBackgroundColor, expectedColor.getColorValue().getColor().getRGB(), String.format("expected color should be %s", expectedColor));
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
