package solutions.bellatrix.web.core.utilities;

import java.nio.file.Paths;

public class UserHomePathNormalizer {
    public static String normalizePath(String path) {
        if (path.startsWith("user.home")) {
            return Paths.get(getUserHomePath(), path).toString();
        }

        return path;
    }

    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
