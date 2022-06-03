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
    public static void areEqual(LocalDateTime expectedDate, LocalDateTime actualDate, Duration expectedDelta, String exceptionMessage)  throws Exception {
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
            throw new Exception(message);
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
