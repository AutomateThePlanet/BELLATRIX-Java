import com.google.gson.FieldNamingPolicy;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

public class ArtistsConverter extends JsonConverter {
    public ArtistsConverter() {
        super(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        });
    }
}