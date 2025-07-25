package solutions.bellatrix.servicenow.snSetupData.enums;

import lombok.Getter;

@Getter
public enum AttachmentFile {
    UNSUPPORTED_FILE_TYPE("unsupportedImage.tiff", "image/tiff", "UnsupportedImage", "tiff"),
    PDF("DeviceAttachmentAuto.pdf", "application/pdf", "DeviceAttachmentAuto", "pdf"),
    PNG("DeviceAttachmentAuto.png", "image/png", "DeviceAttachmentAuto", "png"),
    JPG("DeviceAttachmentAuto.jpg", "image/jpeg", "DeviceAttachmentAuto", "jpg"),
    OVERSIZE_ATTACHMENT("OversizeAttachment.jpg", "image/jpeg", "DeviceAttachmentAuto", "jpg"),
    XLS("DeviceAttachmentAuto.xls", "application/vnd.ms-excel", "DeviceAttachmentAuto", "xls"),
    XLSX("DeviceAttachmentAuto.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "DeviceAttachmentAuto", "xlsx"),
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