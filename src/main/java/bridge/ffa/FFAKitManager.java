package bridge.ffa;

import bridge.compatibility.itemsadder.IAManager;
import bridge.config.ConfigurationFile;
import bridge.exceptions.ObjectNotFoundException;
import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CustomLog
public class FFAKitManager implements Listener {

    private static ConfigurationFile config;
    private static final HashMap<UUID, String> PlayersKits = new HashMap<>();

    public static void setup(final @NotNull ConfigurationFile config) {
        FFAKitManager.config = config;
    }

    /**
     * Creates kit, slot and item itself.
     *
     * @param kit  the kit's name
     * @param slot the slot index
     * @param item the item
     * @return created item
     */
    public static @NotNull FFAKitItem createKitItem(final @NotNull String kit, final int slot, final @NotNull ItemStack item) {
        return new FFAKitItem(config).create(kit, slot, item);
    }

    /**
     * Creates kit, slot and item itself
     * using ItemsAdder's item as item base.
     *
     * @param kit  the kit's name
     * @param slot the slot index
     * @param item the item base
     * @return created item
     */
    public static @NotNull FFAKitItem createKitItem(final @NotNull String kit, final int slot, final @NotNull CustomStack item) {
        return new FFAKitItem(config).create(kit, slot, item);
    }


    /**
     * Loads item from your data.
     *
     * @param kit  kit to load from
     * @param slot which item to load
     * @return {@link FFAKitItem} or null if not exist
     */
    public static @Nullable FFAKitItem loadItem(final @NotNull String kit, final int slot) {
        try {
            if (FFAKitItem.isCreated(config, kit, slot)) return new FFAKitItem(config).load(kit, slot);
        } catch (ObjectNotFoundException e) {
            LOG.error("There was exception with FFA item in kit: " + kit + ", in slot: " + slot, e);
        }
        return null;
    }

    /**
     * Applies kit to player and set all items
     * to ones written in kit configuration.
     *
     * @param player the player to apply kit to
     * @param kit    the kit name
     */
    public static void applyKit(final @NotNull Player player, final @NotNull String kit) {
        final HashMap<Integer, FFAKitItem> settings = getKit(kit);
        if (settings.isEmpty()) return;
        final PlayerInventory inv = player.getInventory();
        for (final int slotIndex : settings.keySet()) {
            final FFAKitItem item = settings.get(slotIndex);
            if(item.isIAItem())
                inv.setItem(slotIndex, CustomStack.getInstance(item.getIAItemID()).getItemStack());
            else inv.setItem(slotIndex, item);
        }
        //sets armor settings
        final ItemStack helmet = getHelmet(kit);
        final ItemStack chestplate = getChestplate(kit);
        final ItemStack leggings = getLeggings(kit);
        final ItemStack boots = getBoots(kit);
        if (helmet != null) inv.setHelmet(helmet);
        if (chestplate != null) inv.setChestplate(chestplate);
        if (leggings != null) inv.setLeggings(leggings);
        if (boots != null) inv.setBoots(boots);
        PlayersKits.put(player.getUniqueId(), kit);
    }

    /**
     * Gets kit as {@link HashMap}. Key is the slot index and value is the item.
     *
     * @param kit the kit name
     * @return kit configuration
     */
    public static @NotNull HashMap<Integer, FFAKitItem> getKit(final @NotNull String kit) {
        final ConfigurationSection section = config.getConfigurationSection(kit);
        if (section == null) return new HashMap<>();
        HashMap<Integer, FFAKitItem> map = new HashMap<>();
        for (final String slotIndex : section.getKeys(false)) {
            //if it is armor slot, don't touch it
            if (slotIndex.equals("helmet") ||
                    slotIndex.equals("chestplate") ||
                    slotIndex.equals("leggings") ||
                    slotIndex.equals("boots")) continue;
            final int slot = Integer.parseInt(slotIndex);
            final FFAKitItem item;
            try {
                item = new FFAKitItem(config).load(kit, slot);
            } catch (ObjectNotFoundException ignore) { continue; }
            map.put(slot, item);
        }
        return map;
    }

    /**
     * Gets all kits that are written in config.
     *
     * @return list of kits names
     */
    public static @NotNull List<String> getKits() {
        ConfigurationSection section = config.getDefaultSection();
        if (section == null) return List.of();
        return section.getKeys(false).stream().toList();
    }

    /**
     * Saves all items that are in array.
     *
     * @param items the array full with {@link FFAKitItem}
     * @throws IOException when trying to save
     */
    public static void saveAll(final @NotNull List<FFAKitItem> items) throws IOException {
        for (final FFAKitItem item : items) {
            config.set(String.format("%s.%s.item", item.getKitName(), item.getSlotIndex()), new ItemStack(item.toItem()).serialize());
            if (item.isIAItem())
                config.set(String.format("%s.%s.itemsAdder", item.getKitName(), item.getSlotIndex()), item.getIAItemID());
        }
        config.save();
    }

    /**
     * Removes kit that was applied to player.
     *
     * @param player the player to remove kit from
     */
    public static void removeKit(final @NotNull Player player) {
        PlayersKits.remove(player.getUniqueId());
    }

    /**
     * Get kit name using player's UUID.
     *
     * @param player the player to get kit from
     * @return the kit name
     */
    public static @Nullable String getPlayerKitName(final @NotNull Player player) {
        return PlayersKits.get(player.getUniqueId());
    }

    /**
     * Gets helmet from kit.
     *
     * @param kit kit's name
     * @return helmet
     */
    public static @Nullable ItemStack getHelmet(final @NotNull String kit) {
        if (!isKitCreated(kit)) return null;
        final ConfigurationSection section = config.getConfigurationSection(String.format("%s.helmet.item", kit));
        if (section == null) return null;
        final String id = config.getString(String.format("%s.helmet.itemsAdder", kit), "none");
        if (IAManager.isIDValid(id)) return CustomStack.getInstance(id).getItemStack();
        return ItemStack.deserialize(section.getValues(true));
    }

    /**
     * Gets chestplate from kit.
     *
     * @param kit kit's name
     * @return chestplate
     */
    public static @Nullable ItemStack getChestplate(final @NotNull String kit) {
        if (!isKitCreated(kit)) return null;
        final ConfigurationSection section = config.getConfigurationSection(String.format("%s.chestplate.item", kit));
        if (section == null) return null;
        final String id = config.getString(String.format("%s.chestplate.itemsAdder", kit), "none");
        if (IAManager.isIDValid(id)) return CustomStack.getInstance(id).getItemStack();
        return ItemStack.deserialize(section.getValues(true));
    }

    /**
     * Gets leggings from kit.
     *
     * @param kit kit's name
     * @return leggings
     */
    public static @Nullable ItemStack getLeggings(final @NotNull String kit) {
        if (!isKitCreated(kit)) return null;
        final ConfigurationSection section = config.getConfigurationSection(String.format("%s.leggings.item", kit));
        if (section == null) return null;
        final String id = config.getString(String.format("%s.leggings.itemsAdder", kit), "none");
        if (IAManager.isIDValid(id)) return CustomStack.getInstance(id).getItemStack();
        return ItemStack.deserialize(section.getValues(true));
    }

    /**
     * Gets boots from kit.
     *
     * @param kit kit's name
     * @return boots
     */
    public static @Nullable ItemStack getBoots(final @NotNull String kit) {
        if (!isKitCreated(kit)) return null;
        final ConfigurationSection section = config.getConfigurationSection(String.format("%s.boots.item", kit));
        if (section == null) return null;
        final String id = config.getString(String.format("%s.boots.itemsAdder", kit), "none");
        if (IAManager.isIDValid(id)) return CustomStack.getInstance(id).getItemStack();
        return ItemStack.deserialize(section.getValues(true));
    }

    /**
     * Sets boots for kit.
     *
     * @param kit kit's name
     */
    public static void setHelmet(final @NotNull String kit, final @NotNull ItemStack item) {
        if (!isKitCreated(kit)) return;
        config.set(String.format("%s.helmet.item", kit), item.serialize());
        final CustomStack customItem = CustomStack.byItemStack(item);
        if (customItem != null) config.set(String.format("%s.helmet.itemsAdder", kit), customItem.getId());
        else config.set(String.format("%s.helmet.itemsAdder", kit), "none");
    }

    /**
     * Sets chestplate for kit.
     *
     * @param kit kit's name
     */
    public static void setChestplate(final @NotNull String kit, final @NotNull ItemStack item) {
        if (!isKitCreated(kit)) return;
        config.set(String.format("%s.chestplate.item", kit), item.serialize());
        final CustomStack customItem = CustomStack.byItemStack(item);
        if (customItem != null) config.set(String.format("%s.chestplate.itemsAdder", kit), customItem.getId());
        else config.set(String.format("%s.chestplate.itemsAdder", kit), "none");
    }

    /**
     * Sets leggings for kit.
     *
     * @param kit kit's name
     */
    public static void setLeggings(final @NotNull String kit, final @NotNull ItemStack item) {
        if (!isKitCreated(kit)) return;
        config.set(String.format("%s.leggings.item", kit), item.serialize());
        final CustomStack customItem = CustomStack.byItemStack(item);
        if (customItem != null) config.set(String.format("%s.leggings.itemsAdder", kit), customItem.getId());
        else config.set(String.format("%s.leggings.itemsAdder", kit), "none");
    }

    /**
     * Sets boots for kit.
     *
     * @param kit kit's name
     */
    public static void setBoots(final @NotNull String kit, final @NotNull ItemStack item) {
        if (!isKitCreated(kit)) return;
        config.set(String.format("%s.boots.item", kit), item.serialize());
        final CustomStack customItem = CustomStack.byItemStack(item);
        if (customItem != null) config.set(String.format("%s.boots.itemsAdder", kit), customItem.getId());
        else config.set(String.format("%s.boots.itemsAdder", kit), "none");
    }

    /**
     * Check if kit exist.
     *
     * @param kit the kit name to check
     * @return true, if exist
     */
    public static boolean isKitCreated(final @NotNull String kit) {
        return config.getConfigurationSection(kit) != null;
    }

    /**
     * Check if slot is available (empty).
     *
     * @param kit  the kit name
     * @param slot the slot index to check
     * @return true, if slot not exist
     */
    public static boolean isSlotAvailable(final @NotNull String kit, final int slot) {
        return config.getConfigurationSection(String.format("%s.%s", kit, slot)) == null;
    }
}
