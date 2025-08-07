package solutions.bellatrix.servicenow.utilities.converters.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
    private static final String PACIFIC_TIME_ZONE = "America/Los_Angeles";
    private final DateTimeFormatter serializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(serializeFormatter.format(value));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        String dateStr = in.nextString().trim();

        if (dateStr.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter;

        if (dateStr.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$")) {
            // Example: 2025-07-16T13:45:00.123Z
            dateStr = dateStr.substring(0, 10);
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if (dateStr.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
            // Example: 2025-07-16 13:45:00
            dateStr = dateStr.substring(0, 10);
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if (dateStr.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            // Example: 07/16/2025
            formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        } else {
            // Default to yyyy-MM-dd with timezone (ignored in LocalDate parsing)
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.of(PACIFIC_TIME_ZONE));
        }

        return LocalDate.parse(dateStr, formatter);
    }
}