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
package solutions.bellatrix.core.assertions;

import java.time.Duration;
import java.time.LocalDateTime;

public class LocalDateTimeAssert {
    public static void areEqual(LocalDateTime expectedDate, LocalDateTime actualDate, DateTimeDeltaType deltaType, int count, String exceptionMessage) throws Exception {
        if (expectedDate == null && actualDate == null){
            return;
        }
        else if (expectedDate == null){
            throw new NullPointerException("The expected date was null");
        }
        else if (actualDate == null){
            throw new NullPointerException("The actual date was null");
        }

        Duration expectedDelta = getTimeSpanDeltaByType(deltaType, count);
        long totalSecondsDifference = Math.abs(Duration.between(expectedDate, actualDate).toSeconds());
        if (totalSecondsDifference > expectedDelta.toSeconds())
        {
            var message = exceptionMessage+"\nExpected Date: "+expectedDate+", Actual Date: "+actualDate+
                    " \nExpected Delta: "+expectedDelta+", Actual Delta(in seconds): "+totalSecondsDifference+" (Delta Type: "+deltaType+")";
            throw new Exception(message);
        }

    }
    public static void areEqual(LocalDateTime expectedDate, LocalDateTime actualDate, Duration expectedDelta, String exceptionMessage) {
        if (expectedDate == null && actualDate == null){
            return;
        }
        else if (expectedDate == null){
            throw new NullPointerException("The expected date was null");
        }
        else if (actualDate == null){
            throw new NullPointerException("The actual date was null");
        }

        var actualDelta = Duration.between(expectedDate, actualDate);

        if (Math.abs(actualDelta.toSeconds()) > expectedDelta.toSeconds())
        {
            var message = exceptionMessage+"\nExpected Date: "+expectedDate+", Actual Date: "+actualDate+
                    " \nExpected Delta: "+expectedDelta+", Actual Delta: "+actualDelta;
            throw new RuntimeException(message);
        }
    }
    private static Duration getTimeSpanDeltaByType(DateTimeDeltaType type, int count)  throws UnsupportedOperationException {
        Duration result;
        switch (type)
        {
            case DAYS:
                result = Duration.ofDays(count);
                break;
            case HOURS:
                result = Duration.ofHours(count);
                break;
            case MINUTES:
                result = Duration.ofMinutes(count);
                break;
            case SECONDS:
                result = Duration.ofSeconds(count);
                break;
            case MILLISECONDS:
                result = Duration.ofMillis(count);
                break;
            default:
                throw new UnsupportedOperationException("The type is not implemented yet.");
        }
        return result;
    }
}
