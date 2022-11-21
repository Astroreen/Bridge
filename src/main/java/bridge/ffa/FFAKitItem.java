package bridge.ffa;

import bridge.compatibility.itemsadder.ItemsAdderManager;
import bridge.config.ConfigurationFile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class FFAKitItem extends ItemStack {

    private static ConfigurationFile config;

    private String kit;
    private int slot;
    private ItemStack item;
    private boolean isItemsAdderItem;


    public FFAKitItem(final @NotNull ConfigurationFile config) {
        FFAKitItem.config = config;
    }

    public boolean create(final @NotNull String kit, final @NotNull ItemStack item, final int slot) throws IOException {
        this.kit = kit;
        this.item = item;
        this.slot = slot;

        config.set(String.format("%s.%s.item", kit, slot), item);
        config.set(String.format("%s.%s.itemsAdder", kit, slot), "none");
        isItemsAdderItem = false;
        return config.save();
    }

    public @Nullable ItemStack load(final @NotNull String kit, final int slot) {
        if (!isCreated()) return null;
        final String id = config.getString(String.format("%s.%s.itemsAdder", kit, slot), "none");
        if (ItemsAdderManager.isIDValid(id)) {
            ItemStack temp = ItemsAdderManager.getInstance().getItem(id);
            if (temp != null) {
                this.item = temp;
                return item;
            }
        }
        return config.getItemStack(String.format("%s.%s.item", kit, slot));

    }


    public void setIAItem(final @NotNull String id) throws IOException {
        if (!ItemsAdderManager.isIDValid(id)) return;
        config.set(String.format("%s.%s.item", kit, slot), id);
        config.save();
    }

    public int getSlotIndex() {
        return slot;
    }

    public String getKitName() {
        return kit;
    }

    public ItemStack getItem() {
        return toItem();
    }

    public ItemStack toItem() {
        return item;
    }

    public boolean isIAItem() {
        return isItemsAdderItem;
    }

    public boolean isCreated() {
        return config.getConfigurationSection(String.format("%s.%s", kit, slot)) != null;
    }


}
