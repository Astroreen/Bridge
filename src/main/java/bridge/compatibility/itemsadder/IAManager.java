package bridge.compatibility.itemsadder;

import bridge.listeners.ListenerManager;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IAManager implements Listener {

    @Getter
    private static IAManager instance;
    private static boolean isActive = false;

    public IAManager() {
        instance = this;
        ListenerManager.register("ItemsAdder", this);
    }


    @EventHandler
    public void onItemsAdderLoadDataEvent(final @NotNull ItemsAdderLoadDataEvent event){
        if(event.getCause().equals(ItemsAdderLoadDataEvent.Cause.FIRST_LOAD)) isActive = true;
        else if (event.getCause().equals(ItemsAdderLoadDataEvent.Cause.RELOAD)) reload();
    }

    public void reload() {
        //empty
    }

    /**
     * Gets custom item as {@link CustomStack}.
     * @param id the item id
     * @return ItemsAdder's custom item
     */
    public static @Nullable CustomStack getItem(final @NotNull String id) {
        if(!isActive()) return null;
        return CustomStack.getInstance(id);

    }

    public static boolean isItemExist(final @NotNull String id){
        if(!isActive()) return false;
        if(isIDValid(id)) return CustomStack.getInstance(id) != null;
        else return false;
    }

    public static boolean isIDValid(final @NotNull String id){
        if(id.contains(":")) return id.matches("^[A-Za-z1-9_\\-+()./\\\\\\[\\]<>]+:[A-Za-z1-9_\\-+()./\\\\\\[\\]<>]+");
        else return true;
    }

    public static boolean isActive() {return isActive;}
}
