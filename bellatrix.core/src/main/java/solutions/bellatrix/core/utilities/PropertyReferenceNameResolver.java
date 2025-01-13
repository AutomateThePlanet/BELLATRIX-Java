package solutions.bellatrix.core.utilities;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@UtilityClass
public class PropertyReferenceNameResolver {
    public static <T> Field getMember(Class<T> clazz, PropertyReference<T> propertyReference) {
        var methodName = getFunctionalMethodName(propertyReference);
        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
            if (methodName.toLowerCase().contains(field.getName().toLowerCase())) {
                return field;
            }
        }

        return null;
    }

    @SneakyThrows
    public static String getFunctionalMethodName(PropertyReference<?> functionalInterface) {
        Method writeReplaceMethod = functionalInterface.getClass().getDeclaredMethod("writeReplace");
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(functionalInterface);

        return serializedLambda.getImplMethodName();
    }
}
