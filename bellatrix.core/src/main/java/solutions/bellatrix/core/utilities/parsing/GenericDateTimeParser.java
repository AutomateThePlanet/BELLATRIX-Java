package solutions.bellatrix.core.utilities.parsing;

import lombok.experimental.UtilityClass;
import solutions.bellatrix.core.utilities.Ref;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class GenericDateTimeParser {
    public static List<DateTimeFormatter> FORMATTERS = new ArrayList<>();
    public static List<DateTimeFormatter> PRIORITIZED_FORMATTERS = new ArrayList<>();

    public static void addPattern(String pattern) {
        FORMATTERS.add(DateTimeFormatter.ofPattern(pattern));
    }

    public static void prioritizePattern(String pattern) {
        PRIORITIZED_FORMATTERS.add(DateTimeFormatter.ofPattern(pattern));
    }

    static {
        FORMATTERS.add(DateTimeFormatter.ISO_DATE_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_DATE);
        FORMATTERS.add(DateTimeFormatter.BASIC_ISO_DATE);
        FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE);
        FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_OFFSET_DATE);
        FORMATTERS.add(DateTimeFormatter.ISO_OFFSET_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        FORMATTERS.add(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS'Z'"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy.MM.dd G 'at' HH:mm:ss z"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy.MM.dd GGGGG 'at' HH:mm:ss z"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy.MM.dd GGGGG 'at' HH:mm:ss z"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd HHmmss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd HHmmssSSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd HHmm"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd HH"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMdd"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss.SSSXXX"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        FORMATTERS.add(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static LocalDateTime parse(String data) {
        if (!PRIORITIZED_FORMATTERS.isEmpty()) {
            for (var formatter : PRIORITIZED_FORMATTERS) {
                try {
                    return LocalDateTime.parse(data, formatter);
                } catch (DateTimeParseException ignored){
                }
            }
        }
        for (var formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(data, formatter);
            } catch (DateTimeParseException ignored){
            }
        }

        throw new IllegalArgumentException(String.format("There was no proper format found for input string '%s'." +
                "Please register your custom format in %s by using the add method!", data, GenericDateTimeParser.class.getName()));
    }

    public static boolean tryParse(String data, Ref<LocalDateTime> returnObject) {
        if (!PRIORITIZED_FORMATTERS.isEmpty()) {
            for (var formatter : PRIORITIZED_FORMATTERS) {
                try {
                    returnObject.value = LocalDateTime.parse(data, formatter);
                    return true;
                } catch (DateTimeParseException ignored){
                }
            }
        }
        for (var formatter : FORMATTERS) {
            try {
                returnObject.value = LocalDateTime.parse(data, formatter);
                return true;
            } catch (DateTimeParseException ignored){
            }
        }
        returnObject.value = null;
        return false;
    }
}
