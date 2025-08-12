package solutions.bellatrix.servicenow.utilities;

import java.lang.reflect.Field;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;

public class MapperUtility {
    private static ModelMapper modelMapper = new ModelMapper();

    @SneakyThrows
    public static <S, D> D map(S source, Class<D> destinationType, Object... additionalObjects) {
        var mappedResult = modelMapper.map(source, destinationType);
        for (Object additionalObject : additionalObjects) {
            var type = additionalObject.getClass();
            if (hasFieldOfType(destinationType, type)) {
                var field = getFieldOfType(destinationType, type);
                field.setAccessible(true);
                field.set(mappedResult, additionalObject);
            }
        }

        return mappedResult;
    }

    private static boolean hasFieldOfType(Class<?> clazz, Class<?> fieldType) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(fieldType)) {
                return true;
            }
        }

        return false;
    }

    private static Field getFieldOfType(Class<?> clazz, Class<?> fieldType) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(fieldType)) {
                return field;
            }
        }

        return null;
    }
}