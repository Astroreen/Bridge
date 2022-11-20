package bridge.config;

import bridge.Bridge;
import bridge.compatibility.tab.TABManager;
import bridge.ffa.FFA;
import lombok.CustomLog;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;

@CustomLog(topic = "ModuleManager")
public class ModuleManager {

    private static Bridge plugin;
    private static final HashMap<String, Module> modules = new HashMap<>();
    private static final String MODULE_PATH = "settings.modules";
    private static final String ENABLED = ".enabled";

    /**
     * Load all modules to memory.
     * Will not replace if module already exist.
     *
     * @param plugin plugin
     */
    public static void setup(final Bridge plugin) {
        ModuleManager.plugin = plugin;
        HashMap<String, Module> temp = new HashMap<>();
        temp.put("ffa", new FFA());
        temp.put("tab", new TABManager());
        for (String key : temp.keySet())
            if (!modules.containsKey(key)) modules.put(key, temp.get(key));
    }

    public static void start() {
        for (final String path : modules.keySet()) {
            final boolean use = plugin.getPluginConfig().getBoolean(MODULE_PATH + path + ENABLED, false);
            if (use) {
                final Module module = modules.get(path);
                if (!start(plugin, module)) modules.remove(path);
            } else modules.remove(path);
        }
    }

    private static boolean start(final @NotNull Bridge plugin, final @NotNull Module module) {
        if (!module.active() && module.isConditionsMet()) return module.start(plugin);
        else return module.active();
    }

    public static void reload(final @NotNull Bridge plugin) {
        //load all modules again
        setup(plugin);
        for (String key : modules.keySet()) {
            final Module module = modules.get(key);
            final boolean use = plugin.getPluginConfig().getBoolean(MODULE_PATH + key + ENABLED, false);
            if (use) {
                if (!module.active()) {
                    if (module.isConditionsMet()) start(plugin, module);
                    else modules.remove(key);
                } else module.reload();
            } else {
                if (module.active()) module.disable();
                modules.remove(key);
            }
        }
    }

    public static void disable() {
        for (String key : modules.keySet()) disable(modules.get(key));
    }

    private static void disable(final @NotNull Module module) {
        if (module.active()) module.disable();
    }

    /**
     * Get Set of active module's names.
     *
     * @return {@link Set} of names
     */
    @Contract(pure = true)
    public static @NotNull Set<String> getActive() {
        return modules.keySet();
    }

    /**
     * Get module by name.
     *
     * @param name the module name
     * @return module or null if not exist.
     */
    public static @Nullable Module getModule(final @NotNull String name) {
        return modules.getOrDefault(name, null);
    }
}