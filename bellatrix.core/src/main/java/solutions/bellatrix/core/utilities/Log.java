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

import solutions.bellatrix.core.plugins.EventListener;

public class Log {
    public final static EventListener<LoggerActionEventArgs> LOGGED_ENTRY = new EventListener<>();
    public final static EventListener<LoggerActionEventArgs> LOGGED_INFO = new EventListener<>();
    public final static EventListener<LoggerActionEventArgs> LOGGED_ERROR = new EventListener<>();

    public static void info(String format, Object... args) {
        String info = format;
        if (args != null && args.length > 0) {
            info = String.format(format, args);
        }

        LOGGED_ENTRY.broadcast(new LoggerActionEventArgs(info));
        LOGGED_INFO.broadcast(new LoggerActionEventArgs(info));
        System.out.println(info);
    }

    public static void error(String format, Object... args) {
        String error = format;
        if (args != null && args.length > 0) {
            error = String.format(format, args);
        }

        LOGGED_ENTRY.broadcast(new LoggerActionEventArgs(error));
        LOGGED_ERROR.broadcast(new LoggerActionEventArgs(error));
        System.err.println(error);
    }
}
