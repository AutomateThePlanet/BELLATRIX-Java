package solutions.bellatrix.servicenow.plugins.fileuploads.fileWriterServices;

import lombok.Getter;
import solutions.bellatrix.core.configuration.ConfigurationService;
import solutions.bellatrix.web.configuration.GridSettings;
import solutions.bellatrix.web.configuration.WebSettings;

@Getter
public class EnvironmentalContext {
    private final WebSettings webSettings = ConfigurationService.get(WebSettings.class);

    public EnvironmentalContext(String fileModuleSourceName) {
        this.executingModule = fileModuleSourceName;
        gridSettings = webSettings.getGridSettings().get(0);
        executionType = webSettings.getExecutionType();
    }

    private final GridSettings gridSettings;
    private final String executingModule;
    private final String executionType;
}
