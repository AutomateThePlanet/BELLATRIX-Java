package O25_openCv_image_actions.data.enums;

import O25_openCv_image_actions.data.ImageInBase64;
import solutions.bellatrix.web.infrastructure.Base64Encodable;

public enum EncodedImageDemo implements Base64Encodable {
    FALCON_9("falcon9", ImageInBase64.falcon9, 55, 70),
    FALCON_9_BACK_BUTTON("falcon9BackButton", ImageInBase64.falcon9BackButton, 20, 5),
    COMMENT_TEXT_AREA("commentTextArea", ImageInBase64.commentTextArea, 30, 50);

    private final String imageName;
    private final String encodedImage;
    private final int xOffset;
    private final int yOffset;

    EncodedImageDemo(String imageName, String encodedImage, int xOffset, int yOffset) {
        this.imageName = imageName;
        this.encodedImage = encodedImage;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public String getBase64Image() {
        return encodedImage;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public int getYOffset() {
        return yOffset;
    }
}
