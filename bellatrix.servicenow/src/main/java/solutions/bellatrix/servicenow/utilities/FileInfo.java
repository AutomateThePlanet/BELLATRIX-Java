package solutions.bellatrix.servicenow.utilities;

import lombok.Data;

@Data
public class FileInfo {
    private String contentType;
    private String fileExtension;
    private String fileName;
    private String uploadPath;

    public static FileInfo createPngFile() {
        var fileInfo = new FileInfo();
        fileInfo.setFileName("FileName");
        fileInfo.setFileExtension("png");
        fileInfo.setContentType("image/png");
        fileInfo.setUploadPath("$s.$s".formatted(fileInfo.getFileName(), fileInfo.getFileExtension()));
        return fileInfo;
    }
}