package solutions.bellatrix.servicenow.plugins.fileuploads;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

@Deprecated(forRemoval = true)
public final class FileUploadService {
    private final static Map<String, String> FILE_PATHS;
    private final static Map<String, String> VALUE_TO_REPLACE;

    static {
        FILE_PATHS = new HashedMap();
        VALUE_TO_REPLACE = new HashedMap();
    }

    public static void addFilePath(String templateName, String filePath, String value) {
        if (!FILE_PATHS.containsKey(templateName)) {
            FILE_PATHS.put(templateName, filePath);
            if (!VALUE_TO_REPLACE.containsKey(value)) {
                VALUE_TO_REPLACE.put(templateName, value);
            }
        }
    }

    public static void removeFilePath(String templateName) {
        FILE_PATHS.remove(templateName);
    }

    public static String getFilePath(String templateName) {
        return FILE_PATHS.get(templateName);
    }

    public static String getFileName(String templateName) {
        var filePath = FILE_PATHS.get(templateName);
        var pathParts = filePath.toString().split("\\\\");
        return pathParts[pathParts.length - 1];
    }

    public static String getReplacedValue(String templateName) {
        return VALUE_TO_REPLACE.get(templateName);
    }
}