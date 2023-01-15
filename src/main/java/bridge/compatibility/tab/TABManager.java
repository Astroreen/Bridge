package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.compatibility.Compatibility;
import bridge.compatibility.CompatiblePlugin;
import common.database.Connector;
import common.database.QueryType;
import common.database.UpdateType;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CustomLog
public class TABManager implements TabEvent, Module {

    private static Bridge plugin;
    private static Connector con;
    private static boolean isModuleEnabled;
    private static boolean isStarsEnabled;
    private static Currency stars = null;
    private static NicknameManager manager = null;
    private static boolean starting = true;
    private final static Set<UUID> exist = new HashSet<>();
    private final static Set<UUID> check = new HashSet<>();

    @Subscribe
    public void onPlayerLoadEvent(final @NotNull PlayerLoadEvent event) {
        if (!event.isJoin()) {
            reload();
            return;
        }
        if (!active()) return;
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

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        TABManager.plugin = plugin;
        con = new Connector();

        isModuleEnabled = plugin.getPluginConfig().getBoolean("settings.modules.tab.ColorNickname", true);
        if (isModuleEnabled) {
            new NicknameManager(plugin);
            manager = NicknameManager.getInstance();
        }
        isStarsEnabled = plugin.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        if (isStarsEnabled) stars = new Stars(manager, con);
        TabAPI.getInstance().getEventBus().register(this);

        exist.clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                try (final ResultSet rs = con.querySQL(QueryType.LOAD_ALL_NICKNAME_UUIDS)) {
                    while (rs.next()) exist.add(UUID.fromString(rs.getString("playerID")));
                } catch (SQLException e) {
                    LOG.error("There was an exception with SQL", e);
                }
                //if player joins when list is not loaded, he will be added to the check-list.
                //after uuid was loaded, filtering the new ones and adding to database
                check.stream().filter(uuid -> !exist.contains(uuid))
                        .collect(Collectors.toSet()).forEach(uuid -> {
                            con.updateSQL(UpdateType.ADD_NICKNAME, uuid.toString(), NicknameManager.getDefaultNickColor());
                            exist.add(uuid);
                        });
                //then, apply to player their color
                Bukkit.getOnlinePlayers().forEach(p -> manager.applyColor(p, manager.getPlayerColor(p.getUniqueId()), false));
                check.clear();
                LOG.warn("Colors were returned back!");
            }
        }.runTaskAsynchronously(plugin);

        TABManager.starting = false;
        return true;
    }

    @Override
    public void reload() {
        con.refresh();
        if (!active()) return;
        isStarsEnabled = plugin.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        manager.reload();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach((p) ->
                        manager.applyColor(p, manager.getPlayerColor(p.getUniqueId()), false));
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public void disable() {
        TabAPI.getInstance().getEventBus().unregister(this);
    }

    @Override
    public boolean isConditionsMet() {
        if (!Compatibility.getHooked().contains(CompatiblePlugin.TAB)) {
            LOG.error("Can't start module '" + getName() + "'. Is this plugin exist?");
            return false;
        }
        return true;
    }

    @Override
    public boolean active() {
        return isModuleEnabled;
    }

    @Override
    public String getName() {
        return "TAB";
    }

    public static @Nullable Currency getStars() {
        if (isStarsEnabled) return stars;
        else return null;
    }

    public static @Nullable NicknameManager getManager() {
        return manager;
    }

}
