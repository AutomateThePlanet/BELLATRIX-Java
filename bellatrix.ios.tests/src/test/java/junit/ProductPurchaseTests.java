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

package junit;

import org.junit.jupiter.api.Test;
import solutions.bellatrix.ios.components.Button;
import solutions.bellatrix.ios.infrastructure.ExecutionApp;
import solutions.bellatrix.ios.infrastructure.Lifecycle;
import solutions.bellatrix.ios.infrastructure.junit.IOSTest;

@ExecutionApp(lifecycle = Lifecycle.RESTART_ON_FAIL)
public class ProductPurchaseTests extends IOSTest {
    @Test
    public void buttonClicked_when_callClickMethod() {
        var button = app().create().byName(Button.class, "ComputeSumButton");
        button.click();
    }
}