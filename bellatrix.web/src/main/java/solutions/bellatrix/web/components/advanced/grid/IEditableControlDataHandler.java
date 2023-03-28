package solutions.bellatrix.web.components.advanced.grid;

import solutions.bellatrix.web.components.WebComponent;

public interface IEditableControlDataHandler<TComponent extends WebComponent> extends IReadonlyControlDataHandler<TComponent> {
    void setData(Class<TComponent> elementType, TComponent element, String data);
}

