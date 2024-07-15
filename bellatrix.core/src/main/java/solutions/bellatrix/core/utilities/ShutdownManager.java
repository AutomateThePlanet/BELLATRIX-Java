package solutions.bellatrix.core.utilities;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ShutdownManager {
    private static final List<AutoCloseable> resources = new ArrayList<>();
    private static final List<Runnable> instructions = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ShutdownManager::runAllInstructions));
    }

    public static void register(Runnable runnable) {
        instructions.add(runnable);
    }

    private static void runAllInstructions() {
        if (instructions.isEmpty()) return;

        for (var instruction : instructions) {
            instruction.run();
        }
    }
}