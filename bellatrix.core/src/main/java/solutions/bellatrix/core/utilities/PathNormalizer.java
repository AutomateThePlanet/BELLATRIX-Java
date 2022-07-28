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
package solutions.bellatrix.core.utilities;

import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathNormalizer {
    public static String normalizePath(String path) {
        var fileSeparator = StringEscapeUtils.escapeJava(System.getProperty("file.separator"));
        path = path.replaceAll("[\\\\/]", fileSeparator);
        Matcher matcher = Pattern.compile("\\$\\{(.*?)}").matcher(path);

        while (matcher.find()) {
            String replacement = resolveVariable(matcher.group(1));
            if (replacement != null) {
                path = path.replace(matcher.group(0), replacement);
            }
        }

        return path;
    }

    public static String resolveVariable(String variable) {

////    switch (variable) {
////        case "myVar1" -> { return "resolvedVar1"; }
////        case "myVar2" -> { return "resolvedVar2"; }
////        case "myVar3" -> { return "resolvedVar3"; }
////    }

        return System.getProperty(variable);
    }
}
