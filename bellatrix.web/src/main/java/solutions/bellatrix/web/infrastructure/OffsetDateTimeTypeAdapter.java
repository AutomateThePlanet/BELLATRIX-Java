package solutions.bellatrix.web.infrastructure;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeTypeAdapter implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public JsonElement serialize(OffsetDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(src));
    }

    @Override
    public OffsetDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        OffsetDateTime result;
        try {
            result = OffsetDateTime.parse(json.getAsString(), formatter);
        }
        catch(Exception ex) {
            result = LocalDateTime.parse(json.getAsString(), localDateTimeFormatter).atOffset(ZoneOffset.UTC);
        }

        return result;
    }
}