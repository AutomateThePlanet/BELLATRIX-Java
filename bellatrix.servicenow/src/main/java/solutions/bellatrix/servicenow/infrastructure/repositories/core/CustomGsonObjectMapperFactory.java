package solutions.bellatrix.servicenow.infrastructure.repositories.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;

import java.lang.reflect.Type;

public class CustomGsonObjectMapperFactory implements GsonObjectMapperFactory {
    @Override
    public Gson create(Type type, String s) {
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateConverter());
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter());

        return gsonBuilder.setPrettyPrinting().create();
    }
}