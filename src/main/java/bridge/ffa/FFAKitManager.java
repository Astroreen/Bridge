package bridge.ffa;

import bridge.config.ConfigurationFile;
import bridge.exceptions.ObjectNotFoundException;
import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@CustomLog
public class FFAKitManager {

    private static ConfigurationFile config;

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
    public static FFAKitItem createKitItem(final @NotNull String kit, final int slot, final @NotNull ItemStack item) {
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
    public static FFAKitItem createKitItem(final @NotNull String kit, final int slot, final @NotNull CustomStack item) {
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
            if (FFAKitItem.isCreated(kit, slot)) return new FFAKitItem(config).load(kit, slot);
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
        if (!isKitCreated(kit)) return new HashMap<>();
        final ConfigurationSection section = config.getConfigurationSection(kit);
        if (section == null) return new HashMap<>();
        HashMap<Integer, FFAKitItem> map = new HashMap<>();
        for (String slotIndex : section.getKeys(false)) {
            final int slot = Integer.parseInt(slotIndex);
            final FFAKitItem item = loadItem(kit, slot);
            if (item == null) continue;
            map.put(slot, item);
        }
        return map;
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
