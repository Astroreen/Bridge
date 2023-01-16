package bridge;

import bridge.compatibility.tab.BRTABModule;
import bridge.exceptions.HookException;
import bridge.module.HeadshotModule;
import bridge.module.ffa.FFA;
import bridge.module.ShiftFlyModule;
import bridge.packets.player.EmojiTaber;
import bridge.packets.player.MentionTaber;
import common.Module;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@CustomLog(topic = "ModuleManager")
public class ModuleManager {

    private static Bridge plugin;
    private static final HashMap<String, Module> modules = new HashMap<>();
    private static final String MODULE_PATH = "settings.modules.";
    private static final String ENABLED = ".enabled";

    /**
     * Load all modules to memory.
     * Will not replace if module already exist.
     *
     * @param plugin plugin
     */
    public static void setup(final Bridge plugin) {
        ModuleManager.plugin = plugin;
        final HashMap<String, Module> temp = new HashMap<>();
        //first - path in config, second - module class
        temp.put("ffa", new FFA());
        temp.put("tab", new BRTABModule());
        temp.put("emoji-taber", new EmojiTaber());
        temp.put("mention-taber", new MentionTaber());
        temp.put("shift-fly", new ShiftFlyModule());
        temp.put("headshot-particles", new HeadshotModule());
        temp.keySet().forEach(key -> {
            if (!modules.containsKey(key)) modules.put(key, temp.get(key));
        });
    }

    public static void start() {
        if(modules.isEmpty()) return;
        new ArrayList<>(modules.keySet()).forEach(path -> {
            final boolean use = plugin.getPluginConfig().getBoolean(MODULE_PATH + path + ENABLED, false);
            if (use) {
                final Module module = modules.get(path);
                if (!start(plugin, module)) modules.remove(path);
            } else modules.remove(path);
        });
    }

    private static boolean start(final @NotNull Bridge plugin, final @NotNull Module module) {
        if (!module.active() && module.isConditionsMet()) {
            // log important information in case of an error
            try {
                return module.start(plugin);
            } catch (final HookException exception) {
                final String message = String.format("Could not hook into %s module! %s",
                        module.getName(),
                        exception.getMessage());
                LOG.warn(message, exception);
                LOG.warn("Bridge will work correctly, except for that single module. "
                        + "You can turn it off by setting 'settings.modules." + module.getName().toLowerCase(Locale.ROOT)
                        + "' to false in config.yml file.");
            } catch (final RuntimeException | LinkageError exception) {
                final String message = String.format("There was an unexpected error while hooking into %s module (Bridge %s, Spigot %s)! %s",
                        module.getName(),
                        Bridge.getInstance().getDescription().getVersion(),
                        Bukkit.getVersion(),
                        exception.getMessage());
                LOG.error(message, exception);
                LOG.warn("Bridge will work correctly, except for that single integration. "
                        + "You can turn it off by setting 'settings.modules." + module.getName().toLowerCase(Locale.ROOT)
                        + "' to false in config.yml file.");
            }
        }
        return module.active();
    }

    public static void reload(final @NotNull Bridge plugin) {
        //load all modules again
        setup(plugin);
        if(modules.isEmpty()) return;
        final Set<String> disabling = new HashSet<>();
        modules.keySet().forEach(key -> {
            final Module module = modules.get(key);
            final boolean use = plugin.getPluginConfig().getBoolean(MODULE_PATH + key + ENABLED, false);
            if (use) {
                if (!module.active()) {
                    if (module.isConditionsMet()) start(plugin, module);
                    else disabling.add(key);
                } else module.reload();
            } else {
                if (module.active()) module.disable();
                disabling.add(key);
            }
        });
        disabling.forEach(modules::remove);
    }

    public static void disable() {
        if(modules.isEmpty()) return;
        for (String key : modules.keySet()) disable(modules.get(key));
    }

    private static void disable(final @NotNull Module module) {
        if (module.active()) module.disable();
    }

    /**
     * Get Set of active module's names.
     *
     * @return {@link Set<String>} of names
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
