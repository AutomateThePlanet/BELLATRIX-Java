package solutions.bellatrix.playwright.components.advanced.grid;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.playwright.components.WebComponent;
import solutions.bellatrix.playwright.components.advanced.HeaderInfo;
import solutions.bellatrix.playwright.findstrategies.FindStrategy;

@Getter @Setter
public class ControlColumnData extends HeaderInfo {
    private FindStrategy findStrategy;
    private Class<?> componentClass;

    ControlColumnData(String headerName) {
        super(headerName);
    }
    ControlColumnData(String headerName, Integer order) {
        super(headerName, order);
    }

    <TComponent extends WebComponent> ControlColumnData(String headerName, FindStrategy by, Class<TComponent> componentClazz) {
        super(headerName, null);
        findStrategy = by;
        componentClass = componentClazz;
    }

    <TComponent extends WebComponent, TFindStrategy extends FindStrategy> ControlColumnData(String headerName, TFindStrategy by, Class<TComponent> componentClazz, int order) {
        super(headerName, order);
        findStrategy = by;
        componentClass = componentClazz;
    }
}
