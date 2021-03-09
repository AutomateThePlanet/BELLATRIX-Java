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
import java.time.LocalDateTime;

public interface ComponentTime extends Component {
    String getTime();
    void setTime(int hours, int minutes);

    @SneakyThrows
    default void validateTimeIs(String value) {
        Method method = WebValidator.class.getDeclaredMethod("defaultValidateAttributeIs", WebComponent.class, String.class, String.class, String.class);
        method.invoke(SingletonFactory.getInstance(WebValidator.class), (WebComponent)this, getTime(), value, "time");
    }

    @SneakyThrows
    default void validateTimeContains(String value) {
        Method method = WebValidator.class.getDeclaredMethod("defaultValidateAttributeContains", WebComponent.class, String.class, String.class, String.class);
        method.invoke(SingletonFactory.getInstance(WebValidator.class), (WebComponent)this, getTime(), value, "time");
    }

    @SneakyThrows
    default void validateTimeNotContains(String value) {
        Method method = WebValidator.class.getDeclaredMethod("defaultValidateAttributeNotContains", WebComponent.class, String.class, String.class, String.class);
        method.invoke(SingletonFactory.getInstance(WebValidator.class), (WebComponent)this, getTime(), value, "time");
    }
}
