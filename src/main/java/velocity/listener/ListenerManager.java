package velocity.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.CustomLog;
import org.jetbrains.annotations.NotNull;
import velocity.BridgeVelocity;

import java.util.HashMap;

@CustomLog
public class ListenerManager {

    private static ProxyServer proxy;
    private static final HashMap<String, Object> registered = new HashMap<>();


    public static void setup(final @NotNull ProxyServer proxy) {
        ListenerManager.proxy = proxy;
        //default listeners
        //register("GetServer", new GetServerEventListener());
        proxy.getScheduler().buildTask(
                BridgeVelocity.getInstance(), () ->
                {
                    // log which listeners have been registered
                    if (!registered.isEmpty()) {
                        final StringBuilder builder = new StringBuilder();
                        for (final String name : registered.keySet())
                            builder.append(name).append(", ");

                        final String plugins = builder.substring(0, builder.length() - 2);
                        LOG.debug("Registered listeners on start: " + plugins + ".");
                    }
                }).schedule();
    }

    public static void register(final @NotNull String name, final @NotNull Object listener) {
        if (registered.containsKey(name) || registered.containsValue(listener)) return;
        proxy.getEventManager().register(BridgeVelocity.getInstance(), listener);
        registered.put(name, listener);
        //LOG.debug("Listener '" + name + "' was registered.");
    }
}
