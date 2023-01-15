package bridge.modules.permissions;

import bridge.compatibility.Compatibility;
import bridge.compatibility.luckperms.LPPermissionManager;
import bridge.utils.PlayerConverter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PermissionManager implements Perms {

    private static boolean lpPermsActive;
    private LPPermissionManager lpPerms = null;

    public PermissionManager() {
        if(Compatibility.getHooked().contains("LuckPerms")) {
            lpPerms = LPPermissionManager.getInstance();
            lpPermsActive = true;
        }
    }

    @Override
    public boolean havePermission(final @NotNull Player player, final @NotNull String permission) {
        if(lpPermsActive) return lpPerms.havePermission(player, permission);
        else return havePermission(player.getUniqueId(), permission);
    }

    @Override
    public boolean havePermission(@NotNull UUID uuid, @NotNull String permission) {
        if(lpPermsActive) {
            return lpPerms.havePermission(uuid, permission);
        } else {
            Player player = PlayerConverter.getPlayer(uuid);
            if (player == null) return false;
            return player.hasPermission(permission);
        }
    }

    @Override
    public void addPermission(final @NotNull UUID uuid, final @NotNull String permission, final boolean value) {
        if(lpPermsActive) {
            lpPerms.addPermission(uuid, permission, value);
        }
    }
}
