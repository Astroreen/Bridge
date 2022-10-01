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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@CustomLog
public class TABManager implements Listener, TabEvent {

    private final Bridge instance = Bridge.getInstance();
    private final Saver saver = instance.getSaver();
    private final Connector con;
    private final Currency stars;

    private boolean isStarsEnabled;
    public TABManager () {
        con = new Connector();
        stars = new Stars(con);
    }

    protected void register () {
        if(!NicknameColorManager.setup(con)) return;
        Bukkit.getPluginManager().registerEvents(this, instance);
        TabAPI.getInstance().getEventBus().register(this);
    }

    protected void unregister () {
        TabAPI.getInstance().getEventBus().unregister(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin (@NotNull final PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()){
            //save async to database
            saver.add(new Saver.Record(UpdateType.ADD_NICKNAME, event.getPlayer().getUniqueId().toString(), NicknameColorManager.getDefaultColor()));
        }
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
        if(!p.hasPlayedBefore()) return;

        ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
        try {
            NicknameColorManager.applyNicknameColor(p, rs.getString("color"), false);
        } catch (SQLException e) {
            LOG.error("There was an exception with SQL", e);
        }
    }

    private void reload () {
        isStarsEnabled = instance.getPluginConfig().getBoolean("settings.modules.tab.UseMoney", true);
        NicknameColorManager.reload();
        new BukkitRunnable() {
            @Override
            public void run() {
                ResultSet rs = con.querySQL(QueryType.LOAD_ALL_NICKNAME_COLORS);
                try {
                    HashMap<UUID, String> data = new HashMap<>();
                    while(rs.next()) {
                        data.put(
                                UUID.fromString(rs.getString("playerID")),
                                rs.getString("color")
                        );
                    }
                    Bukkit.getOnlinePlayers().forEach((p) ->
                            NicknameColorManager.applyNicknameColor(
                                    p, data.get(p.getUniqueId()), false));
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

    public static void hidePlayersNickname () {
        //TODO hide players nickname and run this method for
    }

}
