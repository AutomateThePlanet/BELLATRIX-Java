import com.google.gson.FieldNamingPolicy;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.data.configuration.DataSettings;
import solutions.bellatrix.data.http.httpContext.HttpContext;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;
import solutions.bellatrix.data.http.infrastructure.JsonConverter;

public class ArtistRepository extends HttpRepository<Artist> {
    public ArtistRepository() {
        super(Artist.class, new JsonConverter(builder -> {
            builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        }), () -> {
            var httpSettings = ConfigurationService.get(DataSettings.class).getHttpSettings();
            var httpContext = new HttpContext(httpSettings);
            httpContext.addPathParameter("artists");
            return httpContext;
        });
    }

}