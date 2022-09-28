package bridge.compatibility;

import bridge.Bridge;
import bridge.compatibility.placeholderapi.PlaceholderAPIIntegrator;
import bridge.compatibility.tab.BRTABIntegrator;
import bridge.exceptions.HookException;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Compatibility with other plugins
 */
@CustomLog
public class Compatibility implements Listener {

    private static Compatibility instance;
    private final Map<String, Integrator> integrators = new HashMap<>();
    private final List<String> hooked = new ArrayList<>();

    public Compatibility() {
        instance = this;

        integrators.put("PlaceholderAPI", new PlaceholderAPIIntegrator());
        integrators.put("TAB", new BRTABIntegrator());
        // hook into already enabled plugins in case Bukkit messes up the loading order
        for (final Plugin hook : Bukkit.getPluginManager().getPlugins()) {
            hook(hook);
        }

        Bukkit.getPluginManager().registerEvents(this, Bridge.getInstance());
        new BukkitRunnable() {
            @Override
            public void run() {
                // log which plugins have been hooked
                if (!hooked.isEmpty()) {
                    final StringBuilder string = new StringBuilder();
                    for (final String plugin : hooked) {
                        string.append(plugin).append(", ");
                    }
                    final String plugins = string.substring(0, string.length() - 2);
                    LOG.info("Hooked into " + plugins + "!");
                }
            }
        }.runTask(Bridge.getInstance());

    }

    /**
     * @return the list of hooked plugins
     */
    public static List<String> getHooked() {
        return instance.hooked;
    }

    public static void reload() {
        for (final String hooked : getHooked()) {
            instance.integrators.get(hooked).reload();
        }
    }

    public static void disable() {
        if (instance != null) {
            for (final String hooked : getHooked()) {
                instance.integrators.get(hooked).close();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPluginEnable(final @NotNull PluginEnableEvent event) {
        hook(event.getPlugin());
    }

    private void hook(final @NotNull Plugin hookedPlugin) {

        // don't want to hook twice
        if (hooked.contains(hookedPlugin.getName())) {
            return;
        }

        // don't want to hook into disabled plugins
        if (!hookedPlugin.isEnabled()) {
            return;
        }

        final String name = hookedPlugin.getName();
        final Integrator integrator = integrators.get(name);

        // this plugin is not an integration
        if (integrator == null) {
            return;
        }

        // hook into the plugin if it's enabled in the config
        if (Bridge.getInstance().getPluginConfig().getBoolean("hook." + name.toLowerCase(Locale.ROOT))) {
            LOG.info("Hooking into " + name);

            // log important information in case of an error
            try {
                integrator.hook();
                hooked.add(name);
            } catch (final HookException exception) {
                final String message = String.format("Could not hook into %s %s! %s",
                        hookedPlugin.getName(),
                        hookedPlugin.getDescription().getVersion(),
                        exception.getMessage());
                LOG.warn(message, exception);
                LOG.warn("Bridge will work correctly, except for that single integration. "
                        + "You can turn it off by setting 'options." + name.toLowerCase(Locale.ROOT)
                        + "' to false in config.yml file.");
            } catch (final RuntimeException | LinkageError exception) {
                final String message = String.format("There was an unexpected error while hooking into %s %s (Bridge %s, Spigot %s)! %s",
                        hookedPlugin.getName(),
                        hookedPlugin.getDescription().getVersion(),
                        Bridge.getInstance().getDescription().getVersion(),
                        Bukkit.getVersion(),
                        exception.getMessage());
                LOG.error(message, exception);
                LOG.warn("BetonQuest will work correctly, except for that single integration. "
                        + "You can turn it off by setting 'hook." + name.toLowerCase(Locale.ROOT)
                        + "' to false in config.yml file.");
            }
        }
    }
}
