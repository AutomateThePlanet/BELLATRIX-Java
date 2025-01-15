package solutions.bellatrix.core.infrastructure;

import solutions.bellatrix.core.utilities.SingletonFactory;

public interface BellatrixApp extends AutoCloseable{

    void addDriverOptions(String key, String value);

    default <TPage extends PageObjectModel> TPage createPage(Class<TPage> pageOf, Object... args) {
        return SingletonFactory.getInstance(pageOf, args);
    }

    default <TSection extends PageObjectModel> TSection createSection(Class<TSection> pageOf, Object... args) {
        return SingletonFactory.getInstance(pageOf, args);
    }
}
