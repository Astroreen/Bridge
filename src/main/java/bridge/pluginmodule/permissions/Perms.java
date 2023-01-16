package bridge.pluginmodule.permissions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Perms {

    /**
     * Check if player have permission.
     *
     * @param player     the player
     * @param permission permission to check
     * @return true if Player have permission
     */
    default boolean havePermission(final @NotNull Player player, final @NotNull String permission) {
        return havePermission(player.getUniqueId(), permission);
    }

    /**
     * Check if player have permission.
     *
     * @param uuid       the player uuid
     * @param permission permission to check
     * @return true if Player have permission
     */

    boolean havePermission(final @NotNull UUID uuid, final @NotNull String permission);

    /**
     * Add permission to player instance.
     *
     * @param uuid       uuid of the player
     * @param permission permission to check
     * @param value      is player available to use this permission
     */
    void addPermission(final @NotNull UUID uuid, final @NotNull String permission, final boolean value);
}
