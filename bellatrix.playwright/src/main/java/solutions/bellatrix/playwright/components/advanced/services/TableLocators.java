package solutions.bellatrix.playwright.components.advanced.services;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TableLocators {
    public String getRowTag() {
        return "tr";
    }

    public String getHeaderTag() {
        return "th";
    }

    public String getCellTag() {
        return "td";
    }

    public String getFooterTag() {
        return "tfoot";
    }

    public String getHeaderXpath() {
        return "//" + getHeaderTag();
    }

    public String getCellXpath() {
        return "//" + getCellTag();
    }

    public String getRowsXpath() {
        return String.format("//%s[descendant::%s]", getRowTag(), getCellTag());
    }

    public String getHeadersXpath() {
        return String.format("//%s[descendant::%s]", getRowTag(), getHeaderTag());
    }

    public String getFooterRowsXpath() {
        return String.format("//%s[ancestor::%s]", getRowTag(), getFooterTag());
    }

    public String getFooterXpath() {
        return "//" + getFooterTag();
    }
}
