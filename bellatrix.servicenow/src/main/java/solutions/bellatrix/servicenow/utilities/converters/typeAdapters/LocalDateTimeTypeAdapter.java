package solutions.bellatrix.servicenow.utilities.converters.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter;

    public LocalDateTimeTypeAdapter() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("America/Los_Angeles"));
    }

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(formatter.format(value));
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (Objects.isNull(in.peek())) {
            return null;
        }

        String dateTimeString = in.nextString();
        if (dateTimeString.isEmpty()) {
            return null;
        }

        return LocalDateTime.parse(dateTimeString, formatter);
    }
}