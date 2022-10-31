package bridge.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GetServersEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final List<String> servers = new ArrayList<>();

    public GetServersEvent(String @NotNull [] servers) {
        for(String name : servers) {
            if(name == null) continue;
            this.servers.add(name);
        }
    }

    public List<String> getServers () {
        return servers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
