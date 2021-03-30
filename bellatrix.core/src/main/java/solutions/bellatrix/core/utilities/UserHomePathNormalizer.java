package solutions.bellatrix.core.utilities;

import java.nio.file.Paths;

public class UserHomePathNormalizer {
    public static String normalizePath(String path) {
        if (path.startsWith("user.home")) {
            path = Paths.get(getUserHomePath(), path.substring(9)).toString();
        }

        if (RuntimeInformation.IS_MAC && RuntimeInformation.IS_UNIX) {
            path = path.replace('\\', '/');
        } else if (RuntimeInformation.IS_WINDOWS) {
            path = path.replace('/', '\\');
        }

        return path;
    }

    public static String getUserHomePath() {
        String userHomePath = System.getProperty("user.home");
        if (userHomePath.endsWith("/") || userHomePath.endsWith("\\")) {
            userHomePath = userHomePath.substring(0, userHomePath.length() - 1);
        }
        return userHomePath;
    }
}
