package opencv.data;

import solutions.bellatrix.plugins.opencv.Base64Encodable;

public enum EncodedImageDemo implements Base64Encodable {
    /**
     * <img src={@value ImageInBase64#falcon9}/>
     */
    FALCON_9("falcon9", ImageInBase64.falcon9, 50, 25),
    /**
     * <img src={@value ImageInBase64#searchInput}/>
     */
    SEARCH_INPUT("searchInput", ImageInBase64.searchInput),
    /**
     * <img src={@value ImageInBase64#falconResults}/>
     */
    FALCON_RESULTS("falconResults", ImageInBase64.falconResults),
    /**
     * <img src={@value ImageInBase64#falcon9BackButton}/>
     */
    FALCON_9_BACK_BUTTON("falcon9BackButton", ImageInBase64.falcon9BackButton),
    /**
     * <img src={@value ImageInBase64#commentTextArea}/>
     */
    COMMENT_TEXT_AREA("commentTextArea", ImageInBase64.commentTextArea);

    private final String imageName;
    private final String encodedImage;
    private int xOffset = 0;
    private int yOffset = 0;

    EncodedImageDemo(String imageName, String encodedImage, int xOffset, int yOffset) {
        this.imageName = imageName;
        this.encodedImage = encodedImage;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    EncodedImageDemo(String imageName, String encodedImage) {
        this.imageName = imageName;
        this.encodedImage = encodedImage;
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
