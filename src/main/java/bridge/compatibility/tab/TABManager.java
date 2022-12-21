package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.compatibility.Compatibility;
import bridge.compatibility.CompatiblePlugin;
import bridge.database.Connector;
import bridge.database.QueryType;
import bridge.database.UpdateType;
import bridge.modules.Currency;
import bridge.modules.Module;
import bridge.utils.ColorCodes;
import bridge.utils.PlayerConverter;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CustomLog
public class TABManager implements TabEvent, Module {

    private static Bridge plugin;
    private static Connector con;
    private static boolean isModuleEnabled;
    private static boolean isStarsEnabled;
    private static Currency stars = null;
    private static NicknameManager manager = null;
    private final static List<UUID> exist = new ArrayList<>();

    public void setup() {
        plugin = Bridge.getInstance();
        con = new Connector();

        isModuleEnabled = plugin.getPluginConfig().getBoolean("settings.modules.tab.ColorNickname", true);
        if (isModuleEnabled) {
            new NicknameManager(plugin);
            manager = NicknameManager.getInstance();
        }
        isStarsEnabled = plugin.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        if (isStarsEnabled) stars = new Stars(manager, con);
        TabAPI.getInstance().getEventBus().register(this);
    }

    @Subscribe
    public void onPlayerLoadEvent (final @NotNull PlayerLoadEvent event) {
        if (!event.isJoin()) {
            reload();
            return;
        }
        if(!active()) return;
        final TabPlayer player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Player p = PlayerConverter.getPlayer(uuid);
        //cuz event was fired, and we can get from it TabPlayer instance
        assert p != null;
        if(!exist.contains(uuid)){
            con.updateSQL(UpdateType.ADD_NICKNAME, uuid.toString(), NicknameManager.getDefaultNickColor());
            exist.add(uuid);
            manager.applyColor(p, NicknameManager.getDefaultNickColor(), false);
            return;
        }

        final String color = manager.getPlayerColor(uuid);
        if(color == null || !(NicknameManager.getInstance().isGradient(color)
                || ColorCodes.isHexValid(color))) manager.applyColor(p, NicknameManager.getDefaultNickColor(), true);
        else manager.applyColor(p, color, true);
    }

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        exist.clear();
        new BukkitRunnable() {
            @Override
            public void run() {
                final ResultSet rs = con.querySQL(QueryType.LOAD_ALL_NICKNAME_UUIDS);
                try {
                    while(rs.next())
                        exist.add(UUID.fromString(rs.getString("playerID")));
                } catch (SQLException e) {
                    LOG.error("There was an exception with SQL", e);
                }
            }
        }.runTaskAsynchronously(TABManager.plugin);
        return true;
    }

    @Override
    public void reload () {
        con.refresh();
        if (!active()) return;
        isStarsEnabled = plugin.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        manager.reload();
        exist.clear();
        new BukkitRunnable() {
            @Override
            public void run() {
                final ResultSet rs = con.querySQL(QueryType.LOAD_ALL_NICKNAME_COLORS);
                try {
                    final HashMap<UUID, String> data = new HashMap<>();
                    while(rs.next()) {
                        final UUID uuid = UUID.fromString(rs.getString("playerID"));
                        exist.add(uuid);
                        data.put(
                                uuid,
                                rs.getString("color")
                        );
                    }
                    Bukkit.getOnlinePlayers().forEach((p) ->
                            manager.applyColor(p, data.get(p.getUniqueId()), false));
                } catch (SQLException e) {
                    LOG.error("There was an exception with SQL", e);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public void disable() {
        TabAPI.getInstance().getEventBus().unregister(this);
    }

    @Override
    public boolean isConditionsMet() {
        if(!Compatibility.getHooked().contains(CompatiblePlugin.TAB)){
            LOG.error("Can't start module 'TAB'. Is this plugin exist?");
            return false;
        }
        return true;
    }

    @Override
    public boolean active() {
        return isModuleEnabled;
    }

    public static @Nullable Currency getStars() {
        if (isStarsEnabled) return stars;
        else return null;
    }

    public static @Nullable NicknameManager getManager () {
        return manager;
    }

}
