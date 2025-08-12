package solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices;

import solutions.bellatrix.servicenow.plugins.fileuploads.models.UploadedFile;

import java.util.concurrent.ConcurrentHashMap;

public class FileResolutionService {
    public static ThreadLocal<ConcurrentHashMap<String, UploadedFile>> resolutionList;

    static {
        resolutionList = ThreadLocal.withInitial(ConcurrentHashMap::new);
    }

    public static void addFileFromCurrentContext(UploadedFile fileInfo) {
        ConcurrentHashMap<String, UploadedFile> map = resolutionList.get();
        map.putIfAbsent(fileInfo.getExecutionMethod(), fileInfo);
    }

    public static UploadedFile getFileFromCurrentContext(String key) {
        return resolutionList.get().get(key);
    }

    public static void removeFileFromResolutionList(UploadedFile fileInfo) {
        resolutionList.get().remove(fileInfo.getExecutionMethod());
    }
}
