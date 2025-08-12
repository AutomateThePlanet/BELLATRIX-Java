package solutions.bellatrix.servicenow.utilities.converters;

import solutions.bellatrix.servicenow.utilities.converters.typeAdapters.DateTimeAdapterFactory;
import solutions.bellatrix.servicenow.utilities.converters.typeAdapters.StringTypeAdapter;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

import java.util.List;

public class TableApiConverter extends JsonConverter {
    public TableApiConverter() {
        super(gson -> {
            gson.registerTypeAdapter(String.class, new StringTypeAdapter());
            gson.registerTypeAdapterFactory(new DateTimeAdapterFactory());
        });
    }

    @Override
    public <T> List<T> fromStringToList(String json, Class<T> type) {
        return super.fromStringToList(json, type);
    }

    @Override
    public <T> T fromString(String data, Class<T> type) {
        return super.fromString(data, type);
    }
}