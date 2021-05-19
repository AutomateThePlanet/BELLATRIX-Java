package solutions.bellatrix.core.plugins;

import solutions.bellatrix.core.utilities.SingletonFactory;

import java.util.Objects;

public abstract class UsesPlugins {
    public <T extends Plugin> void addPlugin(Class<T> plugin, Object... args) {
        PluginExecutionEngine.addPlugin(SingletonFactory.getInstance(plugin, args));
    }

    public <T extends Listener> void addListener(Class<T> listener, Object... args) {
        Objects.requireNonNull(SingletonFactory.getInstance(listener, args)).addListener();
    }
}
