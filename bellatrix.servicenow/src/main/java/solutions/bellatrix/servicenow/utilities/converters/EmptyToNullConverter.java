package solutions.bellatrix.servicenow.utilities.converters;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class EmptyToNullConverter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        TypeAdapters.STRING.write(out, value);
    }

    @Override
    public String read(final JsonReader in) throws IOException {
        var result = TypeAdapters.STRING.read(in);

        if (result == null) {
            return null;
        }

        return result.isEmpty() ? null : result;
    }
}