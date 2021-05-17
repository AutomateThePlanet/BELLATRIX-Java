package solutions.bellatrix.core.plugins;

import solutions.bellatrix.core.utilities.SingletonFactory;

import java.util.Objects;

public abstract class UsesPlugins {
    public void addPlugin(Plugin plugin) {
        PluginExecutionEngine.addPlugin(plugin);
    }

    public <T extends Listener> void addListener(Class<T> listener, Object ... args) {
        Objects.requireNonNull(SingletonFactory.getInstance(listener, args)).addListener();
    }
}
