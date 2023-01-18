package bridge.module.ffa;

import bridge.compatibility.itemsadder.IAManager;
import common.config.ConfigurationFile;
import common.exceptions.ObjectNotFoundException;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class FFAKitItem extends ItemStack {

    private static ConfigurationFile config;

    private String kit;
    private int slot;
    private String itemsAdderID;


    public FFAKitItem(final @NotNull ConfigurationFile config) {
        FFAKitItem.config = config;
    }

    private FFAKitItem(final @NotNull ConfigurationFile config, final @NotNull String kit, final int slot, final @NotNull ItemStack item) {
        FFAKitItem.config = config;
        this.slot = slot;
        this.kit = kit;
        this.itemsAdderID = "none";

        final Material type = item.getType();
        this.setType(type);
        this.setAmount(item.getAmount());
        if (type.isLegacy()) this.setData(item.getData());
        if (item.hasItemMeta()) this.setItemMeta(item.getItemMeta());
    }

    private FFAKitItem(final @NotNull ConfigurationFile config, final @NotNull String kit, final int slot, final @NotNull CustomStack customItem) {
        FFAKitItem.config = config;
        this.slot = slot;
        this.kit = kit;
        this.itemsAdderID = customItem.getId();

        final ItemStack item = customItem.getItemStack();
        final Material type = item.getType();
        this.setType(type);
        this.setAmount(item.getAmount());
        if (type.isLegacy()) this.setData(item.getData());
        if (item.hasItemMeta()) this.setItemMeta(item.getItemMeta());
    }

    /**
     * Creates {@link FFAKitItem} instance.
     *
     * @param kit  which kit set to
     * @param slot to which slot save
     * @param item item base
     * @return created item
     */
    public @NotNull FFAKitItem create(final @NotNull String kit, final int slot, final @NotNull ItemStack item) {
        this.kit = kit;
        this.slot = slot;
        this.itemsAdderID = "none";

        final Material type = item.getType();
        this.setType(type);
        this.setAmount(item.getAmount());
        if (type.isLegacy()) this.setData(item.getData());
        if (item.hasItemMeta()) this.setItemMeta(item.getItemMeta());

        config.set(String.format("%s.%s.item", kit, slot), new ItemStack(this.toItem()).serialize());
        config.set(String.format("%s.%s.itemsAdder", kit, slot), "none");
        return new FFAKitItem(config, kit, slot, item);
    }

    /**
     * Creates {@link FFAKitItem} instance from ItemsAdder's item.
     *
     * @param kit        which kit set to
     * @param slot       to which slot save
     * @param customItem item base
     * @return created item
     */
    public @NotNull FFAKitItem create(final @NotNull String kit, final int slot, final @NotNull CustomStack customItem) {
        this.kit = kit;
        this.slot = slot;
        this.itemsAdderID = customItem.getId();

        final ItemStack item = customItem.getItemStack();
        final Material type = item.getType();
        this.setType(type);
        this.setAmount(item.getAmount());
        if (type.isLegacy()) this.setData(item.getData());
        if (item.hasItemMeta()) this.setItemMeta(item.getItemMeta());

        config.set(String.format("%s.%s.item", kit, slot), new ItemStack(this.toItem()).serialize());
        config.set(String.format("%s.%s.itemsAdder", kit, slot), customItem.getId());
        return new FFAKitItem(config, kit, slot, customItem);
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
        if (!isCreated(config, kit, slot)) throw new ObjectNotFoundException("FFAKitItem object is not created yet!");
        final ConfigurationSection section = config.getConfigurationSection(String.format("%s.%s.item", kit, slot));
        assert section != null;
        final String id = config.getString(String.format("%s.%s.itemsAdder", kit, slot), "none");
        if (IAManager.isIDValid(id)){
            final CustomStack customItem = CustomStack.getInstance(id);
            if(customItem != null) return new FFAKitItem(config, kit, slot, customItem);
        }
        return new FFAKitItem(config, kit, slot, ItemStack.deserialize(section.getValues(true)));
    }

    /**
     * Saves this instance to kits-config.yml file.
     *
     * @throws IOException when can't save to config
     */
    public void save() throws IOException {
        config.set(String.format("%s.%s.item", kit, slot), new ItemStack(this.toItem()).serialize());
        config.set(String.format("%s.%s.itemsAdder", kit, slot), this.getIAItemID());
        config.save();
    }


    /**
     * Get index of slot similar to
     * <p>
     * {@link org.bukkit.inventory.Inventory#getItem(int)}
     *
     * @return slot index
     */
    public int getSlotIndex() {
        return this.slot;
    }

    /**
     * Get kit's name.
     *
     * @return kit's name
     */
    public String getKitName() {
        return this.kit;
    }

    /**
     * Check if this item is itemsAdder's item.
     *
     * @return true if it is
     */
    public boolean isIAItem() {
        return !this.itemsAdderID.equals("none");
    }

    public @Nullable String getIAItemID() {
        if (isCreated()) return itemsAdderID;
        else return null;
    }

    /**
     * Returns this class as ItemStack.
     * Same as {@link #getItem()}
     *
     * @return minecraft item
     */
    public ItemStack toItem() {
        return this.getItem();
    }

    /**
     * Returns this class as ItemStack.
     * Same as {@link #toItem()}
     *
     * @return minecraft item
     */
    public ItemStack getItem() {
        return this;
    }

    /**
     * Check if this item was created.
     *
     * @return true if was created
     */
    public boolean isCreated() {
        return FFAKitItem.isCreated(config, kit, slot);
    }

    /**
     * Check if this item was created.
     *
     * @param kit  the kit's name
     * @param slot the slot index
     * @return true if was created
     */
    public static boolean isCreated(final @NotNull ConfigurationFile config, final @NotNull String kit, final int slot) {
        return config.getConfigurationSection(String.format("%s.%s.item", kit, slot)) != null;
    }
}
