package solutions.bellatrix.servicenow.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtilities {
    public static String getFileAsString(String... args) throws IOException {
        List<String> pathComponents = new ArrayList<>(List.of("ot-security-web-system-tests", "src", "main", "resources", "attachments"));
        pathComponents.addAll(List.of(args));
        Path filePath = pathComponents.stream()
                .reduce(Paths.get(System.getProperty("user.dir")).getParent(), Path::resolve, Path::resolve);

        return new String(Files.readAllBytes(filePath));
    }
}