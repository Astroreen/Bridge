package bridge.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GetServerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    String server;

    public GetServerEvent (String server) {
        this.server = server;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
