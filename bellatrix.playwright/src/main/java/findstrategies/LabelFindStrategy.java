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

package findstrategies;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import findstrategies.options.LabelOptions;
import findstrategies.utils.By;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class LabelFindStrategy extends PatternStrategy {
    public LabelFindStrategy(String text) {
        this.text = text;
    }

    public LabelFindStrategy(Pattern pattern) {
        this.pattern = pattern;
    }

    public LabelFindStrategy(String text, LabelOptions options) {
        this.text = text;
        this.options = options;
    }

    public LabelFindStrategy(Pattern pattern, LabelOptions options) {
        this.pattern = pattern;
        this.options = options;
    }

    @Override
    public Locator convert(Page page) {
        if (text != null) return get(page, By.LABEL, text, ((LabelOptions)options).absolute());
        if (pattern != null) return get(page, By.LABEL, pattern, ((LabelOptions)options).absolute());
        return null;
    }

    @Override
    public Locator convert(Locator locator) {
        if (text != null) return get(locator, By.LABEL, text, ((LabelOptions)options).relative());
        if (pattern != null) return get(locator, By.LABEL, pattern, ((LabelOptions)options).relative());
        return null;
    }
}
