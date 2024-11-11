package solutions.bellatrix.web.findstrategies;

import lombok.Getter;
import org.openqa.selenium.By;
import solutions.bellatrix.web.infrastructure.ByImageBase64;
import solutions.bellatrix.web.infrastructure.Base64Encodable;

@Getter
public class ImageBase64FindStrategy extends FindStrategy {
    private final Base64Encodable encodedImage;

    public ImageBase64FindStrategy(Base64Encodable encodedImage) {
        super(encodedImage.getBase64Image());
        this.encodedImage = encodedImage;
    }

    @Override
    public By convert() {
        return new ByImageBase64(encodedImage.getBase64Image());
    }

    @Override
    public String toString() {
        return String.format("image base 64: %s", encodedImage.getBase64Image());
    }
}