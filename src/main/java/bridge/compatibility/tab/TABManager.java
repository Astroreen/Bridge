package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.database.Connector;
import bridge.database.QueryType;
import bridge.database.Saver;
import bridge.database.UpdateType;
import bridge.modules.Currency;
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
public class TABManager implements TabEvent {

    private final Bridge instance;
    private final Saver saver;
    private final Connector con;
    private final Currency stars;
    private final List<UUID> exist;
    private static boolean isModuleEnabled;
    private boolean isStarsEnabled;
    private NicknameColorManager manager = null;

    public TABManager () {
        instance = Bridge.getInstance();
        saver = instance.getSaver();
        isModuleEnabled = instance.getPluginConfig().getBoolean("settings.modules.tab.ColorNickname", true);
        isStarsEnabled = instance.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        if (isModuleEnabled) manager = new NicknameColorManager(instance);
        con = new Connector();
        exist = new ArrayList<>();
        stars = new Stars(manager, con);
    }

    protected void register () {
        TabAPI.getInstance().getEventBus().register(this);
        exist.clear();
        new BukkitRunnable() {
            @Override
            public void run() {
                ResultSet rs = con.querySQL(QueryType.LOAD_ALL_NICKNAME_UUIDS);
                try {
                    while(rs.next())
                        exist.add(UUID.fromString(rs.getString("playerID")));
                } catch (SQLException e) {
                    LOG.error("There was an exception with SQL", e);
                }
            }
        }.runTaskAsynchronously(instance);
    }

    protected void unregister () {
        TabAPI.getInstance().getEventBus().unregister(this);
    }

    @Subscribe
    public void onPlayerLoad (@NotNull final PlayerLoadEvent event) {
        if (!event.isJoin()) {
            reload();
            return;
        }
        final TabPlayer player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Player p = PlayerConverter.getPlayer(uuid);

        //cuz event was fired, and we can get from it TabPlayer instance
        assert p != null;
        if(!exist.contains(uuid)){
            exist.add(uuid);
            String color = manager.getDefaultColor();
            manager.applyNicknameColor(p, color, false);
            saver.add(new Saver.Record(UpdateType.ADD_NICKNAME, uuid.toString(), color));
            return;
        }

        try {
            ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
            if(rs.next()){
                final String hex = rs.getString("color");
                if (hex == null) manager.applyNicknameColor(p, manager.getDefaultColor(), false);
                manager.applyNicknameColor(p, hex, false);
            }
        } catch (SQLException e) {
            LOG.error("There was an exception with SQL", e);
            manager.applyNicknameColor(p, manager.getDefaultColor(), false);
        }
    }

    protected void reload () {
        isStarsEnabled = instance.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        manager.reload();
        exist.clear();
        new BukkitRunnable() {
            @Override
            public void run() {
                ResultSet rs = con.querySQL(QueryType.LOAD_ALL_NICKNAME_COLORS);
                try {
                    HashMap<UUID, String> data = new HashMap<>();
                    while(rs.next()) {
                        UUID uuid = UUID.fromString(rs.getString("playerID"));
                        exist.add(uuid);
                        data.put(
                                uuid,
                                rs.getString("color")
                        );
                    }
                    Bukkit.getOnlinePlayers().forEach((p) ->
                            manager.applyNicknameColor(p, data.get(p.getUniqueId()), false));
                } catch (SQLException e) {
                    LOG.error("There was an exception with SQL", e);
                }
            }
        }.runTaskAsynchronously(instance);
    }

    public @Nullable Currency getStars() {
        if (isStarsEnabled) return stars;
        else return null;
    }

    public @Nullable NicknameColorManager getManager () {
        return manager;
    }
    public static boolean isModuleEnabled() {
        return isModuleEnabled;
    }

}
