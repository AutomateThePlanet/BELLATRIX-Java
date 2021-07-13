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

package solutions.bellatrix.android.infrastructure;

import java.util.Arrays;

public enum Lifecycle {
    NOT_SET("not_set"),
    REUSE_IF_STARTED("reuse_if_started"),
    RESTART_EVERY_TIME("restart_every_time"),
    RESTART_ON_FAIL("restart_on_fail");

    private final String text;

    Lifecycle(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Lifecycle fromText(String text) {
        return Arrays.stream(values())
                .filter(l -> l.text.equalsIgnoreCase(text) ||
                        l.text.replace("_", " ").equalsIgnoreCase(text) ||
                        l.text.replace("_", "").equalsIgnoreCase(text))
                .findFirst().orElse(Lifecycle.RESTART_EVERY_TIME);
    }
}
