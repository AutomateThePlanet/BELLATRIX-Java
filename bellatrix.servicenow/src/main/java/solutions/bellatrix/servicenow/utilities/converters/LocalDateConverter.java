package solutions.bellatrix.servicenow.utilities.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    DateTimeFormatter serializeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String PACIFIC_TIME_ZONE = "America/Los_Angeles";

    @Override
    public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(serializeFormatter.format(localDate));
    }

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var elementAsString = jsonElement.getAsString();
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of(PACIFIC_TIME_ZONE));

        if (elementAsString.isEmpty()) {
            return null;
        }

        if (elementAsString.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z$")) {
            elementAsString = elementAsString.substring(0, 10);
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        if (elementAsString.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
            elementAsString = elementAsString.substring(0, 10);
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        if (elementAsString.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        }

        return LocalDate.parse(elementAsString, dateTimeFormatter);
    }
}