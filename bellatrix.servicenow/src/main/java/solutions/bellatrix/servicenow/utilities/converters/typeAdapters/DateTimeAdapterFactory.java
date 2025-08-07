package solutions.bellatrix.servicenow.utilities.converters.typeAdapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == LocalDateTime.class) {
            return (TypeAdapter<T>) new LocalDateTimeTypeAdapter();
        } else if (type.getRawType() == LocalDate.class) {
            return (TypeAdapter<T>) new LocalDateTypeAdapter();
        }

        return null;
    }
}