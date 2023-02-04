package bridge.pluginmodule.messenger;

import bridge.Bridge;
import bridge.event.GetServerEvent;
import bridge.event.GetServersEvent;
import bridge.event.RunUpdaterEvent;
import common.messanger.Action;
import common.messanger.ActionHandler;
import common.messanger.Channel;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

@CustomLog( topic = "ActionHandler")
public class ServerActionHandler extends ActionHandler {

    private final Channel channel;
    private final Action action;
    private final ConcurrentLinkedQueue<String> data;

    ServerActionHandler(Channel channel, Action action, ConcurrentLinkedQueue<String> data) {
        super(channel, action, data);
        this.channel = channel;
        this.action = action;
        this.data = data;
    }

    @Override
    protected void schedule(@NotNull Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(Bridge.getInstance(), task);
    }

    @Override
    protected void ServerHandler(final @NotNull Action action, final ConcurrentLinkedQueue<String> data) {
        switch (action) {
            case RUN_UPDATER -> Bukkit.getPluginManager().callEvent(new RunUpdaterEvent());
        }
    }

    @Override
    protected void ProxyHandler(final @NotNull Action action, final ConcurrentLinkedQueue<String> data){
        switch (action){
            case GET_SERVER -> Bukkit.getPluginManager().callEvent(new GetServerEvent(data.poll()));
            case GET_SERVERS -> {
                String rawServers = data.poll();
                if (rawServers == null) {
                    LOG.debug("Action 'GET_SERVERS' was triggered, but no servers names passed");
                    return;
                }
                Bukkit.getPluginManager().callEvent(new GetServersEvent(Arrays.asList(rawServers.split(", "))));
            }
        }
    }
}
