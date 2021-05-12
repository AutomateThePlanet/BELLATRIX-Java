package solutions.bellatrix.core.utilities;

import java.nio.file.Paths;

public class UserHomePathNormalizer {
    public static String normalizePath(String path) {
        if (RuntimeInformation.IS_MAC || RuntimeInformation.IS_UNIX) {
            path = path.replace('\\', '/');
        } else if (RuntimeInformation.IS_WINDOWS) {
            path = path.replace('/', '\\');
        }

        if (path.startsWith("user.home")) {
            path = Paths.get(getUserHomePath(), path.substring(9)).toString();
        }

        return path;
    }

    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }
}
