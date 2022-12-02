package bridge.ffa;

import bridge.config.ConfigurationFile;
import bridge.exceptions.ObjectNotFoundException;
import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
            final int slot = Integer.parseInt(slotIndex);
            final FFAKitItem item = loadItem(kit, slot);
            if (item == null) continue;
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
     * Applies kit to player and set all items
     * to ones written in kit configuration.
     *
     * @param player the player to apply kit to
     * @param kit    the kit name
     */
    public static void applyKit(final @NotNull Player player, final @NotNull String kit) {
        HashMap<Integer, FFAKitItem> settings = getKit(kit);
        if (settings.isEmpty()) return;
        Inventory inv = player.getInventory();
        for (final int i : settings.keySet()) inv.setItem(i, settings.get(i));
        PlayersKits.put(player.getUniqueId(), kit);
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
