package data.converters.typeAdapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DurationConverter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    DateTimeFormatter serializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(Duration duration, Type srcType, JsonSerializationContext context) {
        var endDate = LocalDateTime.ofEpochSecond(0,(int)duration.toNanos(), ZoneOffset.of("+00:00"));

        return new JsonPrimitive(serializeFormatter.format(endDate));
    }

    @Override
    public Duration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var elementAsString = jsonElement.getAsString();

        if (elementAsString.isEmpty()) {
            return null;
        }

        var endDateTime = LocalDateTime.parse(elementAsString, serializeFormatter);
        var startDateTime = LocalDateTime.parse("1970-01-01 00:00:00", serializeFormatter);

        return Duration.between(startDateTime, endDateTime);
    }
}