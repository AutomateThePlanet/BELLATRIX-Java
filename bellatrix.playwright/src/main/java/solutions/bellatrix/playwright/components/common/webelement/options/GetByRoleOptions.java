package solutions.bellatrix.playwright.components.common.webelement.options;

import java.util.regex.Pattern;

/**
 * Absolute copy of the GetByRoleOptions of Locator and FrameLocator.
 * <br>
 * The idea is to combine them under one class and access them more easily.
 */
public class GetByRoleOptions implements Options {
    public Boolean checked;
    public Boolean disabled;
    public Boolean exact;
    public Boolean expanded;
    public Boolean includeHidden;
    public Integer level;
    public Object name;
    public Boolean pressed;
    public Boolean selected;

    public GetByRoleOptions() {
    }

    public GetByRoleOptions setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public GetByRoleOptions setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public GetByRoleOptions setExact(boolean exact) {
        this.exact = exact;
        return this;
    }

    public GetByRoleOptions setExpanded(boolean expanded) {
        this.expanded = expanded;
        return this;
    }

    public GetByRoleOptions setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
        return this;
    }

    public GetByRoleOptions setLevel(int level) {
        this.level = level;
        return this;
    }

    public GetByRoleOptions setName(String name) {
        this.name = name;
        return this;
    }

    public GetByRoleOptions setName(Pattern name) {
        this.name = name;
        return this;
    }

    public GetByRoleOptions setPressed(boolean pressed) {
        this.pressed = pressed;
        return this;
    }

    public GetByRoleOptions setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
