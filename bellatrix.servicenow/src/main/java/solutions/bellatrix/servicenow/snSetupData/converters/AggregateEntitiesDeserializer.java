package solutions.bellatrix.servicenow.snSetupData.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import org.openqa.selenium.json.TypeToken;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TypeAdapterFactory;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels.AggregateEntities;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.aggregateApiModels.AggregateField;

public class AggregateEntitiesDeserializer implements JsonDeserializer<AggregateEntities> {
    private final Gson gson;

    public AggregateEntitiesDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping()
            .setLenient()
            .setPrettyPrinting();
        TypeAdapterFactory.allExceptEntities().forEach(gsonBuilder::registerTypeAdapter);

        gson = gsonBuilder.create();
    }

    @Override
    public AggregateEntities deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var entities = new ArrayList<AggregateField>();

        Type typeTest = new TypeToken<HashMap<String, String>>() {
        }.getType();

        HashMap<String, String> parsedJson = gson.fromJson(jsonElement.getAsJsonObject(), typeTest);
        for (String key : parsedJson.keySet()) {
            var currentField = AggregateField.builder()
                .name(key)
                .value(Double.valueOf(parsedJson.get(key)))
                .build();

            entities.add(currentField);
        }

        return AggregateEntities.builder().entities(entities).build();
    }
}