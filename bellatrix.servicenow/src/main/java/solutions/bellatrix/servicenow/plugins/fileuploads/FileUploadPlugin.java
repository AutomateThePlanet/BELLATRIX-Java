package solutions.bellatrix.servicenow.plugins.fileuploads;

import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.servicenow.plugins.fileuploads.annotations.ExecutionModule;
import solutions.bellatrix.servicenow.plugins.fileuploads.annotations.FileToBeUploaded;
import solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices.EnvironmentalContext;
import solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices.FileCreatorService;
import solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices.FileDeleterService;
import solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices.FileResolutionService;

import java.io.IOException;
import java.lang.reflect.Method;

public class FileUploadPlugin extends Plugin {
    private EnvironmentalContext executionContextInfo;
    private FileCreatorService fileCreatorService;
    private String executionMethod;

    @Override
    public void preBeforeClass(Class type) {
        var fileModuleSourceAnnotation = getFileModuleSourceAnnotation(type);
        if (fileModuleSourceAnnotation != null) {
            executionContextInfo = new EnvironmentalContext(fileModuleSourceAnnotation.moduleName());
            fileCreatorService = new FileCreatorService(executionContextInfo);
        }
    }

    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) throws IOException {
        var uploadFileAnnotation = getFileToBeUploadedAnnotation(memberInfo);
        if (uploadFileAnnotation != null) {
            var templateFileName = uploadFileAnnotation.templateName();

            var file = fileCreatorService.createFileBasedOnTemplate(templateFileName);
            executionMethod = memberInfo.getName();
            file.setExecutionMethod(executionMethod);

            FileResolutionService.addFileFromCurrentContext(file);
        }
    }

    @Override
    public void postAfterTest(TestResult testResult, Method memberInfo, Throwable failedTestException) {
        var uploadFileAnnotation = getFileToBeUploadedAnnotation(memberInfo);
        if (uploadFileAnnotation != null) {
            var deleteService = new FileDeleterService(executionContextInfo);
            var file = FileResolutionService.getFileFromCurrentContext(executionMethod);
            deleteService.deleteFileFromFileSystem(file);
            FileResolutionService.removeFileFromResolutionList(file);
        }
    }

    private FileToBeUploaded getFileToBeUploadedAnnotation(Method memberInfo) {
        return memberInfo.getDeclaredAnnotation(FileToBeUploaded.class);
    }

    private ExecutionModule getFileModuleSourceAnnotation(Class type) {
        return (ExecutionModule) type.getDeclaredAnnotation(ExecutionModule.class);
    }
}