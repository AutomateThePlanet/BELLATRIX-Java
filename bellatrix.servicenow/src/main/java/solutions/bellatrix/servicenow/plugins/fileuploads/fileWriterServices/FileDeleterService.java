package solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.core.utilities.SecretsResolver;
import solutions.bellatrix.servicenow.plugins.fileuploads.models.LambdaTestRequestFileModel;
import solutions.bellatrix.servicenow.plugins.fileuploads.models.TemplateFileNames;
import solutions.bellatrix.servicenow.plugins.fileuploads.models.UploadedFile;

import java.nio.file.Files;
import java.util.Base64;

public class FileDeleterService {
    private final EnvironmentalContext environmentalContext;

    public FileDeleterService(EnvironmentalContext environmentalContext) {
        this.environmentalContext = environmentalContext;
    }

    @SneakyThrows
    public void deleteFileFromFileSystem(UploadedFile fileInfo) {
        if (!fileInfo.getFileName().equals(TemplateFileNames.FILE_XML)) {
            Files.delete(fileInfo.getLocalPath());
            Log.info("File with name: %s successfully deleted from file system.".formatted(fileInfo.getFileName()));
            if (environmentalContext.getExecutionType().equals("lambdatest")) {
                String apiUrl = "https://api.lambdatest.com/automation/api/v1/user-files/delete";
                var fileToDelete = new LambdaTestRequestFileModel();
                fileToDelete.key = fileInfo.getFileName();
                Response deleteFileResponse = RestAssured
                    .given()
                    .body(fileToDelete)
                    .header("accept", "application/json")
                    .header("Authorization", getAuthorizationHeaderValue())
                    .when()
                    .delete(apiUrl);

                Log.info("Delete File Request Response-> " + deleteFileResponse.getBody().prettyPrint());
            }
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
}
