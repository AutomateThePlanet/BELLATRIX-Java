package solutions.bellatrix.core.utilities.parsing;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
// TODO: RENAME ME
public class ParsingInstructions<TFrom> extends HashMap<Class<?>, Function<TFrom, ?>> {
    public ParsingInstructions() {
        super();
    }

    public ParsingInstructions(Map<Class<?>, Function<TFrom, ?>> map) {
        super(map);
    }

    public <TTo> ParsingInstructions add(Class<TTo> toClass, Function<TFrom, ?> parsingInstructions) {
        // TODO: EventHandler<> for changing existing instructions
        super.put(toClass, parsingInstructions);
        return this;
    }

    public Function<TFrom, ?> get(Class<TFrom> clazz) {
        return super.get(clazz);
    }

    public static ParsingInstructions ofEntries(Entry<Class<?>, Function<?, ?>>... entries) {
        return new ParsingInstructions(Map.ofEntries(entries));
    }
}