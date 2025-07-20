package solutions.bellatrix.data.http.contracts;

import com.google.gson.annotations.SerializedName;
import solutions.bellatrix.data.http.infrastructure.QueryParameter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * The Queryable interface provides a method to convert an object into a list of query parameters.
 */
public interface Queryable {
    default LinkedList<QueryParameter> toQueryParams() {
        try {
            var queryParameters = new LinkedList<QueryParameter>();
            Class<?> clazz = this.getClass();

            while (clazz!=null) {
                Field[] fields = clazz.getDeclaredFields();
                Arrays.stream(fields).forEach(x -> x.setAccessible(true));
                for (Field field : fields) {
                    if (field.isAnnotationPresent(SerializedName.class)) {
                        var queryParameterName = field.getAnnotation(SerializedName.class).value();
                        var value = field.get(this);
                        if (Objects.nonNull(value)) {
                            queryParameters.add(new QueryParameter(queryParameterName, value));
                        }
                    }
                }

                clazz = clazz.getSuperclass();
            }

            return queryParameters;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}