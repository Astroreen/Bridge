package bridge.compatibility.itemsadder;

import bridge.listeners.ListenerManager;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemsAdderManager implements Listener {

    @Getter
    private static ItemsAdderManager instance;
    private static boolean isActive = false;

    public ItemsAdderManager() {
        instance = this;
        ListenerManager.register("ItemsAdder", this);
    }


    @EventHandler
    public void onItemsAdderLoadDataEvent(@NotNull ItemsAdderLoadDataEvent event){
        if(event.getCause().equals(ItemsAdderLoadDataEvent.Cause.FIRST_LOAD)) isActive = true;
        else if (event.getCause().equals(ItemsAdderLoadDataEvent.Cause.RELOAD)) reload();
    }

    public void reload() {
        //empty
    }

    public @Nullable ItemStack getItem(final @NotNull String id){
        CustomStack stack = CustomStack.getInstance(id);
        if(stack != null) {
            return stack.getItemStack();
        } else return null;
    }

    public boolean isItemExist(final @NotNull String id){
        if(isIDValid(id)) return CustomStack.getInstance("your_item") != null;
        else return false;
    }

    public static boolean isIDValid(final @NotNull String id){
        return id.matches("^[A-Za-z1-9]+:[A-Za-z1-9]+");
    }

    public static boolean isActive() {return isActive;}
}
