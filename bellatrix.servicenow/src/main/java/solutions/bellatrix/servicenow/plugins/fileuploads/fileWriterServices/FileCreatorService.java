package solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices;

import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.TimestampBuilder;
import solutions.bellatrix.servicenow.plugins.fileuploads.models.UploadedFile;

import java.io.File;

public class FileCreatorService {
    private final FileWriterService fileWriterService;
    private final FileReaderService fileReaderService;
    private final EnvironmentalContext environmentalContext;

    public FileCreatorService(EnvironmentalContext environmentalContext) {
        this.environmentalContext = environmentalContext;
        fileWriterService = new FileWriterService(this.environmentalContext);
        fileReaderService = new FileReaderService(this.environmentalContext);
    }


    @SneakyThrows
    public UploadedFile createFileBasedOnTemplate(String templateName) {
        UploadedFile fileInfo = UploadedFile.builder().build();
        var softwareIdentity = TimestampBuilder.buildUniqueTextByPrefix("ATP_Details");
        fileInfo.setTemplateName(templateName);
        fileInfo.setFileName();
        fileInfo.setSoftwareIdentity(softwareIdentity);
        fileWriterService.writeToFileSystem(fileInfo);
        fileReaderService.updateNewlyCreatedFilePaths(fileInfo);

        return fileInfo;
    }

    private File createFileObject(String fileName) {
        return new File(fileName);
    }
}
