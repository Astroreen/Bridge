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
    public boolean havePermission(final @NotNull Player player, final Permission permission) {
        if(lpPermsActive) return lpPerms.havePermission(player, permission);
        else return havePermission(player.getUniqueId(), permission);
    }

    @Override
    public boolean havePermission(final @NotNull UUID uuid, final Permission permission) {
        if(lpPermsActive) {
            return lpPerms.havePermission(uuid, permission);
        } else {
            Player player = PlayerConverter.getPlayer(uuid);
            if (player == null) return false;
            return player.hasPermission(permission.perm);
        }
    }

    @Override
    public void addPermission(final @NotNull Player player, final Permission permission, final boolean value) {
        if(lpPermsActive) lpPerms.addPermission(player, permission, value);
        else addPermission(player.getUniqueId(), permission, value);
    }

    @Override
    public void addPermission(final @NotNull UUID uuid, final Permission permission, final boolean value) {
        if(lpPermsActive) {
            lpPerms.addPermission(uuid, permission, value);
        }
    }
}
