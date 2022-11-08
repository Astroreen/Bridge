package bridge.listeners;

import bridge.Bridge;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CustomLog
public class ListenerManager {

    private final Bridge plugin;
    private final HashMap<String, Listener> registered;

    public ListenerManager() {
        this.plugin = Bridge.getInstance();
        registered = new HashMap<>();

        //default listeners
        //TODO put here real default listeners or register them out of this class
        registered.put("GetServer", new GetServerEventListener());

        new BukkitRunnable() {
            @Override
            public void run() {
                // log which listeners have been registered
                if (!registered.isEmpty()) {
                    final StringBuilder builder = new StringBuilder();
                    for (final String name : registered.keySet()) {
                        Bukkit.getPluginManager().registerEvents(registered.get(name), plugin);
                        builder.append(name).append(", ");
                    }
                    final String plugins = builder.substring(0, builder.length() - 2);
                    LOG.debug("Registered listeners on start: " + plugins + ".");
                }
            }
        }.runTaskAsynchronously(Bridge.getInstance());

    }

    public void registerListener(final String name, final @NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        registered.put(name, listener);
        LOG.debug("Listener '" + name + "' was registered.");
    }

    public @NotNull List<Listener> getListeners() {
        List<Listener> listeners = new ArrayList<>();
        for (final String name : registered.keySet()) {
            listeners.add(registered.get(name));
        }
        return listeners;
    }
}
