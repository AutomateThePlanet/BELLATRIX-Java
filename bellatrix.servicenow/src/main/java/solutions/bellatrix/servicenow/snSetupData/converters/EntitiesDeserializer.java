package solutions.bellatrix.servicenow.snSetupData.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.ApiEntity;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.Entities;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TypeAdapterFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EntitiesDeserializer<TEntity extends ApiEntity> implements JsonDeserializer<Entities<TEntity>> {
    private final Class<TEntity> entityClass;
    private final Gson gson;

    public EntitiesDeserializer(Class<TEntity> entityClass) {
        this.entityClass = entityClass;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.setLenient();
        TypeAdapterFactory.allExceptEntities().forEach(gsonBuilder::registerTypeAdapter);
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    @Override
    public Entities<TEntity> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var result = new Entities<TEntity>();
        result.setEntities(new ArrayList<>());

        var entitiesNode = jsonElement.getAsJsonObject().getAsJsonArray("result");
        if (entitiesNode != null) {
            for (var currentEntry : entitiesNode) {
                var currentEntity = gson.fromJson(currentEntry, entityClass);
                result.getEntities().add(currentEntity);
            }
        }

        return result;
    }
}