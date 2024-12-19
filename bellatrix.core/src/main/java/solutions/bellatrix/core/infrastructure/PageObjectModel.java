package solutions.bellatrix.core.infrastructure;

import solutions.bellatrix.core.utilities.InstanceFactory;

public interface PageObjectModel<MapT, AssertsT> {
    default MapT map() {
        return InstanceFactory.createByTypeParameter(this.getClass(), 0);
    }

    default AssertsT asserts() {
        return InstanceFactory.createByTypeParameter(this.getClass(), 1);
    }
}
