/*
 * Copyright 2024 Automate The Planet Ltd.
 * Author: Nikolay Avramov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.web.infrastructure;/*
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

import java.util.Arrays;

public enum MaxBrowserLifeSpan {
    NOT_SET("not_set"),
    CLASS("class"),
    TEST_RUN("test_run");

    private final String text;

    MaxBrowserLifeSpan(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static MaxBrowserLifeSpan fromText(String text) {
        return Arrays.stream(values())
                .filter(l -> l.text.equalsIgnoreCase(text) ||
                        l.text.replace("_", " ").equalsIgnoreCase(text) ||
                        l.text.replace("_", "").equalsIgnoreCase(text))
                .findFirst().orElse(MaxBrowserLifeSpan.NOT_SET);
    }
}
