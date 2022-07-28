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

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampBuilder {
    public static synchronized String getGuid() {
        return java.util.UUID.randomUUID().toString();
    }

    public static synchronized String buildUniqueTextByPrefix(String prefix) {
        return buildUniqueText(prefix, "", "");
    }

    public static synchronized String buildUniqueTextByPrefix(String prefix, String separator) {
        return buildUniqueText(prefix, "", separator);
    }

    public static synchronized String buildUniqueTextBySuffix(String suffix) {
        return buildUniqueText("", suffix, "");
    }

    public static synchronized String buildUniqueTextBySuffix(String suffix, String separator) {
        return buildUniqueText("", suffix, separator);
    }

    public static synchronized String buildUniqueText(String prefix, String suffix, String separator) {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String datetime = ft.format(dNow);
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prefix + separator + datetime + separator + suffix;
    }
}
