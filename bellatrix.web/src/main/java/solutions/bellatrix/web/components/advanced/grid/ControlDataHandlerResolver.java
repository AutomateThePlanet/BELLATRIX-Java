package solutions.bellatrix.web.components.advanced.grid;

import solutions.bellatrix.web.components.WebComponent;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class ControlDataHandlerResolver {
    private static final Map<Class<? extends WebComponent>, IReadonlyControlDataHandler<?>> readonlyHandlers = new HashMap<>();
    private static final Map<Class<? extends WebComponent>, IEditableControlDataHandler<?>> editableHandlers = new HashMap<>();

    public static <T extends WebComponent> void registerReadonlyHandler(Class<T> componentType, IReadonlyControlDataHandler<T> handler) {
        readonlyHandlers.put(componentType, handler);
    }

    public static <T extends WebComponent> void registerEditableHandler(Class<T> componentType, IEditableControlDataHandler<T> handler) {
        editableHandlers.put(componentType, handler);
    }

    public static <T extends WebComponent> IReadonlyControlDataHandler<T> resolveReadonlyHandler(Class<T> componentType) {
        IReadonlyControlDataHandler<?> handler = readonlyHandlers.get(componentType);
        if (handler == null) {
            handler = resolveEditableHandler(componentType);
        }
        return (IReadonlyControlDataHandler<T>) handler;
    }

    public static <T extends WebComponent> IEditableControlDataHandler<T> resolveEditableHandler(Class<T> componentType) {
        return (IEditableControlDataHandler<T>) editableHandlers.get(componentType);
    }
}

