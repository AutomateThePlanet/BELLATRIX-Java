package solutions.bellatrix.servicenow.utilities.converters.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Objects;

public class StringTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        TypeAdapters.STRING.write(out, value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        var result = TypeAdapters.STRING.read(in);

        if (Objects.isNull(result) || result.isEmpty()) {
            return null;
        }

        return result;
    }
}