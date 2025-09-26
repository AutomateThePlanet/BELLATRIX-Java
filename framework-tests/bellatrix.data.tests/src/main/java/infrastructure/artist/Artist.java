package infrastructure.artist;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Artist extends HttpEntity<String, Artist> {
    @SerializedName("ArtistId")
    private String id;

    @SerializedName("Name")
    private String name;

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id;
    }
}