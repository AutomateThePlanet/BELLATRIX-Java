package solutions.bellatrix.web.infrastructure;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmptyObjectTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.BEGIN_ARRAY) {
                    JsonArray arr = new JsonParser().parse(in).getAsJsonArray();
                    if (arr.size() == 0) {
                        if (isCollectionType(type.getType())) {
                            return delegate.fromJsonTree(arr);
                        } else {
                            return delegate.fromJsonTree(new JsonObject());
                        }
                    }
                    return delegate.fromJsonTree(arr);
                }
                return delegate.read(in);
            }
        };
    }

    private boolean isCollectionType(Type type) {
        if (type instanceof Class<?>) {
            return Collection.class.isAssignableFrom((Class<?>)type);
        } else if (type instanceof ParameterizedType) {
            return isCollectionType(((ParameterizedType)type).getRawType());
        }
        return false;
    }
}