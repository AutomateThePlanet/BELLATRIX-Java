package solutions.bellatrix.core.utilities;

import java.io.Serializable;

@FunctionalInterface
public interface PropertyReference<T> extends Serializable {
    Object apply(T t);
}
