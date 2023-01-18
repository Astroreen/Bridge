package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.compatibility.Compatibility;
import bridge.compatibility.CompatiblePlugin;
import common.IModule;
import bridge.module.nickname.NicknameManager;
import bridge.module.nickname.Stars;
import common.database.Connector;
import common.database.QueryType;
import common.database.UpdateType;
import lombok.CustomLog;
import me.neznamy.tab.api.TabAPI;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

import static bridge.compatibility.tab.TABManager.*;

@CustomLog
public class BRTABModule implements IModule {

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        TABManager.plugin = plugin;
        final Connector con = new Connector();

        isModuleEnabled = plugin.getPluginConfig().getBoolean("modules.nickname.color-nickname", true);
        if (isModuleEnabled) {
            new NicknameManager(plugin);
            manager = NicknameManager.getInstance();
        }
        isStarsEnabled = plugin.getPluginConfig().getBoolean("modules.nickname.money-to-change-nickname", true);
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
        TABManager.reload();
    }

    @Override
    public void disable() {
        TABManager.disable();
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
    public String getName() {
        return "TAB";
    }
}
