package solutions.bellatrix.servicenow.snSetupData.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import solutions.bellatrix.servicenow.infrastructure.repositories.core.TypeAdapterFactory;

public class EntitiesSerializer {
    private final Gson gson;

    public EntitiesSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.setLenient();
        TypeAdapterFactory.allExceptEntities().forEach(gsonBuilder::registerTypeAdapter);
        gson = gsonBuilder.create();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }
}