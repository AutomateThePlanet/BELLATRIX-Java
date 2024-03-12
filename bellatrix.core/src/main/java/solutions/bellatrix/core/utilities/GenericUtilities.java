package solutions.bellatrix.core.utilities;

import lombok.SneakyThrows;

// ToDo This class needs to be removed and the methods inside moved somewhere else
public class GenericUtilities {
    // ToDo Dozer must be used instead
    @SneakyThrows
    public static <SourceT, ResultT> ResultT convert(SourceT source, int index) {
        ResultT object = InstanceFactory.createByTypeParameter(source.getClass(), index);

        return convert(source, object);
    }

    // ToDo Dozer must be used instead
    @SneakyThrows
    public static <SourceT, ResultT> ResultT convert(SourceT source, ResultT result) {
        var object = (ResultT) InstanceFactory.create(result.getClass());

        var sourceFields = source.getClass().getDeclaredFields();
        var objectFields = object.getClass().getDeclaredFields();

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
}
