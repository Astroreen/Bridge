package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.module.nickname.NicknameManager;
import bridge.utils.ColorCodes;
import bridge.utils.PlayerConverter;
import common.Currency;
import common.database.Connector;
import common.database.UpdateType;
import lombok.CustomLog;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.Subscribe;
import me.neznamy.tab.api.event.TabEvent;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@CustomLog
public class TABManager implements TabEvent {

    protected static Bridge plugin;
    protected static Connector con;
    protected static boolean isModuleEnabled;
    protected static boolean isStarsEnabled;
    protected static Currency stars = null;
    protected static NicknameManager manager = null;
    protected static boolean starting = true;
    protected final static Set<UUID> exist = new HashSet<>();
    protected final static Set<UUID> check = new HashSet<>();
    private static TABManager instance;

    public TABManager() {instance = this;}

    @Subscribe
    public void onPlayerLoadEvent(final @NotNull PlayerLoadEvent event) {
        if (!event.isJoin()) {
            reload();
            return;
        }
        if (!isModuleEnabled) return;
        final TabPlayer player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Player p = PlayerConverter.getPlayer(uuid);
        //cuz event was fired, and we can get from it TabPlayer instance
        assert p != null;
        if (!exist.contains(uuid)) {
            if(starting) check.add(uuid);
            else con.updateSQL(UpdateType.ADD_NICKNAME, uuid.toString(), NicknameManager.getDefaultNickColor());
            manager.applyColor(p, NicknameManager.getDefaultNickColor(), false);
            return;
        }

        final String color = manager.getPlayerColor(uuid);
        if (color == null || !(NicknameManager.getInstance().isGradient(color)
                || ColorCodes.isHexValid(color))) manager.applyColor(p, NicknameManager.getDefaultNickColor(), false);
        else manager.applyColor(p, color, false);
    }

    protected static void reload() {
        con.refresh();
        if (!isModuleEnabled) return;
        isStarsEnabled = plugin.getPluginConfig().getBoolean("modules.nickname.money-to-change-nickname", true);
        manager.reload();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach((p) ->
                        manager.applyColor(p, manager.getPlayerColor(p.getUniqueId()), false));
            }
        }.runTaskAsynchronously(plugin);
    }


    protected static void disable() {
        TabAPI.getInstance().getEventBus().unregister(instance);
    }

    public static @Nullable Currency getStars() {
        if (isStarsEnabled) return stars;
        else return null;
    }

    public static @Nullable NicknameManager getManager() {
        return manager;
    }

}
