package solutions.bellatrix.servicenow.plugins.fileuploads.models;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Data
@Builder
public class UploadedFile {
    private String softwareIdentity;
    private String executionMethod;
    private String templateName;
    private String fileContent;
    private String fileName;
    /**
     * The path to the file
     */
    private Path filePath;
    /**
     * Do not use this field directly. Use {@link #getFilePath()} instead in the tests
     *
     */
    private Path localPath;
    private File file;

    public void setFileName() {
        var extractedTemplateName = templateName.substring(0, templateName.lastIndexOf("."));
        fileName = "%s-%s.xml".formatted(extractedTemplateName, UUID.randomUUID());
    }
}
