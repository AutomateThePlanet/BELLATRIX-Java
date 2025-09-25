import org.junit.jupiter.api.Test;
import solutions.bellatrix.data.configuration.RepositoryProvider;

public class BaseTest {

    @Test
    public void getAllResources_when_sendGetAlRequest() {
        RepositoryProvider.INSTANCE.register(Artist.class, ArtistRepository.class);
        var artistRepository = new ArtistRepository();

        Artist artist = Artist.builder().name("James Clavell").build().create();
        artist.getResponse().getNativeResponse();
    }
}