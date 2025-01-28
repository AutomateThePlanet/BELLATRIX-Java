package solutions.bellatrix.plugins.opencv;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import plugins.screenshots.ScreenshotPlugin;
import solutions.bellatrix.core.utilities.SingletonFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class OpenCvService {
    /**
     * @return the current scaling factor (e.g., 1.0 for 100%, 1.25 for 125%)
     */
    public static double getJavaMonitorScaling() {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        // Get the default transform
        AffineTransform transform = gc.getDefaultTransform();

        // The scaling factor is typically the same for both X and Y
        double scaleX = transform.getScaleX();
        double scaleY = transform.getScaleY();

        // Return X scale (usually same as Y scale)
        return scaleX;
    }

    /**
     * @return the coordinates of the image found on the screen
     */
    public static Point getLocation(Base64Encodable encodedImage, boolean shouldGrayScale) {
        var screenshotPlugin = SingletonFactory.getInstance(ScreenshotPlugin.class);

        if (screenshotPlugin == null) {
            throw new IllegalArgumentException("It seems that the screenshot plugin isn't registered by the 'ScreenshotPlugin.class' key inside SingletonFactory's map or isn't registered at all!\n" +
                    "Check the BaseTest class of your project where the plugins are registered. Register the specific screenshot plugin implementation as the base ScreenshotPlugin.class.\n" +
                    "for example: addPluginAs(ScreenshotPlugin.class, WebScreenshotPlugin.class);");
        }

        var screenshot = screenshotPlugin.takeScreenshot();


        OpenCV.loadLocally();

        Mat result = loadImages(encodedImage, screenshot, shouldGrayScale);

        return getMatchLocation(encodedImage, result);
    }

    private static Point getMatchLocation(Base64Encodable encodedImage, Mat result) {
        BufferedImage bufferedImage;

        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.maxLoc;

        if (encodedImage.getXOffset() == 0 && encodedImage.getYOffset() == 0) {
            try {
                bufferedImage = getImageWidthHeight(encodedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            double[] imageCenterCoordinates = {matchLoc.x / getJavaMonitorScaling() + (double)(bufferedImage.getWidth() / 2), matchLoc.y / getJavaMonitorScaling() + (double)(bufferedImage.getHeight() / 2)};
            matchLoc.set(imageCenterCoordinates);
        }

        return matchLoc;
    }

    private static BufferedImage getImageWidthHeight(Base64Encodable encodedImage) throws IOException {
        String cleanBase64 = removePrefixFromBase64(encodedImage);

        byte[] decodedBytes = Base64.getDecoder().decode(cleanBase64);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        BufferedImage bimg = ImageIO.read(bis);

        return bimg;
    }

    private static Mat loadImages(Base64Encodable encodedImage, byte[] screenshot, boolean shouldGrayScale) {
        Mat template = getMatrixFromBase64(encodedImage);
        if (shouldGrayScale) {
            changeToGrayscale(template);
        }

        Mat source = getMatrixFromBinaryData(screenshot);
        if (shouldGrayScale) {
            changeToGrayscale(source);
        }

        Mat result = createResultMatrix(source, template);

        Imgproc.matchTemplate(source, template, result, Imgproc.TM_CCOEFF_NORMED);

        return result;
    }

    private static Mat changeToGrayscale(Mat template) {
        Mat templateGrayscale = new Mat();
        Imgproc.cvtColor(template, templateGrayscale, Imgproc.COLOR_BGR2GRAY);

        template = templateGrayscale;
        return template;
    }

    private static Mat getMatrixFromBase64(Base64Encodable encodedImage) {
        String cleanBase64 = removePrefixFromBase64(encodedImage);

        byte[] decodedBytes = Base64.getDecoder().decode(cleanBase64);
        return getMatrixFromBinaryData(decodedBytes);
    }

    private static String removePrefixFromBase64(Base64Encodable encodedImage) {
        String base64Image = encodedImage.getBase64Image();
        return base64Image.replaceFirst("^data:.+?;base64,", "");
    }

    private static Mat getMatrixFromBinaryData(byte[] decodedBytes) {
        Mat mat = new Mat(1, decodedBytes.length, CvType.CV_8U);
        mat.put(0, 0, decodedBytes);

        return Imgcodecs.imdecode(mat, Imgcodecs.IMREAD_COLOR);
    }

    private static Mat createResultMatrix(Mat source, Mat template) {
        Mat result = new Mat();
        int result_cols = source.cols() - template.cols() + 1;
        int result_rows = source.rows() - template.rows() + 1;
        result.create(result_rows, result_cols, CvType.CV_32FC1);

        return result;
    }
}