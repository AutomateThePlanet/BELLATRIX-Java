package solutions.bellatrix.core.utilities;

import java.nio.file.Paths;

public class UserHomePathNormalizer {
    public static String normalizePath(String path) {
        if (path.startsWith("user.home")) {
            return Paths.get(getUserHomePath(), path.substring(9)).toString();
        }

        return path;
    }

    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
