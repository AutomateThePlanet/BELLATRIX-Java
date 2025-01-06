package solutions.bellatrix.web.findstrategies;

import lombok.Getter;
import org.openqa.selenium.By;
import solutions.bellatrix.plugins.opencv.Base64Encodable;
import solutions.bellatrix.web.infrastructure.ByImageBase64;

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