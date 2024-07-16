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

package solutions.bellatrix.core.utilities;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ShutdownManager {
    private static final List<Runnable> instructions = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ShutdownManager::runAllInstructions));
    }

    public static void register(Runnable runnable) {
        instructions.add(runnable);
    }

    private static void runAllInstructions() {
        if (instructions.isEmpty()) return;

        for (var instruction : instructions) {
            try {
                instruction.run();
            } catch (Exception ignored) {
            }
        }
    }
}