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

package layout;

import lombok.Getter;
import org.testng.Assert;
import solutions.bellatrix.core.plugins.EventListener;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FinishValidationBuilder {
    public final static EventListener<LayoutValidationEventArgs> VALIDATED_COMPONENT_LAYOUT = new EventListener<>();

    @Getter private final Predicate comparingFunction;
    private String notificationMessage;
    private String failedAssertionMessage;

    public FinishValidationBuilder(Predicate comparingFunction, Supplier<String> notificationMessageFunction, Supplier<String> failedAssertionMessageFunction) {
        this.comparingFunction = comparingFunction;
        this.notificationMessage = notificationMessageFunction.get();
        this.failedAssertionMessage = failedAssertionMessageFunction.get();
    }

    public FinishValidationBuilder(Predicate comparingFunction) {
        this.comparingFunction = comparingFunction;
    }

    public void validate() {
        Assert.assertTrue(comparingFunction.test(true), failedAssertionMessage);
        VALIDATED_COMPONENT_LAYOUT.broadcast(new LayoutValidationEventArgs(notificationMessage));
    }
}
