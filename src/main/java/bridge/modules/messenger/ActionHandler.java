package bridge.modules.messenger;

import bridge.Bridge;
import bridge.event.GetServerEvent;
import bridge.event.GetServersEvent;
import bridge.event.RunUpdaterEvent;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

@CustomLog( topic = "ActionHandler")
public class ActionHandler implements Runnable {

    private final Channel channel;
    private final Action action;
    private final ConcurrentLinkedQueue<String> data;

    ActionHandler(Channel channel, Action action, ConcurrentLinkedQueue<String> data) {
        this.channel = channel;
        this.action = action;
        this.data = data;
        Bukkit.getScheduler().runTaskAsynchronously(Bridge.getInstance(), this);
    }


    @Override
    public void run() {
        if (channel == Channel.BRIDGE)
            BridgeHandler(action, data);
        else if (channel == Channel.BUNGEECORD)
            BungeeCordHandler(action, data);
        else LOG.debug("ActionHandler was triggered, but no channel was found.");
    }

    private void BridgeHandler(@NotNull Action action, ConcurrentLinkedQueue<String> data) {
        switch (action) {
            case RUN_UPDATER -> Bukkit.getPluginManager().callEvent(new RunUpdaterEvent());
        }
    }

    private void BungeeCordHandler(@NotNull Action action, ConcurrentLinkedQueue<String> data){
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
