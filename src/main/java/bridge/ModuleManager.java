package bridge;

import bridge.compatibility.tab.BRTABModule;
import common.exceptions.HookException;
import bridge.module.HeadshotModule;
import bridge.module.ShiftFlyModule;
import bridge.module.ffa.FFA;
import bridge.packets.player.EmojiTaber;
import bridge.packets.player.MentionTaber;
import common.IModule;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * ModuleManager with modules represented as Enums.
 */
@CustomLog(topic = "ModuleManager")
public enum ModuleManager {

    FFA("ffa", new FFA()),
    NICKNAME("nickname", new BRTABModule()),
    SHIFT_FLY("shift-fly", new ShiftFlyModule()),
    EMOJI_TABER("emoji-taber", new EmojiTaber()),
    MENTION_TABER("mention-taber", new MentionTaber()),
    HEADSHOT("headshot-modification", new HeadshotModule()),

    ;

    private final String path;
    private boolean active;
    private final IModule module;

    ModuleManager(final @NotNull String path,
                  final @NotNull IModule module) {
        this.path = "modules." + path; //config path
        this.module = module;
        this.active = false;
    }

    private static Bridge plugin;
    private static final Set<ModuleManager> modules = new HashSet<>();

    /**
     * Load all modules to memory.
     * Will not replace if module already exist.
     *
     * @param plugin plugin
     */
    public static void setup(final @NotNull Bridge plugin) {
        ModuleManager.plugin = plugin;
        modules.addAll(Arrays.asList(ModuleManager.values()));
    }

    /**
     * Enable all modules than can be enabled.
     */
    public static void start() {
        if (modules.isEmpty()) return;
        new ArrayList<>(modules).forEach(module -> {
            if (plugin.getPluginConfig().getBoolean(getPath(module), false)) {
                if (!start(plugin, module)) modules.remove(module);
            } else modules.remove(module);
        });
    }

    private static boolean start(final @NotNull Bridge plugin, final @NotNull ModuleManager module) {
        final IModule iModule = module.getModule();
        if (!module.isActive() && iModule.isConditionsMet()) {
            // log important information in case of an error
            try {
                return module.setActive(iModule.start(plugin));
            } catch (final HookException exception) {
                final String message = String.format("Could not hook into %s module! %s",
                        iModule.getName(),
                        exception.getMessage());
                LOG.warn(message, exception);
                LOG.warn("Bridge will work correctly, except for that single module. "
                        + "You can turn it off by setting 'modules." + iModule.getName().toLowerCase(Locale.ROOT)
                        + "' to false in config.yml file.");
            } catch (final RuntimeException | LinkageError exception) {
                final String message = String.format("There was an unexpected error while hooking into %s module (Bridge %s, Spigot %s)! %s",
                        iModule.getName(),
                        Bridge.getInstance().getDescription().getVersion(),
                        Bukkit.getVersion(),
                        exception.getMessage());
                LOG.error(message, exception);
                LOG.warn("Bridge will work correctly, except for that single integration. "
                        + "You can turn it off by setting 'modules." + iModule.getName().toLowerCase(Locale.ROOT)
                        + "' to false in config.yml file.");
            }
        }
        return module.isActive();
    }

    public static void reload(final @NotNull Bridge plugin) {
        // load all modules again
        setup(plugin);
        if (modules.isEmpty()) return;
        // using set, to then delete modules, because
        // you cant modify another set during `for` loop
        final Set<ModuleManager> disabling = new HashSet<>();
        modules.forEach(module -> {
            final IModule iModule = module.getModule();
            final boolean use = plugin.getPluginConfig().getBoolean(getPath(module), false);
            if (use) {
                if (!module.isActive()) {
                    if (iModule.isConditionsMet()) start(plugin, module);
                    else disabling.add(module);
                } else iModule.reload();
            } else disable(module);

        });
        // disabling modules
        disabling.forEach(modules::remove);
    }

    public static void disable() {
        if (modules.isEmpty()) return;
        for (ModuleManager module : modules) disable(module);
    }

    private static void disable(final @NotNull ModuleManager module) {
        if (module.isActive()) {
            module.getModule().disable();
            module.setActive(false);
            modules.remove(module);
        }
    }

    private boolean setActive(final boolean active) {
        this.active = active;
        return this.active;
    }

    private static @NotNull String getPath(final @NotNull ModuleManager module) {
        return module.path;
    }

    /**
     * Get module from {@link ModuleManager} enum.
     *
     * @return module or null if not exist.
     */
    public @NotNull IModule getModule() {
        return module;
    }

    /**
     * Get {@linkplain ModuleManager Enum} representation of {@linkplain IModule module}.
     *
     * @param iModule module
     * @return Enum representation of module or null
     */
    public static @Nullable ModuleManager getModel(final @NotNull IModule iModule) {
        for (final @NotNull ModuleManager module : ModuleManager.values()) {
            if (module.getModule().equals(iModule)) return module;
        }
        return null;
    }

    /**
     * Check if module active right now.
     *
     * @return true, if it does.
     */
    public boolean isActive() {
        return active;
    }
}
