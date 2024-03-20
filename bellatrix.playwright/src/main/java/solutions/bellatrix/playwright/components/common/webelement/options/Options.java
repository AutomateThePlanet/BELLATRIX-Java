package solutions.bellatrix.playwright.components.common.webelement.options;

import solutions.bellatrix.core.utilities.ConverterService;

public interface Options {
    default <T> T convertTo(Class<T> optionClass) {
        return ConverterService.convertToClass(this, optionClass);
    }
}
