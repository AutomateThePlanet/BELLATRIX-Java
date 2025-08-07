package solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.servicenow.plugins.fileuploads.models.UploadedFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FileWriterService {
    private final EnvironmentalContext environmentalContext;
    private final FileReaderService fileReaderService;

    public FileWriterService(EnvironmentalContext environmentalContext) {
        this.environmentalContext = environmentalContext;
        fileReaderService = new FileReaderService(environmentalContext);
    }

    @SneakyThrows
    public void writeToFileSystem(UploadedFile fileInfo) {
        var fileContent = generateFileContent(fileInfo);
        var filePath = localPathResolution(new ArrayList<>(List.of(environmentalContext.getExecutingModule(), "src", "main", "resources", "temp", fileInfo.getFileName())));
        Files.write(filePath, fileContent.getBytes());
        var file = new File(String.valueOf(filePath));
        fileInfo.setFile(file);
        if (environmentalContext.getExecutionType().equals("lambdatest")) {
            String apiUrl = "https://api.lambdatest.com/automation/api/v1/user-files";

            Response fileUploadResponse = RestAssured
                .given()
                .header("Authorization", getAuthorizationHeaderValue())
                .contentType("multipart/form-models")
                .multiPart("files", fileInfo.getFile())
                .when()
                .post(apiUrl);

            Log.info("Upload File Request Response Body -> " + fileUploadResponse.getBody().prettyPrint());
        }
    }

    private String getEncodedCredentials() {
        var gridSettings = environmentalContext.getGridSettings();
        var lambatestArguments = gridSettings.getArguments().get(0);
        var accessKey = SecretsResolver.getSecret(lambatestArguments.get("accessKey").toString());
        var username = SecretsResolver.getSecret(lambatestArguments.get("username").toString());
        var formattedCredentials = "%s:%s".formatted(username, accessKey);

        return Base64.getEncoder().encodeToString(formattedCredentials.getBytes());
    }

    private String getAuthorizationHeaderValue() {
        var encodedCredentials = getEncodedCredentials();

        return "Basic " + encodedCredentials;
    }

    private Path localPathResolution(List<String> path) {
        return path.stream()
            .reduce(Paths.get(System.getProperty("user.dir")).getParent(), Path::resolve, Path::resolve);
    }


    @SneakyThrows
    private String generateFileContent(UploadedFile fileInfo) {
        var rawFileWithPlaceholders = fileReaderService.readTemplateFile(fileInfo.getTemplateName());
        var fileBody = "";
        fileBody = rawFileWithPlaceholders.replace("@%", fileInfo.getSoftwareIdentity());
        return fileBody;
    }
}
