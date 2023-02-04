package bridge.module;

import bridge.Bridge;
import bridge.listener.ListenerManager;
import common.IModule;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.NotNull;

public class DaySleepModule implements IModule, Listener {
    @Override
    public boolean start(@NotNull Bridge plugin) {
        ListenerManager.register(getName(), this);
        return true;
    }

    @Override
    public void reload() {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerEnterBedEvent (final @NotNull PlayerBedEnterEvent event){
        if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW)) {
            event.setCancelled(false);
            event.setUseBed(Event.Result.ALLOW);
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isConditionsMet() {
        return true;
    }

    @Override
    public String getName() {
        return "DaySleep";
    }
}
