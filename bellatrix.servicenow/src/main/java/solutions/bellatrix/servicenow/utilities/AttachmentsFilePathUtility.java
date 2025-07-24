package solutions.bellatrix.servicenow.utilities;

import solutions.bellatrix.servicenow.snSetupData.enums.AttachmentFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.WebSettings;

public class AttachmentsFilePathUtility {
    public static String getAttachmentsFilePath(AttachmentFile attachmentFile) {
        var webSettings = ConfigurationService.get(WebSettings.class);
        var filePathAfterAttachments = attachmentFile.getValue();
        String filePath;
        if (webSettings.getExecutionType().equals("lambdatest")) {
            var pathParts = filePathAfterAttachments.split("\\\\");
            var fileName = pathParts[pathParts.length - 1];
            filePath = "C:\\Users\\ltuser\\Downloads\\" + fileName;
        } else {
            Path rootPath = Paths.get(System.getProperty("user.dir")).getParent();
            Path fullFilePath = Paths.get(String.valueOf(rootPath), "system-tests-customizations", "src", "main", "java", "attachments", filePathAfterAttachments);
            filePath = fullFilePath.toFile().getPath();
        }

        return filePath;
    }
}