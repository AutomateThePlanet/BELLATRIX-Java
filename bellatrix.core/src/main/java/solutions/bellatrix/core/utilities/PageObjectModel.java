package solutions.bellatrix.core.utilities;


public interface PageObjectModel<MapT, AssertsT> {
    MapT map();

    AssertsT asserts();
}
