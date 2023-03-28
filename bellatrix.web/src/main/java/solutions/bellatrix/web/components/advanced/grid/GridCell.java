package solutions.bellatrix.web.components.advanced.grid;

import lombok.Getter;
import lombok.Setter;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.findstrategies.FindStrategy;

@Getter
@Setter
public class GridCell extends WebComponent implements ComponentText, ComponentHtml {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    private Class<? extends WebComponent> cellControlComponentType;
    private FindStrategy cellControlBy;
    private int column;
    private int row;

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }

    public <T extends WebComponent> T as(Class<T> componentType) throws InstantiationException, IllegalAccessException {
        cellControlComponentType = componentType;

        if (cellControlBy == null) {
            T instance = componentType.newInstance();
            instance.setFindStrategy(getFindStrategy());
            instance.setWrappedElement(getWrappedElement());
            instance.setParentWrappedElement(getParentWrappedElement());
            instance.setElementIndex(getElementIndex());
//            instance.setShouldCacheElement(true);

            return instance;
        } else {
            return create(cellControlBy.getClass(), componentType);
        }
    }

//    public <T extends WebComponent> T as(Class<T> elementClass) {
//        WebElement webElement = as(elementClass).getWrappedElement();
//        return elementClass.cast(webElement);
//    }
}

