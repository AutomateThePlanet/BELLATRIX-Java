package solutions.bellatrix.web.components.advanced.grid;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import solutions.bellatrix.core.plugins.EventListener;
import solutions.bellatrix.core.utilities.InstanceFactory;
import solutions.bellatrix.web.components.ComponentActionEventArgs;
import solutions.bellatrix.web.components.WebComponent;
import solutions.bellatrix.web.components.advanced.common.Cell;
import solutions.bellatrix.web.components.advanced.table.TableCell;
import solutions.bellatrix.web.components.contracts.ComponentHtml;
import solutions.bellatrix.web.components.contracts.ComponentText;
import solutions.bellatrix.web.findstrategies.FindStrategy;

@Getter @Setter
@SuppressWarnings("rawtypes")
public class GridCell extends Cell<Grid> implements ComponentHtml, ComponentText {
    public final static EventListener<ComponentActionEventArgs> CLICKING = new EventListener<>();
    public final static EventListener<ComponentActionEventArgs> CLICKED = new EventListener<>();

    public void click() {
        defaultClick(CLICKING, CLICKED);
    }

    private Class cellControlComponentType;
    private FindStrategy cellControlFindStrategy;

    @Override
    public <TComponent extends WebComponent> TComponent as(Class<TComponent> clazz) {
        cellControlComponentType = clazz;
        if (cellControlFindStrategy == null) {
            return super.as(clazz);
        } else {
            return this.create(clazz, cellControlFindStrategy);
        }
    }

    @Override
    public String getText() {
        return defaultGetText();
    }

    @Override
    public String getHtml() {
        return defaultGetInnerHtmlAttribute();
    }
}
