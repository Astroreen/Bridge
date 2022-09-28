package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.config.ConfigurationFile;
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
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@CustomLog
public class TABManager implements Listener, TabEvent {

    private final Bridge instance = Bridge.getInstance();
    private final Saver saver = instance.getSaver();
    private final Connector con;
    private final Currency currency;
    public TABManager () {
        con = new Connector();
        currency = new Stars(con);
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
        if (!event.isJoin()) reload();
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

    protected void reload () {
        //TODO grab players on their server, load their colors and apply
        //example SELECT color
    }

    public static void hidePlayersNickname () {
        //TODO hide players nickname and run this method for
    }

}
