package solutions.bellatrix.servicenow.utilities;

import org.apache.commons.lang3.time.StopWatch;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.servicenow.infrastructure.facades.TestDataFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FacadeFactory {
    private static Map<String, Object> facadeHolder = new HashMap<>();

    private FacadeFactory() {
    }

    public static <T extends TestDataFacade> T createFacade(Class<T> classOf, Object... initargs) {
        try {
            if (!facadeHolder.containsKey(classOf.getName())) {
                T obj = (T) classOf.getConstructors()[0].newInstance(initargs);
                facadeHolder.put(classOf.getName(), obj);
            }
            return (T) facadeHolder.get(classOf.getName());
        } catch (Exception e) {
            return null;
        }
    }

    public static void clearObjectsContainer() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (var abstractFacade : facadeHolder.entrySet()) {
            var concreteFacade = (TestDataFacade) abstractFacade.getValue();
            concreteFacade.deleteTestData();
        }

        clearInternalObjectContainerState();
        stopwatch.stop();
        Log.info("Cleaning up object took: %s s. ".formatted(stopwatch.getTime(TimeUnit.SECONDS)));
    }

    private static void clearInternalObjectContainerState() {
        facadeHolder = new HashMap<>();
    }
}