package solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices;

import lombok.SneakyThrows;
import solutions.bellatrix.servicenow.plugins.fileuploads.models.UploadedFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReaderService {
    private final EnvironmentalContext environmentalContext;

    public FileReaderService(EnvironmentalContext environmentalContext) {
        this.environmentalContext = environmentalContext;
    }

    @SneakyThrows
    public String readTemplateFile(String... args) {
        List<String> pathComponents = new ArrayList<>(List.of(environmentalContext.getExecutingModule(), "target", "classes", "attachments"));
        pathComponents.addAll(List.of(args));
        Path filePath = localPathResolution(pathComponents);

        return new String(Files.readAllBytes(filePath));
    }

    public void updateNewlyCreatedFilePaths(UploadedFile fileInfo) {
        List<String> pathComponents = new ArrayList<>(List.of(environmentalContext.getExecutingModule(), "src", "main", "resources", "temp", fileInfo.getFileName()));
        var localPath = localPathResolution(pathComponents);
        fileInfo.setLocalPath(localPath);
        fileInfo.setFilePath(localPath);
        if (environmentalContext.getExecutionType().equalsIgnoreCase("lambdatest")) {
            fileInfo.setFilePath(Path.of("C:\\Users\\ltuser\\Downloads\\", fileInfo.getFileName()));
        }
    }

    private Path localPathResolution(List<String> path) {
        return path.stream().reduce(Paths.get(System.getProperty("user.dir")).getParent(), Path::resolve, Path::resolve);
    }
}
