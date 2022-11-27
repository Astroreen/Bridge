package bridge.ffa;

import bridge.compatibility.itemsadder.IAManager;
import bridge.config.ConfigurationFile;
import bridge.exceptions.ObjectNotFoundException;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class FFAKitItem extends ItemStack {

    private static ConfigurationFile config;

    private String kit;
    private int slot;
    private ItemStack item;
    private String itemsAdderID;


    public FFAKitItem(final @NotNull ConfigurationFile config) {
        FFAKitItem.config = config;
    }

    private FFAKitItem(final @NotNull ConfigurationFile config, final @NotNull String kit, final int slot, final @NotNull ItemStack item) {
        FFAKitItem.config = config;
        this.item = item;
        this.slot = slot;
        this.kit = kit;
        this.itemsAdderID = "none";
    }

    private FFAKitItem(final @NotNull ConfigurationFile config, final @NotNull String kit, final int slot, final @NotNull CustomStack item) {
        FFAKitItem.config = config;
        this.item = item.getItemStack();
        this.slot = slot;
        this.kit = kit;
        this.itemsAdderID = item.getId();
    }

    /**
     * Creates {@link FFAKitItem} instance.
     *
     * @param kit  which kit set to
     * @param slot to which slot save
     * @param item item base
     * @return created item
     */
    public FFAKitItem create(final @NotNull String kit, final int slot, final @NotNull ItemStack item) {
        this.kit = kit;
        this.item = item;
        this.slot = slot;

        config.set(String.format("%s.%s.item", kit, slot), item);
        config.set(String.format("%s.%s.itemsAdder", kit, slot), "none");
        itemsAdderID = "none";
        return new FFAKitItem(config, kit, slot, item);
    }

    /**
     * Creates {@link FFAKitItem} instance from ItemsAdder's item.
     *
     * @param kit  which kit set to
     * @param slot to which slot save
     * @param item item base
     * @return created item
     */
    public FFAKitItem create(final @NotNull String kit, final int slot, final @NotNull CustomStack item) {
        this.kit = kit;
        this.item = item.getItemStack();
        this.slot = slot;

        config.set(String.format("%s.%s.item", kit, slot), this.item);
        config.set(String.format("%s.%s.itemsAdder", kit, slot), item.getId());
        itemsAdderID = item.getId();
        return new FFAKitItem(config, kit, slot, item);
    }

    /**
     * Loads item from config.
     *
     * @param kit  the kit name
     * @param slot the slot index
     * @return {@link FFAKitItem} instance
     * @throws ObjectNotFoundException when item wasn't found/created in config.
     */
    public @NotNull FFAKitItem load(final @NotNull String kit, final int slot) throws ObjectNotFoundException {
        if (!isCreated()) throw new ObjectNotFoundException("FFAKitItem object is not created yet!");
        final ItemStack stack = config.getItemStack(String.format("%s.%s.item", kit, slot));
        assert stack != null;
        final FFAKitItem item = new FFAKitItem(config, kit, slot, stack);
        final String id = config.getString(String.format("%s.%s.itemsAdder", kit, slot), "none");
        if (IAManager.isIDValid(id)) {
            CustomStack temp = IAManager.getItem(id);
            if (temp != null) {
                item.replaceWithIAItem(temp);
            }
        }
        return item;
    }

    /**
     * Saves this instance to kits-config.yml file.
     *
     * @throws IOException when can't save to config
     */
    public void save() throws IOException {
        config.set(String.format("%s.%s.item", kit, slot), new ItemStack(item));
        config.set(String.format("%s.%s.itemsAdder", kit, slot), itemsAdderID);
        config.save();
    }

    /**
     * Clone this item to another kit or slot or both.
     *
     * @param item item base
     * @param kit  the kit name
     * @param slot the slot index
     * @return new cloned item
     * @throws ObjectNotFoundException when passed item base was not created yet.
     */
    public FFAKitItem clone(final @NotNull FFAKitItem item, final @NotNull String kit, final int slot) throws ObjectNotFoundException {
        if (!item.isCreated()) throw new ObjectNotFoundException("FFAKitItem object is not created yet!");
        if (item.isIAItem()) {
            final String id = item.getIAItemID();
            if (id == null) return create(kit, slot, item);
            final CustomStack custom = IAManager.getItem(id);
            if (custom != null) return create(kit, slot, custom);
        }
        return create(kit, slot, item);
    }


    /**
     * Set this item as ItemsAdder's item.
     *
     * @param id item's id
     */
    public void replaceWithIAItem(final @NotNull String id) {
        if (!IAManager.isIDValid(id)) return;
        replaceWithIAItem(CustomStack.getInstance(id));
    }

    /**
     * Set this item as ItemsAdder's item.
     *
     * @param item {@link CustomStack} item
     */
    public void replaceWithIAItem(final @NotNull CustomStack item) {
        this.itemsAdderID = item.getId();
        this.item = item.getItemStack();
    }

    /**
     * Get index of slot similar to
     * <p>
     * {@link org.bukkit.inventory.Inventory#getItem(int)}
     *
     * @return slot index
     */
    public int getSlotIndex() {
        return slot;
    }

    /**
     * Get kit's name.
     *
     * @return kit's name
     */
    public String getKitName() {
        return kit;
    }

    /**
     * Check if this item is itemsAdder's item.
     *
     * @return true if it is
     */
    public boolean isIAItem() {
        return !itemsAdderID.equals("none");
    }

    public @Nullable String getIAItemID() {
        if (isCreated()) return itemsAdderID;
        else return null;
    }

    /**
     * Check if this item was created.
     *
     * @return true if was created
     */
    public boolean isCreated() {
        return isCreated(kit, slot);
    }

    /**
     * Check if this item was created.
     *
     * @param kit  the kit's name
     * @param slot the slot index
     * @return true if was created
     */
    public static boolean isCreated(final @NotNull String kit, final int slot) {
        return config.getConfigurationSection(String.format("%s.%s", kit, slot)) != null;
    }
}
