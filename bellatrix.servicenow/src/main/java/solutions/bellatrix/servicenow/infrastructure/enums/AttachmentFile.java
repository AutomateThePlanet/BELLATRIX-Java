package solutions.bellatrix.servicenow.infrastructure.enums;

import lombok.Getter;

@Getter
public enum AttachmentFile {
    UNSUPPORTED_FILE_TYPE("unsupportedImage.tiff", "image/tiff", "UnsupportedImage", "tiff"),
    PDF("Name", "application/pdf", "Name", "pdf"),
    PNG("Name.png", "image/png", "Name", "png"),
    JPG("Name.jpg", "image/jpeg", "Name", "jpg"),
    XLS("Name.xls", "application/vnd.ms-excel", "Name", "xls"),
    XLSX("Name.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Name", "xlsx"),
    VALID_SWID_DOCUMENT_XML("ValidSWIDDocument.xml", "xml", "ValidSWIDDocument", "xml");

    private final String value;
    private final String type;
    private final String name;
    private final String extension;

    AttachmentFile(String label, String fileType, String name, String extension) {
        this.value = label;
        this.type = fileType;
        this.name = name;
        this.extension = extension;
    }
}