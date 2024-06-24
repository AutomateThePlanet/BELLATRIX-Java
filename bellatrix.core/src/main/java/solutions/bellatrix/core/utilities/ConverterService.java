package solutions.bellatrix.core.utilities;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConverterService {
    public static <SourceT, ResultT> ResultT convert(SourceT source, int index) {
        ResultT object = InstanceFactory.createByTypeParameter(source.getClass(), index);

        return convert(source, object);
    }

    public static <SourceT, ResultT> ResultT convert(SourceT source, ResultT result) {
        var object = (ResultT) InstanceFactory.create(result.getClass());

        var sourceFields = source.getClass().getDeclaredFields();
        var objectFields = object.getClass().getDeclaredFields();

        for (var sourceField : sourceFields) {
            for (var objectField : objectFields) {
                if (sourceField.getName().equals(objectField.getName())) {
                    sourceField.setAccessible(true);
                    objectField.setAccessible(true);

                    try {
                        objectField.set(object, sourceField.get(source));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }

        return object;
    }

    @SneakyThrows
    public static <SourceT, ResultT> ResultT convertToClass(SourceT source, Class<ResultT> result) {
        var object = (ResultT) InstanceFactory.create(result);

        List<Field> sourceFields = getAllFields(source.getClass());
        List<Field> objectFields = getAllFields(object.getClass());

        for (var sourceField : sourceFields) {
            for (var objectField : objectFields) {
                if (sourceField.getName().equals(objectField.getName())) {
                    sourceField.setAccessible(true);
                    objectField.setAccessible(true);

                    objectField.set(object, sourceField.get(source));
                    break;
                }
            }
        }

        return object;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null) {
            var declaredFields = currentClass.getDeclaredFields();
            for (Field field : declaredFields) {
                fields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }
}
