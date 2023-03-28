package solutions.bellatrix.web.components.advanced.grid;

import solutions.bellatrix.web.components.WebComponent;

public interface IReadonlyControlDataHandler<TComponent extends WebComponent> extends IControlDataHandler<TComponent> {
    <TValue> TValue getData(Class<TComponent> elementType, TComponent element);
    void validateValueIs(Class<TComponent> elementType, TComponent element, String expectedValue);
}

