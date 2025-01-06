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
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

public class OpenCvService {
    /**
     * Convert the base 64 string to an image file
     * @return the path of the created image file
     */
    private static String getImagePathByBase64(Base64Encodable encodedImage) {
        String[] imageProperties = encodedImage.getBase64Image().split(",");
        String extension = switch (imageProperties[0]) {
            case "data:image/jpeg;base64" -> "jpeg";
            case "data:image/png;base64" -> "png";
            default -> "jpg";
        };

        byte[] data = DatatypeConverter.parseBase64Binary(imageProperties[1]);
        String path;
        Path rootPath = Paths.get(System.getProperty("user.dir"));
        Path fullFilePath = Paths.get(String.valueOf(rootPath), "target", "classes", encodedImage.getImageName() + "." + extension);
        path = fullFilePath.toFile().getPath();
        File file = new File(path);

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    private static BufferedImage getImageWidthHeight(String filePath) throws IOException {
        BufferedImage bimg = ImageIO.read(new File(filePath));

        return bimg;
    }

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
        BufferedImage bufferedImage;
        String templatePath = getImagePathByBase64(encodedImage);

        byte[] decodedBytes = Objects.requireNonNull(SingletonFactory.getInstance(ScreenshotPlugin.class)).takeScreenshot();
        OpenCV.loadLocally();

        // Load images
        Mat template = Imgcodecs.imread(templatePath);
        Mat template_grayscale = new Mat();
        Imgproc.cvtColor(template, template_grayscale, Imgproc.COLOR_BGR2GRAY);
        if (shouldGrayScale) {
            template = template_grayscale;
        }

        Mat mat = new Mat(1, decodedBytes.length, CvType.CV_8U);
        mat.put(0, 0, decodedBytes);
        Mat source = Imgcodecs.imdecode(mat, Imgcodecs.IMREAD_COLOR);

        Mat sourceGrayscale = new Mat();
        Imgproc.cvtColor(source, sourceGrayscale, Imgproc.COLOR_BGR2GRAY);
        if (shouldGrayScale) {
            source = sourceGrayscale;
        }

        Mat result = new Mat();
        int result_cols = source.cols() - template.cols() + 1;
        int result_rows = source.rows() - template.rows() + 1;
        result.create(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(source, template, result, Imgproc.TM_CCOEFF_NORMED);

        // Localize the best match
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.maxLoc;

        if (encodedImage.getXOffset() == 0 && encodedImage.getYOffset() == 0) {
            try {
                bufferedImage = getImageWidthHeight(templatePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            double[] imageCenterCoordinates = {matchLoc.x / getJavaMonitorScaling() + (double)(bufferedImage.getWidth() / 2), matchLoc.y / getJavaMonitorScaling() + (double)(bufferedImage.getHeight() / 2)};
            matchLoc.set(imageCenterCoordinates);
        }

        return matchLoc;
    }
}