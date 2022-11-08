package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.database.Connector;
import bridge.database.QueryType;
import bridge.database.Saver;
import bridge.database.UpdateType;
import bridge.modules.Currency;
import lombok.CustomLog;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@CustomLog
public class Stars extends Currency {

    private final Connector con;
    private final NicknameManager manager;
    private final Saver saver;

    public Stars(NicknameManager manager, Connector con) {
        super("stars");
        this.con = con;
        this.manager = manager;
        Bridge instance = Bridge.getInstance();
        saver = instance.getSaver();
    }

    @Override
    public Integer getCurrencyAmount(@NotNull final UUID uuid) {
        if (manager.getLatelyUsedPlayers().contains(uuid)) {
            NicknameManager.PlayerColor info = manager.getPlayerInfo(uuid);
            if (info != null) return info.stars();
        }
        ResultSet rs = con.querySQL(QueryType.SELECT_STARS, uuid.toString());
        try {
            if (rs.next()) return rs.getInt("stars");
            return rs.getInt("stars");
        } catch (SQLException e) {
            LOG.error("There was an exception with SQL", e);
        }
        return null;
    }

    @Override
    public void setCurrency(@NotNull final UUID uuid, final int amount) {
        if (manager.getLatelyUsedPlayers().contains(uuid)){
            manager.setPlayerStars(uuid, amount);
        }
        //save to database async
        saver.add(new Saver.Record(UpdateType.UPDATE_STARS, String.valueOf(amount), uuid.toString()));
    }
}
